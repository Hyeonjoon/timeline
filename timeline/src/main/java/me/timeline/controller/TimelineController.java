package me.timeline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import me.timeline.dto.PostRequestDTO;
import me.timeline.dto.PostResponseDTO;
import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;
import me.timeline.service.TimelineServiceImpl;

@RestController
public class TimelineController {
	
	@Autowired
	TimelineServiceImpl TimelineService;
	
	@PostMapping(path="/signup")
	public SignUpResponseDTO SignUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		return TimelineService.SignUp(signUpRequestDTO);
	}
	
	@PostMapping(path="/signin")
	public SignInResponseDTO SignIn(@RequestBody SignInRequestDTO signInRequestDTO) {
		return TimelineService.SignIn(signInRequestDTO);
	}
	
	@PostMapping(path="/postwriting")
	public PostResponseDTO PostWriting(@RequestBody PostRequestDTO postRequestDTO, @RequestHeader (name="Authorization") String jwtToken) {
		return TimelineService.PostWriting(postRequestDTO, jwtToken);
	}
}

