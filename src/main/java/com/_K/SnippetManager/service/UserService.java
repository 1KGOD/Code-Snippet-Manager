package com._K.SnippetManager.service;

import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.web.form.UserForm;
import org.springframework.data.domain.Page;

public interface UserService {
    void saveUser(UserForm userForm);
    void updateUser(User user );
    Page<UserForm> getAllUsers(int page , int size , String keyword);
    void editUser(UserForm userForm);
    void deletedUser(UserForm userForm);
}
