package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.*;
import com._K.SnippetManager.persistence.entity.*;
import com._K.SnippetManager.service.SnippetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
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

    @Autowired
    private SnippetService snippetService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        List<Notification> notifications = new ArrayList<>();
        List<Snippet> snippets = snippetDao.findTop3RatedSnippetsByCount();
        List<Snippet> top3 = snippets.size() > 3 ? snippets.subList(0, 3) : snippets;
        List<Comment> comments = new ArrayList<>();

        // If user is authenticated, load their data
        if (userDetails != null) {
            Optional<User> userOptional = userDao.findByEmailAndIsDeletedFalse(userDetails.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);
                notifications = notificationDao.findByUserAndIsReadFalse(user);
                if (!snippets.isEmpty()) {
                    comments = commentDao.findBySnippetOrderByCreatedAtDesc(snippets.get(0));
                }
            }
        }

         // Always has a value
        model.addAttribute("notifications", notifications);
        model.addAttribute("snippets", top3);
        model.addAttribute("comments", comments);

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
            model.addAttribute("snippetPage", snippetPage.getContent());
            model.addAttribute("search", search);
            model.addAttribute("languages", languageDao.findAll());
        }


        return "home/snippetList";

    }

    @RequestMapping("/snippets/**")
    public String handleInvalidSnippetPath() {
        return "error/404page";
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

            Optional<Language> languageOpt = languageDao.findByName(languageName);

            List<Rating> ratings1 = new ArrayList<>();

            List<Rating> allRatings = ratingDao.findByUserAndPublishedSnippet(user);

            if (hasKeyword || hasLanguage) {
                ratings1 = allRatings.stream()
                        .filter(rating -> {
                            Snippet snippet = rating.getSnippet();
                            if (snippet == null || snippet.getUser() == null) return false;

                            boolean matchesTitle = !hasKeyword || snippet.getTitle().toLowerCase().contains(keyword.toLowerCase());
                            boolean matchesUsername = !hasKeyword || snippet.getUser().getName().toLowerCase().contains(keyword.toLowerCase());

                            boolean matchesLanguage = true;
                            if (hasLanguage && languageOpt.isPresent()) {
                                matchesLanguage = snippet.getLanguage() != null &&
                                        snippet.getLanguage().getName().equalsIgnoreCase(languageOpt.get().getName());
                            }

                            return (matchesTitle || matchesUsername) && matchesLanguage;
                        })
                        .collect(Collectors.toList());
            } else {
                ratings1 = ratingDao.findByUserAndPublishedSnippetOrderByCreatedAtDesc(user);
            }

            List<Snippet> starredSnippets = ratings1.stream()
                    .map(Rating::getSnippet)
                    .filter(s -> s != null && s.getUser() != null)
                    .collect(Collectors.toList());

            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);
            if (notifications == null) {
                notifications = new ArrayList<>();
            }

            List<Comment> comments = new ArrayList<>();
            if (!starredSnippets.isEmpty()) {
                comments = commentDao.findBySnippetOrderByCreatedAtDesc(starredSnippets.get(0));
            }

            model.addAttribute("starredSnippets", starredSnippets);
            model.addAttribute("keyword", keyword);
            model.addAttribute("language", languageName);
            model.addAttribute("notifications", notifications);
            model.addAttribute("languages", languageDao.findAll());
            model.addAttribute("user", user);
        }
        return "home/starredList";
    }

    @RequestMapping("/favorite/**")
    public String handleInvalidFavoritePath() {
        return "error/404page";
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

    @RequestMapping(value = "/myLibrary" , method = RequestMethod.GET)
    public String myLibrary(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "sharedUserId" , required = false)Long sharedUserId , Model model ){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user = users.get();
            List<Notification> notifications = notificationDao.findByUserAndIsReadFalse(user);
            List<Snippet> publishedSnippets = snippetService.publishedSnippet(user);
            List<Snippet> privatedSnippets =  snippetService.privatedSnippet(user);
            List<Snippet> sharedSnippets = snippetService.sharedSnippets(user.getUserID());


            model.addAttribute("user",user);
            model.addAttribute("notifications",notifications);
            model.addAttribute("publishedSnippets",publishedSnippets);
            model.addAttribute("privatedSnippets",privatedSnippets);
            model.addAttribute("sharedSnippets",sharedSnippets);
        }
        return "home/MyLibrary";
    }

    @RequestMapping("/myLibrary/**")
    public String handleInvalidMyLibrryPath() {
        return "error/404page";
    }


}
