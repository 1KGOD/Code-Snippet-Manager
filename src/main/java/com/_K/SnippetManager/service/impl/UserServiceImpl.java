package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.RoleDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Role;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.UserService;
import com._K.SnippetManager.web.form.UserForm;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final RoleDao roleDao;

    private final SnippetDao snippetDao;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao, SnippetDao snippetDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
        this.snippetDao = snippetDao;
    }

    @Override
    public void saveUser(UserForm userForm) {
        User user = new User(userForm);
        Role role = roleDao.findByRoleName("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);
        userDao.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userDao.save(user);
    }

    @Override
    public Page<UserForm> getAllUsers(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<User> users;

        if(keyword != null && !keyword.isEmpty()){
            users = userDao.searchUsersByKeyword(keyword,pageable);
            System.out.println("User Name : "+users.getContent().getFirst().getName());
        }else {
            users = userDao.findByIsDeletedFalse(pageable);
        }
        List<UserForm> userForms = users.getContent().stream().map(UserForm::new).toList();

        return new PageImpl<>(userForms,pageable,users.getTotalElements());
    }

    @Override
    public void editUser(UserForm userForm) {
        Optional<User> user = userDao.findById(userForm.getUserId());
        if(user.isPresent()){
            User user1 = user.get();
            user1.setUserID(userForm.getUserId());
            user1.setName(userForm.getName());
            user1.setEmail(userForm.getEmail());
            userDao.save(user1);
        }
    }

    @Override
    public void deletedUser(UserForm userForm) {
        Optional<User> users = userDao.findById(userForm.getUserId());
        if(users.isPresent()){
            User user = users.get();
            if(!Boolean.TRUE.equals(user.getDeleted())){
                user.setDeleted(true);
                user.setUpdateAt(LocalDateTime.now());
            List<Snippet> userSnippets = snippetDao.findByUserAndIsDeletedFalse(user);
            for(Snippet snippet : userSnippets){
                snippet.setDeleted(true);
                snippet.setUpdateAt(LocalDateTime.now());
            }
            snippetDao.saveAll(userSnippets);
            userDao.save(user);
            }
        }
    }


}
