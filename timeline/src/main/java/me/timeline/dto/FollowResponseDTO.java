package me.timeline.dto;

public class FollowResponseDTO {
	
	private boolean success;
	
	public FollowResponseDTO(boolean success) {
		this.success = success;
	}
	
	public FollowResponseDTO() {
		
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
