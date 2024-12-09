package com.wael.chabchoub.portfoliomanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wael.chabchoub.portfoliomanager.entity.ResponseMessage;
import com.wael.chabchoub.portfoliomanager.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ResponseMessage> sendEmail(
    		@RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message) {
        try {
            emailService.sendSimpleEmail(name, email,subject, message);
            return ResponseEntity.ok(new ResponseMessage("Email sent successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Error while sending email: " + e.getMessage()));
        }
    }
}
