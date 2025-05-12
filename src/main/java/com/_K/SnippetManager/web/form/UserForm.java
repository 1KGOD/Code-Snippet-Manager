package com._K.SnippetManager.web.form;

import com._K.SnippetManager.persistence.entity.*;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserForm {

    @Valid
    private Long userId;

//    @NotNull
//    private List<Long> userIds;

    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotNull(message = "Email cannot be null")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must start with a letter and be valid")
    @Column(unique = true)
    private String email;

    @Pattern(
            regexp = "^(?=.{5,15}$)(?=[A-Za-z])[A-Za-z\\d]*[A-Z]+[A-Za-z\\d]*[a-z]+[A-Za-z\\d]*\\d+[A-Za-z\\d]*$"
            ,
            message = "Password must start with a letter, be 5-15 characters, with at least one uppercase, one lowercase, and one number"
    )
    private String password;


    private String token;

    private Role role;

    private List<Rating> rating;

    private List<Snippet> snippet;

    private List<Notification> notifications;

    private MultipartFile profileImage;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private Boolean isDeleted=false;

    public UserForm(User user){
        this.setUserId(user.getUserID());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        if (user.getPasswordRestToken() != null && !user.getPasswordRestToken().isEmpty()) {
            this.setToken(user.getPasswordRestToken().get(0).getToken());
        } else {
            this.setToken(null); // or default to "N/A"
        }
        this.setRole(user.getRole());
        this.setRating(user.getRating());
        this.setNotifications(user.getNotifications());
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdateAt(user.getUpdateAt());
        // Safely handle the isDeleted property in case it's null
        this.setDeleted(user.getDeleted() != null ? user.getDeleted() : false);
    }

//    public List<Long> getUserIds() {
//        return userIds;
//    }
//
//    public void setUserIds(List<Long> userIds) {
//        this.userIds = userIds;
//    }

    public UserForm(){}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
