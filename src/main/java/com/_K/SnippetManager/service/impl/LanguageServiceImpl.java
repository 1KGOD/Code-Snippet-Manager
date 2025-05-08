package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.service.LanguageService;
import com._K.SnippetManager.web.form.LanguageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageDao languageDao;


    @Override
    public void saveLanguage(LanguageForm languageForm) {
        Language language = new Language(languageForm);
        language.setLanguageId(languageForm.getLanguageId());
        language.setName(languageForm.getName());
        languageDao.save(language);
    }
}
