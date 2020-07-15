package me.timeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.timeline.dto.SignatureInfoDTO;
import me.timeline.entity.TestEntity;
import me.timeline.repository.TimelineRepository;

@Service("TimelineService")
public class TimelineServiceImpl implements TimelineService {
	
	@Autowired
	TimelineRepository timelineRepository;
	
	
	public boolean signUpSucceed(String email, String password) {
		TestEntity testEntity = new TestEntity();
		testEntity.setName(email);
		testEntity.setPassword(password);
		timelineRepository.save(testEntity);
		return true;
	}
}
