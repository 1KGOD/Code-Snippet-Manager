package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User , Long> {
    List<User>findByIsDeletedFalse();
    Optional<User> findByEmailAndIsDeletedFalse(String email);
}
