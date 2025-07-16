package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    private String firstName;
    
    @NotBlank(message = "Il cognome è obbligatorio")
    private String lastName;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Credentials credentials;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BookReview> reviews;

    public User() {}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<BookReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<BookReview> reviews) {
		this.reviews = reviews;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
    
}