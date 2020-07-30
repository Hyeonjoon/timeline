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
public class AuthProvider {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	@Column(columnDefinition = "CHAR(20)")
	private String type;
	
	@OneToMany(mappedBy = "authProvider", cascade = CascadeType.ALL)
	private List<SignatureInformation> signatureInformationList;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void initSignatureInformation() {
		this.signatureInformationList = new ArrayList<>();
	}
	
	public void addSignatureInformation(SignatureInformation signatureInformation) {
		this.signatureInformationList.add(signatureInformation);
	}
}
