package me.timeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.timeline.dao.SignatureInfoDAOImpl;
import me.timeline.dto.SignatureInfoDTO;

@Service("TimelineService")
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	SignatureInfoDAOImpl SignatureInfoDAO;
	
	public SignatureInfoDTO signUpSucceed(String email, String password) {
		return SignatureInfoDAO.signUp(email, password);
	}
}
