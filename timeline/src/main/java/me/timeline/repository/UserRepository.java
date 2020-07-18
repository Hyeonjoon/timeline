package me.timeline.repository;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	public boolean existsByEmail(String email);
}
