package me.timeline.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimelineController {
	
	@PostMapping(path="/signup")
	public UserInfo SignUp(@RequestBody UserInfo userinfo) {
		return userinfo;
	}
	
	@PostMapping(path="/signin")
	public UserInfo SignIn(@RequestBody UserInfo userinfo) {
		return userinfo;
	}
	
}

