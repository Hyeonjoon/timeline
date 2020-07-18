package me.timeline.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.AuthProvider;

public interface AuthProviderRepository extends CrudRepository<AuthProvider, Integer> {
	public Optional<AuthProvider> findByType(String type);
}
