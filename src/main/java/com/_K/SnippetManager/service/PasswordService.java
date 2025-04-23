package com._K.SnippetManager.service;

import com._K.SnippetManager.persistence.entity.PasswordRestToken;

import java.time.LocalDateTime;

public interface PasswordService {

    void savePasswordToken(PasswordRestToken token);
}
