package me.timeline.dto;

public class UserInformationDTO {
	
	private int id;
	private String email;
	private String nickname;
	
	public UserInformationDTO(int id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
