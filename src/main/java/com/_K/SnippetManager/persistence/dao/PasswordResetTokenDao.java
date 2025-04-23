package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.PasswordRestToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenDao extends JpaRepository<PasswordRestToken,Long> {
    PasswordRestToken findByToken(String token);
    Optional<PasswordRestToken> findByTokenAndUserIsDeletedFalse(String token);
}
