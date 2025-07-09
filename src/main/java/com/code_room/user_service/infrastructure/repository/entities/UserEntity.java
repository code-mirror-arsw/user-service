package com.code_room.user_service.infrastructure.repository.entities;

import com.code_room.user_service.domain.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    private String id;

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String identification;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PublicProfileEntity publicProfile;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "enabled")
    private boolean enabled = false;

    @Column(name = "uri_cv_file")
    private String uriCvFile;

    public boolean isEnabled() {
        return enabled;
    }


    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}


