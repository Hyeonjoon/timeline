package me.timeline.repository;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.TestEntity;

public interface TimelineRepository extends CrudRepository<TestEntity, Integer>{
	
}
