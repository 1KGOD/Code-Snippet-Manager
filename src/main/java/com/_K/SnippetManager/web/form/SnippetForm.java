package com._K.SnippetManager.web.form;

import com._K.SnippetManager.persistence.entity.Rating;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class SnippetForm {


    private Long snippetId;

    @Size(min = 2, message = "title must be at least 2 characters long")
    private String title;

    @NotNull
    private Language language;

    private User user;

    private Rating rating;

    private String code;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted = false ;


    public SnippetForm(Snippet snippet){
        this.setSnippetId(snippet.getSnippetId());
        this.setTitle(snippet.getTitle());
        this.setLanguage(snippet.getLanguage());
        this.setUser(snippet.getUser());
        this.setCreatedAt(snippet.getCreatedAt());
        this.setUpdatedAt(snippet.getUpdateAt());
        this.setCode(snippet.getCode());
    }

    public SnippetForm(){}


    public Rating getFavorite() {
        return rating;
    }

    public void setFavorite(Rating rating) {
        this.rating = rating;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
