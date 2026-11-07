package com.wael.chabchoub.portfoliomanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wael.chabchoub.portfoliomanager.security.config.SecurityConfig;
import com.wael.chabchoub.portfoliomanager.security.jwt.JwtUtils;
import com.wael.chabchoub.portfoliomanager.service.EmailService;

/** Keep this class at 5 requests or fewer: the rate limiter allows 5 sends per IP per hour. */
@WebMvcTest(EmailController.class)
@Import(SecurityConfig.class)
class EmailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private EmailService emailService;

	@MockitoBean
	private JwtUtils jwtUtils;

	private String body(String name, String email, String subject, String message, String website) {
		return """
				{"name":"%s","email":"%s","subject":"%s","message":"%s","website":"%s"}"""
				.formatted(name, email, subject, message, website);
	}

	@Test
	void validMessageIsSent() throws Exception {
		mockMvc.perform(post("/api/emails/send").contentType(MediaType.APPLICATION_JSON)
				.content(body("Ada", "ada@example.com", "Hello", "Nice site", ""))).andExpect(status().isOk());
		verify(emailService).sendSimpleEmail("Ada", "ada@example.com", "Hello", "Nice site");
	}

	@Test
	void filledHoneypotLooksLikeSuccessButSendsNothing() throws Exception {
		mockMvc.perform(post("/api/emails/send").contentType(MediaType.APPLICATION_JSON)
				.content(body("Bot", "bot@example.com", "Buy", "Cheap", "http://spam.example"))).andExpect(status().isOk());
		verify(emailService, never()).sendSimpleEmail(any(), any(), any(), any());
	}

	@Test
	void invalidEmailIsRejected() throws Exception {
		mockMvc.perform(post("/api/emails/send").contentType(MediaType.APPLICATION_JSON)
				.content(body("Ada", "not-an-email", "Hello", "Nice site", ""))).andExpect(status().isBadRequest());
		verify(emailService, never()).sendSimpleEmail(any(), any(), any(), any());
	}

	@Test
	void lineBreakInSubjectIsRejected() throws Exception {
		mockMvc.perform(post("/api/emails/send").contentType(MediaType.APPLICATION_JSON)
				.content(body("Ada", "ada@example.com", "Hi\\r\\nBcc: victim@example.com", "Nice site", "")))
				.andExpect(status().isBadRequest());
		verify(emailService, never()).sendSimpleEmail(any(), any(), any(), any());
	}
}
