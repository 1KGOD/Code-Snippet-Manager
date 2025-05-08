package com._K.SnippetManager.service;

import com._K.SnippetManager.web.form.LanguageForm;
import org.springframework.stereotype.Service;


public interface LanguageService {

    void saveLanguage(LanguageForm languageForm);
}
