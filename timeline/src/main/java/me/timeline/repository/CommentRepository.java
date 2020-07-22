package me.timeline.repository;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

}
