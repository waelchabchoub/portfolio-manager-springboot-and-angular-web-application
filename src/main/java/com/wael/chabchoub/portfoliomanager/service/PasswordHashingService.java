package com.wael.chabchoub.portfoliomanager.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHashingService {
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public PasswordHashingService() {
		this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}

	public String hashPassword(String password) {
		return bCryptPasswordEncoder.encode(password); 
	}

	public boolean matchPasswords(String rawPassword, String hashedPassword) {
		return bCryptPasswordEncoder.matches(rawPassword, hashedPassword);
	}
}
