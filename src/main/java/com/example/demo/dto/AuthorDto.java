package com.example.demo.dto;

public class AuthorDto {

	private Long id;

	private String firstName;

	private String lastName;

	public AuthorDto(Long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

}
