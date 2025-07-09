package com.code_room.user_service.infrastructure.repository;

import com.code_room.user_service.infrastructure.repository.entities.PublicProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicProfileRepository extends JpaRepository<PublicProfileEntity, String> {
}