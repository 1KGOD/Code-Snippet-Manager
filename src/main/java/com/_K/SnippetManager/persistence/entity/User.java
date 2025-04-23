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
    private  List<PasswordRestToken> passwordRestToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Rating> rating;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Snippet> snippet;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private String profileImage;

    private Boolean isDeleted = false ;


    public User(UserForm userForm){
        this.setUserID(userForm.getUserId());
        this.setName(userForm.getName());
        this.setEmail(userForm.getEmail());
        this.setPassword(userForm.getPassword());
        this.setRole(userForm.getRole());
        this.setNotifications(userForm.getNotifications());
        this.setRating(userForm.getRating());
        this.setSnippet(userForm.getSnippet());
        this.setCreatedAt(userForm.getCreatedAt());
        this.setUpdateAt(userForm.getUpdateAt());
        this.setDeleted(userForm.getDeleted());
    }

    public User(User user){
        this.setUserID(user.getUserID());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setRole(user.getRole());
        this.setNotifications(user.getNotifications());
        this.setRating(user.getRating());
        this.setSnippet(user.getSnippet());
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdateAt(user.getUpdateAt());
        this.setDeleted(user.getDeleted());
    }


    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public List<PasswordRestToken> getPasswordRestToken() {
        return passwordRestToken;
    }

    public void setPasswordRestToken(List<PasswordRestToken> passwordRestToken) {
        this.passwordRestToken = passwordRestToken;
    }

    public List<Rating> getRating() {
        return rating;
    }

    public void setRating(List<Rating> rating) {
        this.rating = rating;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
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

    public List<Rating> getFavorite() {
        return rating;
    }

    public void setFavorite(List<Rating> rating) {
        this.rating = rating;
    }

    public List<Snippet> getSnippet() {
        return snippet;
    }

    public void setSnippet(List<Snippet> snippet) {
        this.snippet = snippet;
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
