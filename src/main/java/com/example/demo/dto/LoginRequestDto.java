package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

	@NotBlank(message = "Inserisci l'email")
	@Email(message = "Email non valida")
	private String email;

	@NotBlank(message = "Inserisci la password")
	private String password;

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

	public LoginRequestDto() {
		super();
	}

}
