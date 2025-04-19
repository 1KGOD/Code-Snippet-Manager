package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnippetDao extends JpaRepository<Snippet , Long> {
    List<Snippet>findByIsDeletedFalse();
    long countByIsDeletedFalse();
}
