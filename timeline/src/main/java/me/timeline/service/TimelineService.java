package me.timeline.service;

import java.awt.List;

import org.springframework.stereotype.Service;

import me.timeline.dto.SignatureInfoDTO;

public interface TimelineService  {
	public boolean signUpSucceed(String email, String password);
}
