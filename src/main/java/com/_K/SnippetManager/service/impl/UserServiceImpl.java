package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.RoleDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Role;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.UserService;
import com._K.SnippetManager.web.form.UserForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
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
        System.out.println("Updating user: " + user.getEmail());
        userDao.save(user);
    }
}
