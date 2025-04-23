package com._K.SnippetManager.service.impl;

import com._K.SnippetManager.persistence.dao.PasswordResetTokenDao;
import com._K.SnippetManager.persistence.entity.PasswordRestToken;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.PasswordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordResetTokenDao passwordResetTokenDao;

    public PasswordServiceImpl(PasswordResetTokenDao passwordResetTokenDao) {
        this.passwordResetTokenDao = passwordResetTokenDao;
    }

    @Override
    public void savePasswordToken(PasswordRestToken token) {
        passwordResetTokenDao.save(token);
    }
}
