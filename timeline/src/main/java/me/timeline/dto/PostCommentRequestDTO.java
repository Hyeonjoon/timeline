package me.timeline.dto;

public class PostCommentRequestDTO {
	
	private int writingId;
	private String content;
	
	public PostCommentRequestDTO(int writingId, String content) {
		this.writingId = writingId;
		this.content = content;
	}
	
	public PostCommentRequestDTO() {
		
	}
	
	public int getWritingId() {
		return writingId;
	}
	
	public void setWritingId(int writingId) {
		this.writingId = writingId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
