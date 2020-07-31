package me.timeline.dto;

public class SignInResponseDTO {
	private boolean success;
	private String jwtToken;
	
	public SignInResponseDTO(boolean success, String jwtToken) {
		this.success = success;
		this.jwtToken = jwtToken;
	}
	
	public SignInResponseDTO() {
		
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}
	
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
