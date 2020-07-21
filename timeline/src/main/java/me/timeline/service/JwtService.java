package me.timeline.service;

import me.timeline.dto.JwtInputDTO;

public interface JwtService {
	public String JwtCreate(JwtInputDTO jwtInputDTO);
	public boolean JwtIsUsable(String jwt);
	public int JwtGetUserId(String jwt);
}
