package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageDao extends JpaRepository<Language,Long> {
    // ✅ Find language by name (case insensitive if needed)
    Optional<Language> findByName(String name);
    Page<Language> findByIsDeletedFalse(Pageable pageable);

    Page<Language> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
}
