package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.SnippetService;
import com._K.SnippetManager.web.form.SnippetForm;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SnippetServiceImpl implements SnippetService {

    private final SnippetDao snippetDao;
    private final LanguageDao languageDao;

    public SnippetServiceImpl(SnippetDao snippetDao, LanguageDao languageDao) {
        this.snippetDao = snippetDao;
        this.languageDao = languageDao;
    }

    @Override
    public void saveSnippet(SnippetForm snippetForm, User user) {
        Snippet snippet = new Snippet(snippetForm);
        // snippetform-> language -> snippet entity -> db
        //fetch data via db by finding id of language -> get data from db -> set that data to snippet entity
        Language language = languageDao.findById(snippetForm.getLanguage().getLanguageId()).orElseThrow( ()-> new RuntimeException("language not Found"));
        snippet.setUser(user);
        snippet.setLanguage(language);
        snippet.setCreatedAt(LocalDateTime.now());
        snippetDao.save(snippet);
    }

    @Override
    public Page<SnippetForm> getAllSnippets(Long userId, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Snippet> snippets;

        if (keyword != null && !keyword.isEmpty()) {
            snippets = snippetDao.findUserSnippetsByKeyword(userId ,keyword, pageable);
        } else {
            snippets = snippetDao.findByUserUserIDAndIsDeletedFalse(userId,pageable);
        }

        List<SnippetForm> snippetForms = snippets.getContent().stream()
                .map(SnippetForm::new)
                .toList();

        return new PageImpl<>(snippetForms, pageable, snippets.getTotalElements());
    }



}
