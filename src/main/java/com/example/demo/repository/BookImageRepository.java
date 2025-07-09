package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookImage;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
	
	List<BookImage> findAllByBook(Book book);

}