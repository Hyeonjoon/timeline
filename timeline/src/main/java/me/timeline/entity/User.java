package me.timeline.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(unique = true, nullable = false, length = 320)
	private String email;
	
	@Column(unique = true, nullable = false, columnDefinition = "CHAR(32)")
	private String nickname;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<SignatureInformation> signatureInformationList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Writing> writingList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Comment> commentList;
	
	/* Following list has users who are followed by this user. Thus this user is a follower here. */
	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
	private List<Following> followingList;
	
	/* Follower list has users who are following this user. Thus this user is a followee here. */
	@OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
	private List<Following> followerList;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public void initSignatureInformationList() {
		this.signatureInformationList = new ArrayList<>();
	}
	
	public void addSignatureInformation(SignatureInformation signatureInformation) {
		this.signatureInformationList.add(signatureInformation);
	}
	
	public void initWritingList() {
		this.writingList = new ArrayList<>();
	}
	
	public void addWriting(Writing writing) {
		this.writingList.add(writing);
	}
	
	public List<Writing> getWritingList() {
		return writingList;
	}
	
	public void initCommentList() {
		this.commentList =new ArrayList<>();
	}
	
	public void addComment(Comment comment) {
		this.commentList.add(comment);
	}
	
	public void initFollowingList() {
		this.followingList = new ArrayList<>();
	}
	
	public void addFollowing(Following following) {
		this.followingList.add(following);
	}
	
	public void removeFollowing(Following following) {
		this.followingList.remove(following);
	}
	
	public List<Following> getFollowingList() {
		return followingList;
	}
	
	public void initFollowerList() {
		this.followerList = new ArrayList<>();
	}
	
	public void addFollower(Following following) {
		this.followerList.add(following);
	}
	
	public void removeFollower(Following following) {
		this.followerList.remove(following);
	}
	
	public List<Following> getFollowerList() {
		return followerList;
	}
}
