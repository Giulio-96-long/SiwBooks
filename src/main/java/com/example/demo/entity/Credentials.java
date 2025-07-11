package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Credentials {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "L'email è obbligatoria")
	@Column(unique = true, nullable = false)
	private String email; 

	@NotBlank(message = "La password è obbligatoria")
	@Size(min = 6, message = "La password deve avere almeno 6 caratteri")
	@Column(nullable = false)
	private String password; 

	@Column(nullable = false)
	private String role = "USER"; 

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Credentials() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	
}
