package me.timeline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.timeline.dto.SignatureInfoDTO;
import me.timeline.service.TimelineServiceImpl;

@RestController
public class TimelineController {
	@Autowired
	TimelineServiceImpl TimelineService;
	
	@PostMapping(path="/signup")
	public SignatureInfoDTO SignUp(@RequestBody SignatureInfo userinfo) {
		SignatureInfoDTO success = TimelineService.signUpSucceed(userinfo.getEmail(), userinfo.getPassword());
		return success;
	}
	
	@PostMapping(path="/signin")
	public SignatureInfo SignIn(@RequestBody SignatureInfo userinfo) {
		return userinfo;
	}
	
}

