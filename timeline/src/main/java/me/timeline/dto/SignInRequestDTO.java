package me.timeline.dto;

public class SignInRequestDTO {
	private String email;
	private String password;
	private String provider;
	
	public SignInRequestDTO(String email, String password, String provider) {
		this.email = email;
		this.password = password;
		this.provider = provider;
	}
	
	public SignInRequestDTO() {
		
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
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
}
