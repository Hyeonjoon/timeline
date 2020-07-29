package me.timeline.dto;

public class FollowRequestDTO {
	
	private int targetId;
	
	public FollowRequestDTO(int targetId) {
		this.targetId = targetId;
	}
	
	public FollowRequestDTO() {
		
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
}
