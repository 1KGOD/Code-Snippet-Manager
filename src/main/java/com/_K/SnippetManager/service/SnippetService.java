package com._K.SnippetManager.service;

import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.web.form.SnippetForm;
import org.springframework.data.domain.Page;

public interface SnippetService {

    void saveSnippet(SnippetForm snippetForm , User users);
    Page<SnippetForm> getAllSnippets(Long userId,int page, int size , String keywork);
    void editSnippet(SnippetForm snippetForm,User user);
    void deleteSnippet(Long snippetId, User user);
}
