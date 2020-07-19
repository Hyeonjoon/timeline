package me.timeline.dto;

public class JwtInputDTO {
	
	private int id;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIdString() {
		return Integer.toString(id);
	}
}
