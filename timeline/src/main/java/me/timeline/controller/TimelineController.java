package me.timeline.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimelineController {
	@GetMapping(path="/login")
	public String Login() {
		return "Login";
	}
}
