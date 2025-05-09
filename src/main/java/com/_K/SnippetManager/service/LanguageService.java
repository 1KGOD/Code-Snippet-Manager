package com._K.SnippetManager.service;

import com._K.SnippetManager.web.form.LanguageForm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


public interface LanguageService {

    void saveLanguage(LanguageForm languageForm);
    Page<LanguageForm> getAllLanguages(int page, int size, String keyword);
    void editLanguage(Long languageId , LanguageForm languageForm);
    void deleteLanguage(Long languageId);
}
