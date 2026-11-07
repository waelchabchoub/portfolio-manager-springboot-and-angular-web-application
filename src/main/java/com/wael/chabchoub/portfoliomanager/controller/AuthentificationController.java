package com.wael.chabchoub.portfoliomanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.wael.chabchoub.portfoliomanager.entity.Admin;
import com.wael.chabchoub.portfoliomanager.repository.AdminRepository;
import com.wael.chabchoub.portfoliomanager.security.jwt.JwtUtils;
import com.wael.chabchoub.portfoliomanager.service.PasswordHashingService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthentificationController {
	private final JwtUtils jwtUtil;
	private final AdminRepository adminRepository;
	private final PasswordHashingService passwordHashingService;
	private static final int MIN_PASSWORD_LENGTH = 8;

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
		String login = credentials.get("username");
		String password = credentials.get("password");
		
		Admin admin = adminRepository.findByLogin(login);

		if (admin != null && passwordHashingService.matchPasswords(password, admin.getPassword())) {
			String token = jwtUtil.generateToken(login);
			Map<String, String> response = new HashMap<>();
			response.put("token", token);
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(401).body(null);
		}
	}

	@PostMapping("/change-password")
	public ResponseEntity<Map<String, String>> changePassword(Authentication authentication,
			@RequestBody Map<String, String> body) {
		String currentPassword = body.get("currentPassword");
		String newPassword = body.get("newPassword");

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(401).build();
		}
		if (currentPassword == null || newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH
				|| newPassword.equals(currentPassword)) {
			return ResponseEntity.badRequest().body(Map.of("message", "INVALID_NEW_PASSWORD"));
		}

		Admin admin = adminRepository.findByLogin(authentication.getName());
		if (admin == null || !passwordHashingService.matchPasswords(currentPassword, admin.getPassword())) {
			return ResponseEntity.status(403).body(Map.of("message", "WRONG_CURRENT_PASSWORD"));
		}

		admin.setPassword(passwordHashingService.hashPassword(newPassword));
		adminRepository.save(admin);
		return ResponseEntity.ok(Map.of("message", "PASSWORD_CHANGED"));
	}
}
