package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User , Long> {
    List<User>findByIsDeletedFalse();
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND u.isDeleted = false")
    Page<User> searchUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);
    Page<User> findByIsDeletedFalse(Pageable pageable);


}
