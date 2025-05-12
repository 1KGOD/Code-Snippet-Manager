package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.dao.NotificationDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Notification;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.SnippetService;
import com._K.SnippetManager.web.form.SnippetForm;
import com._K.SnippetManager.web.form.UserForm;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {

    private final SnippetDao snippetDao;
    private final LanguageDao languageDao;
    private final UserDao userDao;
    private final NotificationDao notificationDao;

    public SnippetServiceImpl(SnippetDao snippetDao, LanguageDao languageDao, UserDao userDao, NotificationDao notificationDao) {
        this.snippetDao = snippetDao;
        this.languageDao = languageDao;
        this.userDao = userDao;
        this.notificationDao = notificationDao;
    }

    @Override
    public void saveSnippet(SnippetForm snippetForm, User user) {
        Snippet snippet = new Snippet(snippetForm);
        // snippetform-> language -> snippet entity -> db
        //fetch data via db by finding id of language -> get data from db -> set that data to snippet entity
        Language language = languageDao.findById(snippetForm.getLanguage().getLanguageId()).orElseThrow( ()-> new RuntimeException("language not Found"));
        snippet.setUser(user);
        snippet.setLanguage(language);
        snippet.setCreatedAt(LocalDateTime.now());
        snippetDao.save(snippet);
    }

    @Override
    public Page<SnippetForm> getAllSnippets(Long userId, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Snippet> snippets;

        if (keyword != null && !keyword.isEmpty()) {
            snippets = snippetDao.findUserSnippetsByKeyword(userId ,keyword, pageable);
        } else {
            snippets = snippetDao.findByUserUserIDAndIsDeletedFalse(userId,pageable);
        }

        List<SnippetForm> snippetForms = snippets.getContent().stream()
                .map(SnippetForm::new)
                .toList();

        return new PageImpl<>(snippetForms, pageable, snippets.getTotalElements());
    }

    @Override
    public void editSnippet(SnippetForm snippetForm, User user) {
        Optional<Snippet> snippet = snippetDao.findBySnippetIdAndUserAndIsDeletedFalse(snippetForm.getSnippetId(),user);
        if(snippet.isPresent()){
            Snippet snippet1 = snippet.get();
            snippet1.setTitle(snippetForm.getTitle());
            snippet1.setCode(snippetForm.getCode());
            snippet1.setLanguage(snippetForm.getLanguage());
            snippet1.setUpdateAt(LocalDateTime.now());
            snippet1.setUser(user);
            snippetDao.save(snippet1);
        }
    }

    @Override
    public void deleteSnippet(Long snippetId, User user) {
        Optional<Snippet> snippet = snippetDao.findBySnippetIdAndUserAndIsDeletedFalse(snippetId, user);
        if(snippet.isPresent()){
            Snippet snippet1 = snippet.get();
            if(!Boolean.TRUE.equals(snippet1.getDeleted())){
                snippet1.setDeleted(true);
                snippet1.setUpdateAt(LocalDateTime.now());
                snippetDao.save(snippet1);
            }
        }
    }

    @Override
    public List<Snippet> publishedSnippet(User user) {
        List<Snippet> snippets  = snippetDao.findByUserAndUserIsDeletedFalseAndIsDeletedFalseAndIsPublishedTrue(user);
        return snippets;
    }

    @Override
    public List<Snippet> privatedSnippet(User user) {
        List<Snippet> snippets =  snippetDao.findByUserAndUserIsDeletedFalseAndIsDeletedFalseAndIsPublishedFalse(user);
        return  snippets;
    }

    @Override
    public List<Snippet> sharedSnippets(Long userId) {
        List<Snippet> snippets = snippetDao.findBySharedWithUsersUserIDAndIsDeletedFalseAndIsPublishedFalse(userId);
        return snippets;
    }


    @Override
    public void shareSnippetWithUser(Long snippetId, Long userId) {
        Optional<User> userOpt = userDao.findById(userId);
        Optional<Snippet> snippetOpt = snippetDao.findById(snippetId);
        if(snippetOpt.isPresent() && userOpt.isPresent()){
            Snippet snippet = snippetOpt.get();
            User user  = userOpt.get();
            snippet.getSharedWithUsers().add(user);
            snippetDao.save(snippet);
            System.out.println("Sharing with users: ");
            snippet.getSharedWithUsers().forEach(u -> System.out.println(" - " + u.getUserID()));

            Notification notification = new Notification();
            String message = snippet.getUser().getName()+"'s Name has shared a code snippet with you!";
            notification.setSnippet(snippet);
            notification.setRead(false);
            notification.setUser(user);
            notification.setMessage(message);
            notificationDao.save(notification);
        }
        else {
            throw new RuntimeException("Snippet or User not found");
        }
    }


}
