package me.timeline.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.Following;

public interface FollowingRepository extends CrudRepository<Following, Integer>{
	Optional <Following> findByFollower_IdAndFollowee_Id(int follower_id, int followee_id);
}
