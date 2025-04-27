package com._K.SnippetManager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendResetMail(String to, String resetLink) {
        // Implement the logic to send an email using JavaMailSender
        // This is a placeholder for the actual implementation

        // You can use JavaMailSender to send the email here
        // For example:
         SimpleMailMessage message = new SimpleMailMessage();
         message.setTo(to);
         message.setSubject("Reset Your Password");
            message.setText("Hi there,\n\n" +
                    "We received a request to reset your password.\n\n" +
                    "Click the link below to reset your password:\n" +
                    resetLink + "\n\n" +
                    "If you didn’t request this, please ignore this email.\n\n" +
                    "Thanks,\nThe [</>Code Snippet Manager] Team");
            message.setFrom("winesathan133@gmail.com");
            javaMailSender.send(message);
    }

}
