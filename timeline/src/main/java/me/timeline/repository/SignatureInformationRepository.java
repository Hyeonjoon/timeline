package me.timeline.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.SignatureInformation;

public interface SignatureInformationRepository extends CrudRepository<SignatureInformation, Integer>{
	public Optional<SignatureInformation> findByUser_IdAndAuthProvider_Id(int user_id, int provider_id);
}
