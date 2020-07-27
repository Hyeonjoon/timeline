package me.timeline.dto;

import java.util.Date;

public class CommentInformationDTO {
	
	private int id;
	private UserInformationDTO userInformationDTO;
	private String content;
	private Date createdAt;
	
	public CommentInformationDTO(int id, UserInformationDTO userInformationDTO, String content, Date createdAt) {
		this.id = id;
		this.userInformationDTO = userInformationDTO;
		this.content = content;
		this.createdAt = createdAt;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public UserInformationDTO getUserInformationDTO() {
		return userInformationDTO;
	}
	
	public void setUserInformationDTO(UserInformationDTO userInformationDTO) {
		this.userInformationDTO = userInformationDTO;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
