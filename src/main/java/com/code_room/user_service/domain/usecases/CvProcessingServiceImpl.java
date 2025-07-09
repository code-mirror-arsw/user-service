package com.code_room.user_service.domain.usecases;

import com.code_room.user_service.domain.ports.CvProcessingService;
import com.code_room.user_service.infrastructure.controller.dto.PublicProfileDto;
import com.code_room.user_service.infrastructure.repository.PublicProfileRepository;
import com.code_room.user_service.infrastructure.repository.UserRepository;
import com.code_room.user_service.infrastructure.repository.entities.PublicProfileEntity;
import com.code_room.user_service.infrastructure.repository.entities.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor // 1. Mejor práctica: Inyección por constructor
public class CvProcessingServiceImpl implements CvProcessingService {

    // Las dependencias son 'final' para garantizar la inmutabilidad
    private final WebClient openAIWebClient;
    private final UserRepository userRepository;
    private final PublicProfileRepository publicProfileRepository;
    private final ObjectMapper objectMapper; // 2. Inyectamos ObjectMapper para parsear JSON

    @Value("${openai.api.model}")
    private String openApiModel;

    @Override
    public PublicProfileDto processCvAndGenerateProfile(String userId, MultipartFile cvFile) throws IOException {
        String cvText = extractTextFromPdf(cvFile);
        String prompt = buildPrompt(cvText);

        // .block() convierte la llamada asíncrona en síncrona para este flujo.
        PublicProfileDto profileDto = callOpenAiApi(prompt).block();

        if (profileDto != null) {
            savePublicProfile(userId, profileDto);
        }

        return profileDto;
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        return "hola";
    }

    private String buildPrompt(String cvText) {
        // Limitar el texto para no exceder el límite de tokens de la API
        String truncatedText = cvText.length() > 15000 ? cvText.substring(0, 15000) : cvText;

        return """
               Analiza el siguiente texto de un Curriculum Vitae y extrae la siguiente información en formato JSON.
               No incluyas nada más que el objeto JSON en tu respuesta.

               1. "summary": Un resumen profesional conciso de máximo 150 palabras.
               2. "skills": Una lista de las 10 habilidades técnicas o blandas más importantes.
               3. "experienceHighlights": Una lista de 3 a 5 puntos clave que resuman la experiencia laboral más relevante.

               Texto del CV:
               ---
               %s
               ---
               """.formatted(truncatedText);
    }

    private Mono<PublicProfileDto> callOpenAiApi(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", openApiModel,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "response_format", Map.of("type", "json_object")
        );

        return openAIWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::parseOpenApiResponse); // 3. Lógica de parseo extraída a un método limpio
    }

    private PublicProfileDto parseOpenApiResponse(Map<String, Object> responseMap) {
        try {
            // Navegamos de forma segura por la estructura de la respuesta de OpenAI
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new IllegalStateException("La respuesta de OpenAI no contiene 'choices'.");
            }
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String jsonContent = (String) message.get("content");

            // Usamos ObjectMapper para convertir el string JSON a nuestro DTO de forma segura
            return objectMapper.readValue(jsonContent, PublicProfileDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error al parsear el JSON de la respuesta de OpenAI", e);
            throw new RuntimeException("Error al procesar la respuesta de la IA.", e);
        } catch (ClassCastException | NullPointerException e) {
            log.error("La estructura de la respuesta de OpenAI no es la esperada. Respuesta: {}", responseMap, e);
            throw new IllegalStateException("La estructura de la respuesta de la IA ha cambiado.", e);
        }
    }

    private void savePublicProfile(String userId, PublicProfileDto dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        PublicProfileEntity profile = PublicProfileEntity.builder()
                .user(user)
                .summary(dto.getSummary())
                .skills(String.join(", ", dto.getSkills()))
                .experienceHighlights(String.join(" | ", dto.getExperienceHighlights()))
                .build();

        publicProfileRepository.save(profile);
    }
}