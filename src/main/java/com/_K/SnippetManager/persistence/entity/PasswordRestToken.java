package com._K.SnippetManager.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pwd")
@Getter
@Setter
public class PasswordRestToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pwId")
    private Long pwId;

    private String token;

    private LocalDateTime expired;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Long getPwId() {
        return pwId;
    }

    public void setPwId(Long pwId) {
        this.pwId = pwId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpired() {
        return expired;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpired(LocalDateTime expired) {
        this.expired = expired;
    }
}
