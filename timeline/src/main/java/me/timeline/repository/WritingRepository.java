package me.timeline.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.Writing;

public interface WritingRepository extends CrudRepository<Writing, Integer> {
	public boolean existsByUser_IdAndContent(int user_id, String content);
	public Optional<Writing> findByUser_IdAndContent(int user_id, String content);
}
