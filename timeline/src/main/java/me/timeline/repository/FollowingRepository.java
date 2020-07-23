package me.timeline.repository;

import org.springframework.data.repository.CrudRepository;

import me.timeline.entity.Following;

public interface FollowingRepository extends CrudRepository<Following, Integer>{

}
