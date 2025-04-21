package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnippetDao extends JpaRepository<Snippet , Long> {

    @Query("SELECT s FROM Snippet s WHERE s.isDeleted = false")
    Page<Snippet>findByIsDeletedFalse(Pageable pageable);

    List<Snippet> findByUser_UserIDAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);

    @Query("SELECT s FROM Snippet s WHERE " +
            "(s.title LIKE %:keyword% OR s.user.name LIKE %:keyword% OR s.language.name LIKE %:keyword%) " +
            "AND s.isDeleted = false")
    Page<Snippet> findBySearchTerm(@Param("keyword") String keyword, Pageable pageable);

    long countByIsDeletedFalse();
}
