package me.timeline.dto;

public class SignUpRequestDTO {
	private String email;
	private String nickname;
	private String password;
	private String provider;
	private int providerUserId;
	
	public SignUpRequestDTO() {
		
	}
	
	public SignUpRequestDTO(String email, String nickname, String password, String provider, int providerUserId) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.provider = provider;
		this.providerUserId = providerUserId;
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
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public int getProviderUserId() {
		return providerUserId;
	}
	
	public void setProviderUserId(int providerUserId) {
		this.providerUserId = providerUserId;
	}
}
