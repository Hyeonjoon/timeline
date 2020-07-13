package me.timeline.dto;

public class SignatureInfoDTO {
	private String userName = null;
	private int userId = -1;
	
	public String getUserName() {
		return userName;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
		return;
	}
	
	public void setUserId(int id) {
		this.userId = id;
		return;
	}
}
