package me.timeline.dto;

public class PostWritingRequestDTO {
	
	private String content;
	
	public PostWritingRequestDTO(String content) {
		this.content = content;
	}
	
	public PostWritingRequestDTO() {
		
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
