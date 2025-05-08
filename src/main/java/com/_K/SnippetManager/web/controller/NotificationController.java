package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.NotificationDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Notification;
import com._K.SnippetManager.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserDao userDao;

    @PostMapping("/unread/mark-all-read")
    public void markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userDao.findByEmailAndIsDeletedFalse(email).orElseThrow();
        if (user != null) {
            List<Notification> unread = notificationDao.findByUserAndIsReadFalse(user);
            unread.forEach(n -> n.setRead(true));
            notificationDao.saveAll(unread);
        }
    }

}
