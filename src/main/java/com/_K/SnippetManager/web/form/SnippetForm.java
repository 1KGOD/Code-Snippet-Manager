package com._K.SnippetManager.web.form;

import com._K.SnippetManager.persistence.entity.Favorite;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Snippet;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class SnippetForm {


    private Long snippetId;

    @Size(min = 2, message = "title must be at least 2 characters long")
    private String title;

    @NotNull
    private Language language;

    private Favorite favorite;

    private String code;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted = false ;


    public SnippetForm(Snippet snippet){
        this.setSnippetId(snippet.getSnippetId());
        this.setTitle(snippet.getTitle());
        this.setLanguage(language);
        this.setCreatedAt(snippet.getCreatedAt());
        this.setUpdatedAt(snippet.getUpdateAt());
        this.setCode(snippet.getCode());
    }

    public SnippetForm(){}


    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSnippetId() {
        return snippetId;
    }

    public void setSnippetId(Long snippetId) {
        this.snippetId = snippetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
