package com.wael.chabchoub.portfoliomanager.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String senderName, String senderEmail, String subject, String senderMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("waelchabchoub@outlook.com");
        message.setSubject(subject);
        String emailBody = "Sender: " + senderName + ",\n\n" + "Sender Email: " + senderEmail +  ",\n\n" + "Message: " + senderMessage;
        message.setText(emailBody);
        message.setFrom("mariembouchoucha26@gmail.com");

        mailSender.send(message);
    }

}
