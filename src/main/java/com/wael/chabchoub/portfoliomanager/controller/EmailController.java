package com.wael.chabchoub.portfoliomanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wael.chabchoub.portfoliomanager.entity.ContactRequest;
import com.wael.chabchoub.portfoliomanager.entity.ResponseMessage;
import com.wael.chabchoub.portfoliomanager.service.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
	private static final Logger log = LoggerFactory.getLogger(EmailController.class);

	private final EmailService emailService;

	@PostMapping("/send")
	public ResponseEntity<ResponseMessage> sendEmail(@Valid @RequestBody ContactRequest request) {
		// Honeypot filled in: it is a bot. Answer like a success so it learns nothing, but send nothing.
		if (request.website() != null && !request.website().isBlank()) {
			return ResponseEntity.ok(new ResponseMessage("Email sent successfully."));
		}
		try {
			emailService.sendSimpleEmail(request.name(), request.email(), request.subject(), request.message());
			return ResponseEntity.ok(new ResponseMessage("Email sent successfully."));
		} catch (Exception e) {
			log.error("Could not send contact email", e);
			return ResponseEntity.status(500).body(new ResponseMessage("Error while sending email."));
		}
	}
}
