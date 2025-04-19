package com._K.SnippetManager.persistence.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToMany(mappedBy = "language")
    @JsonIgnore
    private List<Snippet> snippet;



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


}
