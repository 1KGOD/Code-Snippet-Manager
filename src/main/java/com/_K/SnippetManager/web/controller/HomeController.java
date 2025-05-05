package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.*;
import com._K.SnippetManager.persistence.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import java.util.ArrayList;
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

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private LanguageDao languageDao;

    @RequestMapping(value = "/home" , method = RequestMethod.GET)
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<com._K.SnippetManager.persistence.entity.User> users = userDao.findByEmailAndIsDeletedFalse(email);

        if(users.isPresent()){
            User user = users.get();
            List<Snippet> snippets = snippetDao.findTop3RatedSnippetsByCount();
            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);

            if(notifications == null){
                notifications =new ArrayList<>();
            }

            List<Comment> comments = commentDao.findBySnippetOrderByCreatedAtDesc(snippets.get(0));

            model.addAttribute("notifications", notifications);
            model.addAttribute("user", user);
            model.addAttribute("snippets",snippets);
            model.addAttribute("comments", comments);
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

    @RequestMapping(value = "/comment/add" , method = RequestMethod.POST)
    public String comment(@RequestParam("snippetId") Long snippetId , @RequestParam("content") String content , @AuthenticationPrincipal UserDetails userDetails , Model model) {
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user = users.get();
            Optional<Snippet> snippet = snippetDao.findBySnippetIdAndIsDeletedFalse(snippetId);
            if(snippet.isPresent()){
                Snippet snippet1 = snippet.get();
                // the user can comment on the snippet
                Comment comment = new Comment();
                comment.setUser(user);
                comment.setSnippet(snippet1);
                comment.setContent(content);
                comment.setCreatedAt(java.time.LocalDateTime.now());
                commentDao.save(comment);
                snippet1.getComments().add(comment);

                // Notification to snippet owner
                String notification = user.getName() + " commented on your snippet: " + snippet1.getTitle();
                Notification notification1 = new Notification();
                notification1.setUser(snippet1.getUser()); // notify the owner
                notification1.setSnippet(snippet1);
                notification1.setMessage(notification);
                notification1.setRead(false);
                notification1.setCreatedAt(java.time.LocalDateTime.now());
                notificationDao.save(notification1);
            }
        }
        return "redirect:/home";
    }

    @RequestMapping(value = "/snippets" , method = RequestMethod.GET)

    public String listSnippets(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "6") int size,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String language,
                               Model model,@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if (users.isPresent()) {
            User user = users.get();
            model.addAttribute("user", user);
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            if (search == null) search = "";
            Page<Snippet> snippetPage = snippetDao.searchPublishedAndNotDeleted(search, language, pageable);
            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);
            if(notifications == null){
                notifications =new ArrayList<>();
            }

            // Avoid error if no snippet is returned
            List<Comment> comments = new ArrayList<>();
            if (!snippetPage.getContent().isEmpty()) {
                comments = commentDao.findBySnippetOrderByCreatedAtDesc(snippetPage.getContent().get(0));
            }
            model.addAttribute("notifications", notifications);
            model.addAttribute("snippetPage", snippetPage);
            model.addAttribute("search", search);
            model.addAttribute("languages", languageDao.findAll());
        }


        return "home/snippetList";

    }

    @RequestMapping(value = "/favorite", method = RequestMethod.GET)
    public String starList(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "language", required = false) String languageName,
                           @RequestParam(value = "name", required = false) String name,
                           Model model) {
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);

        if (users.isPresent()) {
            User user = users.get();

            boolean hasKeyword = keyword != null && !keyword.isEmpty();
            boolean hasLanguage = languageName != null && !languageName.isEmpty();

            // Fetch the Language object from the database if language is provided
            Optional<Language> languageOpt = languageDao.findByName(languageName);

            List<Rating> ratings1 = new ArrayList<>();

            if (hasKeyword) {
                // Fetch all ratings for user
                List<Rating> allRatings = ratingDao.findByUserAndPublishedSnippet(user);

                // Filter manually based on title/username + optional language
                ratings1 = allRatings.stream()
                        .filter(rating -> {
                            Snippet snippet = rating.getSnippet();
                            if (snippet == null || snippet.getUser() == null) return false;

                            boolean matchesTitle = snippet.getTitle().toLowerCase().contains(keyword.toLowerCase());
                            boolean matchesUsername = snippet.getUser().getName().toLowerCase().contains(keyword.toLowerCase());

                            boolean matchesLanguage = true;
                            if (hasLanguage && languageOpt.isPresent()) {
                                matchesLanguage = snippet.getLanguage() != null &&
                                        snippet.getLanguage().getName().equalsIgnoreCase(languageOpt.get().getName());
                            }

                            return (matchesTitle || matchesUsername) && matchesLanguage;
                        })
                        .collect(Collectors.toList());
            } else {
                // No keyword = show all
                ratings1 = ratingDao.findByUserAndPublishedSnippetOrderByCreatedAtDesc(user);
            }


            // Process starred snippets
            List<Snippet> starredSnippets = ratings1.stream()
                    .map(Rating::getSnippet)
                    .filter(s -> s != null && s.getUser() != null) // Ensure Snippet and User are not null
                    .collect(Collectors.toList());

            // Notifications
            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);
            if (notifications == null) {
                notifications = new ArrayList<>();
            }

            // Comments (assuming you need to fetch comments for the first snippet)
            List<Comment> comments = new ArrayList<>();
            if (!starredSnippets.isEmpty()) {
                comments = commentDao.findBySnippetOrderByCreatedAtDesc(starredSnippets.get(0));
            }

            // Adding attributes to the model
            model.addAttribute("starredSnippets", starredSnippets);
            model.addAttribute("keyword", keyword);
            model.addAttribute("language", languageName);
            model.addAttribute("notifications", notifications);
            model.addAttribute("languages", languageDao.findAll());
            model.addAttribute("user", user);
        }
        return "home/starredList";
    }

    @RequestMapping(value = "/{path:.+}", method = RequestMethod.GET)
    public String handleUnknownRoutes(@AuthenticationPrincipal UserDetails userDetails , Model model) {
        // Return the error page if the route doesn't exist
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user  = users.get();
            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);
            if(notifications == null){
                notifications =new ArrayList<>();
            }
            model.addAttribute("notifications", notifications);
            model.addAttribute("user", user);
        }
        return "error/404page"; // Corresponds to error.html in your templates
    }



}
