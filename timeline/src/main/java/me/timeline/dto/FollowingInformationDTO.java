package me.timeline.dto;

public class FollowingInformationDTO {
	
	private String email;
	private String nickname;
	
	public FollowingInformationDTO(String email, String nickname) {
		this.email = email;
		this.nickname = nickname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
