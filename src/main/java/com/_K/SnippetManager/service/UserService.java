package com._K.SnippetManager.service;

import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.web.form.UserForm;

public interface UserService {
    void saveUser(UserForm userForm);
    void updateUser(User user );

}
