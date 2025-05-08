package com._K.SnippetManager.web.form;

import com._K.SnippetManager.persistence.entity.Language;

public class LanguageForm {
    private Long languageId;
    private String name;

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LanguageForm(){};

    public LanguageForm(Language language){
        this.setLanguageId(language.getLanguageId());
        this.setName(language.getName());
    }
}
