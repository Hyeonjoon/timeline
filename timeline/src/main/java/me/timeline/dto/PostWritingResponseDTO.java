package me.timeline.dto;

import java.util.Date;

public class PostWritingResponseDTO {
	
	private boolean success;
	private String content;
	private Date postTime;
	
	public PostWritingResponseDTO(boolean success, String content, Date postTime) {
		this.success = success;
		this.content = content;
		this.postTime = postTime;
	}
	
	public PostWritingResponseDTO() {
		
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success  = success;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getPostTime() {
		return postTime;
	}
	
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	
}
