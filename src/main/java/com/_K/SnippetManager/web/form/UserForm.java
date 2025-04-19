package com._K.SnippetManager.web.form;

import com._K.SnippetManager.persistence.entity.*;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserForm {

    @Valid
    private Long userId;

    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must start with a letter and be valid")
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 3 , message = "Password must be at least 3 characters long")
    private String password;

    private List<PasswordRestToken> passwordRestTokens;

    private Role role;

    private List<Favorite> favorite;

    private List<Snippet> snippet;

    private List<Log> logs;

    private String profileImage;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private Boolean isDeleted=false;

    public UserForm(User user){
        this.setUserId(user.getUserID());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setPasswordRestTokens(user.getPasswordRestTokens());
        this.setRole(user.getRole());
        this.setFavorite(user.getFavorite());
        this.setLogs(user.getLogs());
        this.setProfileImage(user.getProfileImage());
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdateAt(user.getUpdateAt());
        this.setDeleted(user.getDeleted());
    }

    public UserForm(){}

    public List<PasswordRestToken> getPasswordRestTokens() {
        return passwordRestTokens;
    }

    public void setPasswordRestTokens(List<PasswordRestToken> passwordRestTokens) {
        this.passwordRestTokens = passwordRestTokens;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public @Valid Long getUserId() {
        return userId;
    }

    public void setUserId(@Valid Long userId) {
        this.userId = userId;
    }

    public @Size(min = 2, message = "Name must be at least 2 characters long") String getName() {
        return name;
    }

    public void setName(@Size(min = 2, message = "Name must be at least 2 characters long") String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public @Size(min = 3, message = "Password must be at least 3 characters long") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 3, message = "Password must be at least 3 characters long") String password) {
        this.password = password;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
