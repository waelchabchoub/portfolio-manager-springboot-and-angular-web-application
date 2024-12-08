package com.wael.chabchoub.portfoliomanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
	@GetMapping("/{path:[^\\.]*}")
	String index() {
		return "forward:/index.html";
	}
}
