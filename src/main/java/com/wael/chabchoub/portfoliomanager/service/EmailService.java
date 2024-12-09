package com.wael.chabchoub.portfoliomanager.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.wael.chabchoub.portfoliomanager.security.config.PortfolioManagerProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final PortfolioManagerProperties portfolioManagerProperties;

    public void sendSimpleEmail(String senderName, String senderEmail, String subject, String senderMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(portfolioManagerProperties.getEmail().getTo());
        message.setSubject(subject);
        String emailBody = "Sender: " + senderName + ",\n\n" + "Sender Email: " + senderEmail +  ",\n\n" + "Message: " + senderMessage;
        message.setText(emailBody);
        message.setFrom(portfolioManagerProperties.getEmail().getFrom());

        mailSender.send(message);
    }

}
