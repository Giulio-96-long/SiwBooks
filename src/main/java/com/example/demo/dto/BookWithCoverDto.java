package com.example.demo.dto;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookImage;

public class BookWithCoverDto {
	private Book book;
	private BookImage cover;

	public BookWithCoverDto(Book book) {
		this.book = book;
		this.cover = book.getImages().stream().filter(BookImage::isCover).findFirst().orElse(null);
	}

	public Book getBook() {
		return book;
	}

	public BookImage getCover() {
		return cover;
	}
}
