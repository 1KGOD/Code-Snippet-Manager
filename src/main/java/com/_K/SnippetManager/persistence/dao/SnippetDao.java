package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnippetDao extends JpaRepository<Snippet , Long> {
    List<Snippet>findByIsDeletedFalse();
    List<Snippet>findByUserAndIsDeletedFalse(User user);
    List<Snippet> findByUser_UserIDAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);
    long countByIsDeletedFalse();
}
