package com.example.demo.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Il nome è obbligatorio")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Past(message = "La data di nascita deve essere nel passato")
    @Column(name="birth_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    
    @Past(message = "La data di morte deve essere nel passato")
    @Column(name="death_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deathDate;

    private String nationality;
    
    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;
    
    @Column(name = "photo_content_type")
    private String photoContentType;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;
    
    public Author() {}

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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(LocalDate deathDate) {
		this.deathDate = deathDate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	public String getPhotoContentType() {
		return photoContentType;
	}

	public void setPhotoContentType(String photoContentType) {
		this.photoContentType = photoContentType;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}   
	    
}