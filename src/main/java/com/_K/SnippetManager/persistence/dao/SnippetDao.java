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
import java.util.Optional;

@Repository
public interface SnippetDao extends JpaRepository<Snippet , Long> {

    @Query("SELECT s FROM Snippet s WHERE s.isDeleted = false")
    Page<Snippet>findByIsDeletedFalse(Pageable pageable);

    List<Snippet> findByUser_UserIDAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);

    @Query("SELECT s FROM Snippet s " +
            "WHERE (s.user.userID = :userId AND s.isDeleted = false) " +
            "AND (" +
            "LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.language.name) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            ")")
    Page<Snippet> findUserSnippetsByKeyword(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            Pageable pageable);

    Page<Snippet> findByUserUserIDAndIsDeletedFalse(Long userId, Pageable pageable);

    Optional<Snippet> findBySnippetIdAndUserAndIsDeletedFalse(Long snippetId, User user);





    long countByIsDeletedFalse();
}
