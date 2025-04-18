package com._K.SnippetManager.persistence.entity;

import com._K.SnippetManager.web.form.UserForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor // This adds the required default constructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userID;

    private String name;

    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<PasswordRestToken> passwordRestTokens;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Favorite> favorite;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Snippet> snippet;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Log> logs;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private String profileImage;

    private Boolean isDeleted = false ;


    public User(UserForm userForm){

        this.setUserID(userForm.getUserId());
        this.setName(userForm.getName());
        this.setEmail(userForm.getEmail());
        this.setPassword(userForm.getPassword());
        this.setPasswordRestTokens(userForm.getPasswordRestTokens());
        this.setRole(userForm.getRole());
        this.setFavorite(userForm.getFavorite());
        this.setSnippet(userForm.getSnippet());
        this.setLogs(userForm.getLogs());
        this.setCreatedAt(userForm.getCreatedAt());
        this.setUpdateAt(userForm.getUpdateAt());
        this.setDeleted(userForm.getDeleted());
    }



    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public List<PasswordRestToken> getPasswordRestTokens() {
        return passwordRestTokens;
    }

    public void setPasswordRestTokens(List<PasswordRestToken> passwordRestTokens) {
        this.passwordRestTokens = passwordRestTokens;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Favorite> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Favorite> favorite) {
        this.favorite = favorite;
    }

    public List<Snippet> getSnippet() {
        return snippet;
    }

    public void setSnippet(List<Snippet> snippet) {
        this.snippet = snippet;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
