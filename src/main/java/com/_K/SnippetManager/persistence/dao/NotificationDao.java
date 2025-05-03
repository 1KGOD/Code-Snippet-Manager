package com._K.SnippetManager.persistence.dao;

import com._K.SnippetManager.persistence.entity.Notification;
import com._K.SnippetManager.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationDao extends JpaRepository<Notification,Long> {
    List<Notification> findByUserAndIsReadFalse(User user);
}
