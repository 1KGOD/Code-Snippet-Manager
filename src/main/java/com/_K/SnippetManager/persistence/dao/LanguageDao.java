package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageDao extends JpaRepository<Language,Long> {

}
