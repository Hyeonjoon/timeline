package me.timeline.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	public boolean existsByEmail(String email);
	public Optional<User> findByEmail(String email);
}
