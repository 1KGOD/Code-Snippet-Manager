package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.service.LanguageService;
import com._K.SnippetManager.web.form.LanguageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageDao languageDao;

    @Autowired
    private SnippetDao snippetDao;


    @Override
    public void saveLanguage(LanguageForm languageForm) {
        Language language = new Language(languageForm);
        language.setLanguageId(languageForm.getLanguageId());
        language.setName(languageForm.getName());
        languageDao.save(language);
    }

    @Override
    public Page<LanguageForm> getAllLanguages(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("name").ascending());
        Page<Language> languages;

        if(keyword != null && !keyword.isEmpty()){
            languages = languageDao.findByNameContainingIgnoreCaseAndIsDeletedFalse(keyword,pageable);
        }else{
            languages = languageDao.findByIsDeletedFalse(pageable);
        }

        List<LanguageForm> languageForms = languages.getContent()
                .stream().
                map(LanguageForm::new)
                .toList();
        return new PageImpl<>(languageForms, pageable, languages.getTotalElements());
    }

    @Override
    public void editLanguage(Long languageId , LanguageForm languageForm) {
        Optional<Language> languages = languageDao.findById(languageId);
        if(languages.isPresent()){
            Language language = languages.get();
            language.setName(languageForm.getName());
            language.setUpdateAt(LocalDateTime.now());
            languageDao.save(language);
        }
    }

    @Override
    public void deleteLanguage(Long languageId) {
        Optional<Language> languages = languageDao.findById(languageId);
        if(languages.isPresent()){
            Language language = languages.get();
            Boolean isUsed = snippetDao.existsByLanguageAndIsDeletedFalse(language);

            if(isUsed){
                throw new IllegalStateException("The language you attempted to delete is currently used by at least one snippet.");
            }

            language.setDeleted(true);
            languageDao.save(language);
        } else {
            throw new NoSuchElementException("Language not found with ID: " + languageId);
        }
    }
}
