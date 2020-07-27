package me.timeline.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WritingInformationDTO {
	
	private int id;
	private UserInformationDTO userInformationDTO;
	private String content;
	private Date createdAt;
	private List<CommentInformationDTO> commentInformationDTOList;
	
	public WritingInformationDTO(int id, UserInformationDTO userInformationDTO, String content, Date createdAt, List<CommentInformationDTO> commentInformationDTOList) {
		this.id = id;
		this.userInformationDTO = userInformationDTO;
		this.content = content;
		this.createdAt = createdAt;
		this.commentInformationDTOList = commentInformationDTOList;
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
	
	public void initCommentInformationDTOList() {
		commentInformationDTOList = new ArrayList<>();
	}
	
	public void addCommentInformationDTOList(CommentInformationDTO commentInformationDTO) {
		this.commentInformationDTOList.add(commentInformationDTO);
	}
	
	public List<CommentInformationDTO> getCommentInformationDTOList() {
		return commentInformationDTOList;
	}
}
