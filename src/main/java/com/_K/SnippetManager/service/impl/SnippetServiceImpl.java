package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.service.SnippetService;
import com._K.SnippetManager.web.form.SnippetForm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SnippetServiceImpl implements SnippetService {

    private final SnippetDao snippetDao;
    private final LanguageDao languageDao;

    public SnippetServiceImpl(SnippetDao snippetDao, LanguageDao languageDao) {
        this.snippetDao = snippetDao;
        this.languageDao = languageDao;
    }

    @Override
    public void saveSnippet(SnippetForm snippetForm) {
        Snippet snippet = new Snippet(snippetForm);
        // snippetform-> language -> snippet entity -> db
        //fetch data via db by finding id of language -> get data from db -> set that data to snippet entity
        Language language = languageDao.findById(snippetForm.getLanguage().getLanguageId()).orElseThrow( ()-> new RuntimeException("language not Found"));
        snippet.setLanguage(language);
        snippet.setCreatedAt(LocalDateTime.now());
        snippetDao.save(snippet);
    }
}
