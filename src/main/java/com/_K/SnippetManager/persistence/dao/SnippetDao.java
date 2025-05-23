package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Language;
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



    Optional<Snippet> findBySnippetIdAndIsDeletedFalse(Long snippetId);


    @Query("SELECT s FROM Snippet s WHERE s.isDeleted = false AND s.isPublished = true AND " +
            "(LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.user.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:language IS NULL OR s.language.name = :language)")
    Page<Snippet> searchPublishedAndNotDeleted(@Param("search") String search,
                                               @Param("language") String language,
                                               Pageable pageable);

    long countByUser_UserIDAndIsDeletedFalseAndIsPublishedTrue(Long userId);

    @Query("SELECT s FROM Snippet s " +
            "JOIN s.ratings r " +
            "WHERE s.isPublished = true " +
            "GROUP BY s.snippetId " +
            "ORDER BY COUNT(r) DESC")
    List<Snippet> findTop3RatedSnippetsByCount();

    List<Snippet> findByUserAndIsDeletedFalse(User user);

    List<Snippet> findByUserAndUserIsDeletedFalseAndIsDeletedFalseAndIsPublishedTrue(User user);

    List<Snippet> findByUserAndUserIsDeletedFalseAndIsDeletedFalseAndIsPublishedFalse(User user);

    List<Snippet> findBySharedWithUsersUserIDAndIsDeletedFalseAndIsPublishedFalse(Long userId);


    Boolean existsByLanguageAndIsDeletedFalse(Language language);


}
