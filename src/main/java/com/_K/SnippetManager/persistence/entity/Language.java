package com._K.SnippetManager.persistence.entity;


import com._K.SnippetManager.web.form.LanguageForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "language")
@Getter
@Setter
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "languageId")
    private Long languageId;

    private String name;

    private Boolean isDeleted = false;

    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "language")
    @JsonIgnore
    private List<Snippet> snippet;


    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

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

    public void setSnippet(List<Snippet> snippet) {
        this.snippet = snippet;
    }

    public Snippet getSnippet() {
        return (Snippet) snippet;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Language(LanguageForm languageForm){
        this.setLanguageId(languageForm.getLanguageId());
        this.setName(languageForm.getName());
    }

    public Language(){};


}
