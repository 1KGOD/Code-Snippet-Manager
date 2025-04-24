package com._K.SnippetManager.service;

import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.web.form.SnippetForm;

public interface SnippetService {

    void saveSnippet(SnippetForm snippetForm , User users);
}
