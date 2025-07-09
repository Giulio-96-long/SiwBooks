package com.example.demo.serviceImpl.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.dto.RatingCountResponseDto;
import com.example.demo.entity.BookReview;

public interface ReviewService {

	List<BookReview> getReviewsByBook(Long bookId);

	BookReview addReview(Long bookId, BookReview review);

	void deleteReview(Long reviewId);

	Optional<BookReview> getReviewForCurrentUser(Long bookId);

	void updateReview(Long reviewId, BookReview review);

	Page<BookReview> findByBookId(Long bookId, Pageable pageable);

	Page<BookReview> searchReviews(String title, String username, LocalDate startDate, LocalDate endDate,
			Pageable pageable);

	double getAverageRating(Long bookId);

	long getTotalReviews(Long bookId);

	List<RatingCountResponseDto> getRatingCounts(Long bookId);

}