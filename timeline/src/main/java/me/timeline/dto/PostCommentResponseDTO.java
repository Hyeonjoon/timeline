package me.timeline.dto;

import java.util.Date;

public class PostCommentResponseDTO {
	
	private boolean success;
	private int writingId;
	private String content;
	private Date postTime;
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success  = success;
	}
	
	public int getWritingId() {
		return writingId;
	}
	
	public void setWritingId(int writingId){
		this.writingId = writingId;
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
