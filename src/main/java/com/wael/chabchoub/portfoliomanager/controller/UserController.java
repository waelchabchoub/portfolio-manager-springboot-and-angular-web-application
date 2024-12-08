package com.wael.chabchoub.portfoliomanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wael.chabchoub.portfoliomanager.entity.User;
import com.wael.chabchoub.portfoliomanager.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
public class UserController {
    private final UserService userService;
    
    @GetMapping("/get")
    public User getUserInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/save")
    public User saveUserInfo(@RequestBody User user) {
    	if (user.getId() == null) {
    	    user.setId(1L);
    	}
    	
        return userService.saveOrUpdateUserInfo(user);
    }
}
