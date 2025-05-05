package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Comment;
import com._K.SnippetManager.persistence.entity.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Long> {
    List<Comment> findBySnippetOrderByCreatedAtDesc(Snippet snippet);
}
