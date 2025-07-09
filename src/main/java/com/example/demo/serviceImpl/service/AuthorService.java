package com.example.demo.serviceImpl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Author;

public interface AuthorService {

	List<Author> getAllAuthors();

	Author getAuthorById(Long id);

	void deleteAuthor(Long id);

	Author createAuthor(Author author, MultipartFile photo);

	Author updateAuthor(Long id, Author author, MultipartFile photo);

	Page<Author> searchAuthors(String firstName, String lastName, Integer birthYear, Integer deathYear,
			String nationality, Pageable pageable);

	void deleteImage(Long authorId);
	
}
