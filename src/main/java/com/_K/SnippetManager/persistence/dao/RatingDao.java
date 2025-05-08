package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Rating;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingDao extends JpaRepository<Rating,Long> {
    boolean existsByUserAndSnippet(User user, Snippet snippet);
    // Get all ratings by a user for published snippets
    @Query("SELECT r FROM Rating r WHERE r.user = :user AND r.snippet.isPublished = true")
    List<Rating> findByUserAndPublishedSnippet(@Param("user") User user);


    // Get all ratings by user for published snippets, ordered by snippet creation date
    @Query("SELECT r FROM Rating r WHERE r.user = :user AND r.snippet.isPublished = true ORDER BY r.snippet.createdAt DESC")
    List<Rating> findByUserAndPublishedSnippetOrderByCreatedAtDesc(@Param("user") User user);


}


