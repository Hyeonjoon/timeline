package me.timeline.repository;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.Writing;

public interface WritingRepository extends CrudRepository<Writing, Integer> {
	
}
