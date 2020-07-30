package me.timeline.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SignatureInformation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private int id;
	
	private int providerUserId;
	
	@Column(columnDefinition = "TEXT")
	private String passkey;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_id")
	private AuthProvider authProvider;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getProviderUserId() {
		return providerUserId;
	}
	
	public void setProviderUserId(int providerUserId) {
		this.providerUserId = providerUserId;
	}
	
	public String getPasskey() {
		return passkey;
	}
	
	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public AuthProvider getAuthProvider() {
		return authProvider;
	}
	
	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}
}
