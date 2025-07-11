package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "book")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Il titolo è obbligatorio")
	@Column(nullable = false)
	private String title;

	@NotNull(message = "L’anno di pubblicazione è obbligatorio")
	@Column(name = "publication_year")
	private Integer publicationYear;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookImage> images = new ArrayList<>();

	@ManyToMany
	@JoinTable( name = "book_author",
	            joinColumns = @JoinColumn(name = "book_id"),
	            inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author> authors = new ArrayList<>();

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookReview> reviews = new ArrayList<>();

	public Book() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<BookReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<BookReview> reviews) {
		this.reviews = reviews;
	}

	public List<BookImage> getImages() {
		return images;
	}

	public void setImages(List<BookImage> images) {
		this.images = images;
	}	
}