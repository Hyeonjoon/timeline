package me.timeline.dao;

import java.awt.List;

import me.timeline.dto.SignatureInfoDTO;

public interface SignatureInfoDAO {
	public SignatureInfoDTO signUp(String email, String password);
}
