package me.timeline.service;

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

public interface TimelineService  {
	public SignUpResponseDTO SignUp(SignUpRequestDTO signUpRequestDTO);
	public SignInResponseDTO SignIn(SignInRequestDTO signInRequestDTO);
	public PostWritingResponseDTO PostWriting(PostWritingRequestDTO postWritingRequestDTO, String jwtToken);
	public PostCommentResponseDTO PostComment(PostCommentRequestDTO postCommentRequestDTO, String jwtToken);
	public FollowResponseDTO Follow(FollowRequestDTO followRequestDTO, String jwtToken);
	public FollowResponseDTO Unfollow(FollowRequestDTO followRequestDTO, String jwtToken);
}
