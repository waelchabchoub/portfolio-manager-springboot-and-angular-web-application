package com.wael.chabchoub.portfoliomanager.controller;

import org.springframework.http.ResponseEntity;
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
}
