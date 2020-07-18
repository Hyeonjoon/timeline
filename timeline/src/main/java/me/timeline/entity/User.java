package me.timeline.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String email;
	private String nickname;
	
	@OneToMany(mappedBy = "user", cascade= CascadeType.ALL)
	private List<SignatureInformation> signatureInformationList;
	
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
	
	public void initSignatureInformation() {
		this.signatureInformationList = new ArrayList<>();
	}
	
	public void addSignatureInformation(SignatureInformation signatureInformation) {
		this.signatureInformationList.add(signatureInformation);
	}
	
}