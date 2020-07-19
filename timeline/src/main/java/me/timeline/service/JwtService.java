package me.timeline.service;

import me.timeline.dto.JwtInputDTO;

public interface JwtService {
	public String create(JwtInputDTO jwtInputDTO);
	public boolean isUsable(String jwt);
}
