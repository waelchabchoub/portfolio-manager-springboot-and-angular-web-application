package com.wael.chabchoub.portfoliomanager.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wael.chabchoub.portfoliomanager.entity.User;
import com.wael.chabchoub.portfoliomanager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public User getUserInfo() {
        return userRepository.findAll().stream().findFirst().orElse(null);
    }
	
	public User saveOrUpdateUserInfo(User user) {
		Optional<User> existingUser = userRepository.findById(user.getId());
		
        if (existingUser.isPresent()) {
        	 User existing = existingUser.get();
        	 existing.setEmail(user.getEmail());
        	 existing.setAge(user.getAge());
        	 existing.setTitle(user.getTitle());
        	 existing.setBirthday(user.getBirthday());
        	 existing.setWebsite(user.getWebsite());
        	 existing.setCity(user.getCity());
        	 existing.setDegree(user.getDegree());
        	 existing.setAvailableForFreeLance(user.isAvailableForFreeLance());
        	 
        	 return userRepository.save(existing);
        } else {
        	 return userRepository.save(user);
        }
    }
}
