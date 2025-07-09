package com.code_room.user_service.infrastructure.repository.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "public_profile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicProfileEntity {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private UserEntity user;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String experienceHighlights;
}