package com.example.demo.serviceImpl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.dto.BookFormDto;
import com.example.demo.dto.BookWithCoverDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookImage;


public interface BookService {
	
	Book getBookById(Long id);

	void deleteBook(Long id) ;

	Page<BookWithCoverDto> getAllBooks(Pageable pageable);

	BookImage getImageById(Long imageId);

	void addImageToBook(Long bookId, byte[] data, String contentType);

	List<BookImage> getImagesForBook(Long bookId);

	BookFormDto mapImgtoBook(Book existing);

	Page<BookWithCoverDto> searchBooks(String title, Integer year, Pageable pageable);

	void deleteImage(Long bookId, Long imageId);

	Book saveBook(Long id, BookFormDto form);

	

}
