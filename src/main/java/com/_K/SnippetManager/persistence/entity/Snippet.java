package com._K.SnippetManager.persistence.entity;

import com._K.SnippetManager.web.form.SnippetForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "snippet")
@Getter
@Setter
public class Snippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snippetId")
    private Long snippetId;

    private String title;

    @ManyToOne
    @JoinColumn(name = "userId" )
    private User user;

    @ManyToOne
    @JoinColumn(name = "languageId" )
    private Language language;

    @Column(columnDefinition = "TEXT")
    private String code;

    @OneToMany(mappedBy = "snippet")
    @JsonIgnore
    private List<Favorite> favorites;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private Boolean isDeleted = false ;


    public Snippet(SnippetForm snippetForm){
        this.setSnippetId(snippetForm.getSnippetId());
        this.setTitle(snippetForm.getTitle());
        this.setLanguage(language);
        this.setCode(snippetForm.getCode());
        this.setCreatedAt(snippetForm.getCreatedAt());
        this.setUpdateAt(snippetForm.getUpdatedAt());
        this.setDeleted(snippetForm.getDeleted());
    }

    public Snippet(){}


    public Long getSnippetId() {
        return snippetId;
    }

    public void setSnippetId(Long snippetId) {
        this.snippetId = snippetId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
