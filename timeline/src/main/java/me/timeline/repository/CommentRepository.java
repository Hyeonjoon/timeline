package me.timeline.repository;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
	public boolean existsByWriting_IdAndContent(int writing_id, String content);
}
