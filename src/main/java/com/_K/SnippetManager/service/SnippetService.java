package com._K.SnippetManager.service;

import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.web.form.SnippetForm;
import com._K.SnippetManager.web.form.UserForm;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SnippetService {

    void saveSnippet(SnippetForm snippetForm , User users);
    Page<SnippetForm> getAllSnippets(Long userId,int page, int size , String keywork);
    void editSnippet(SnippetForm snippetForm,User user);
    void deleteSnippet(Long snippetId, User user);
    List<Snippet> publishedSnippet(User user);
    List<Snippet> privatedSnippet(User user);
    List<Snippet> sharedSnippets(Long userId);
    void shareSnippetWithUser(Long snippetId, Long userId);
}
