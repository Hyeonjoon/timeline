package me.timeline.service;

import me.timeline.dto.SignInRequestDTO;
import me.timeline.dto.SignInResponseDTO;
import me.timeline.dto.SignUpRequestDTO;
import me.timeline.dto.SignUpResponseDTO;

public interface TimelineService  {
	public SignUpResponseDTO SignUp(SignUpRequestDTO signUpRequestDTO);
	public SignInResponseDTO SignIn(SignInRequestDTO signInRequestDTO);
}
