package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDto {

	@NotBlank(message = "Il nome è obbligatorio")
	private String firstName;

	@NotBlank(message = "Il cognome è obbligatorio")
	private String lastName;

	@NotBlank(message = "L'email è obbligatoria")
	@Email(message = "Email non valida")
	private String email;

	@NotBlank(message = "La password è obbligatoria")
	@Size(min = 6, message = "La password deve contenere almeno 6 caratteri")
	private String password;

	public RegisterRequestDto() {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
