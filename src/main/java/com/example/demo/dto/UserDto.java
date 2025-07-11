package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

	@NotBlank(message = "Il nome è obbligatorio")
	private String firstName;

	@NotBlank(message = "Il cognome è obbligatorio")
	private String lastName;

	@NotBlank(message = "L'email è obbligatoria")
	@Email(message = "Email non valida")
	private String email;

	public UserDto() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
