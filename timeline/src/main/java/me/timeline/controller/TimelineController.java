package me.timeline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import me.timeline.dto.FollowRequestDTO;
import me.timeline.dto.FollowResponseDTO;
import me.timeline.dto.PostCommentRequestDTO;
import me.timeline.dto.PostCommentResponseDTO;
import me.timeline.dto.PostWritingRequestDTO;
import me.timeline.dto.PostWritingResponseDTO;
import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;
import me.timeline.service.TimelineService;

@RestController
public class TimelineController {
	
	@Autowired
	TimelineService timelineService;
	
	@PostMapping(path="/signup")
	public SignUpResponseDTO SignUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		return timelineService.SignUp(signUpRequestDTO);
	}
	
	@PostMapping(path="/signin")
	public SignInResponseDTO SignIn(@RequestBody SignInRequestDTO signInRequestDTO) {
		return timelineService.SignIn(signInRequestDTO);
	}
	
	@PostMapping(path="/postwriting")
	public PostWritingResponseDTO PostWriting(@RequestBody PostWritingRequestDTO postWritingRequestDTO, @RequestHeader (name="Authorization") String jwtToken) {
		return timelineService.PostWriting(postWritingRequestDTO, jwtToken);
	}
	
	@PostMapping(path="/postcomment")
	public PostCommentResponseDTO PostComment(@RequestBody PostCommentRequestDTO postCommentRequestDTO, @RequestHeader (name="Authorization") String jwtToken) {
		return timelineService.PostComment(postCommentRequestDTO, jwtToken);
	}
	
	@PostMapping(path="/follow")
	public FollowResponseDTO Follow(@RequestBody FollowRequestDTO followRequestDTO, @RequestHeader(name="Authorization") String jwtToken) {
		return timelineService.Follow(followRequestDTO, jwtToken);
	}
	
	@PostMapping(path="unfollow")
	public FollowResponseDTO Unfollow(@RequestBody FollowRequestDTO followRequestDTO, @RequestHeader(name="Authorization") String jwtToken) {
		return timelineService.Unfollow(followRequestDTO, jwtToken);
	}
}

