package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.NotificationDao;
import com._K.SnippetManager.persistence.dao.RatingDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Notification;
import com._K.SnippetManager.persistence.entity.Rating;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private  UserDao userDao;

    @Autowired
    private  SnippetDao snippetDao;

    @Autowired
    private  RatingDao ratingDao;

    @Autowired
    private NotificationDao notificationDao;

    @RequestMapping(value = "/home" , method = RequestMethod.GET)
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<com._K.SnippetManager.persistence.entity.User> users = userDao.findByEmailAndIsDeletedFalse(email);

        if(users.isPresent()){
            User user = users.get();
            List<Snippet> snippets = snippetDao.findByIsDeletedFalseAndIsPublishedTrue().stream()
                    .filter(s -> s.getUser() != null)
                    .collect(Collectors.toList());
            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);
            model.addAttribute("user", user);
            model.addAttribute("snippets",snippets);
            model.addAttribute("notifications", notifications);
        }
        return "home/home";
    }

    @RequestMapping(value = "/rating/star" , method = RequestMethod.POST)
    public String stars(@RequestParam("snippetId") Long snippetId , @AuthenticationPrincipal UserDetails userDetails , Model model) {
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user = users.get();
            Optional<Snippet> snippet = snippetDao.findBySnippetIdAndIsDeletedFalse(snippetId);
            if(snippet.isPresent()){
                Snippet snippet1 = snippet.get();

                // Check if the user has already rated this snippet
                if(!ratingDao.existsByUserAndSnippet(user,snippet1)){
                    Rating rating = new Rating();
                    rating.setUser(user);
                    rating.setSnippet(snippet1);
                    rating.setScore(1); // Assuming a score of 1 for the star rating
                    ratingDao.save(rating);

                    // Notification to snippet owner
                    String notification = user.getName() + " rated your snippet: " + snippet1.getTitle();
                    Notification notification1 = new Notification();
                    notification1.setUser(snippet1.getUser()); // notify the owner
                    notification1.setSnippet(snippet1);
                    notification1.setMessage(notification);
                    notification1.setRead(false);
                    notification1.setCreatedAt(java.time.LocalDateTime.now());
                    notificationDao.save(notification1);
                }


            }
        }
        return "redirect:/home";
    }


    @RequestMapping(value = "/notifications/mark-as-read" , method = RequestMethod.POST)
    public ResponseEntity<String> markNotificationsAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Optional<User> optionalUser = userDao.findByEmailAndIsDeletedFalse(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Notification> unread = notificationDao.findByUserAndIsReadFalse(user);

            for (Notification n : unread) {
                n.setRead(true);
            }

            notificationDao.saveAll(unread);
            return ResponseEntity.ok("Notifications marked as read");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }


}
