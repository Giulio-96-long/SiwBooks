package com.example.demo.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RatingCountResponseDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookReview;
import com.example.demo.entity.Credentials;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BookReviewRepository;
import com.example.demo.serviceImpl.service.CredentialsService;
import com.example.demo.serviceImpl.service.ReviewService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private BookReviewRepository reviewRepo;

	@Autowired
	private CredentialsService credentialsService;

	@Override
	public List<BookReview> getReviewsByBook(Long bookId) {

		Book book = bookRepo.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
		return book.getReviews();
	}

	@Override
	public BookReview addReview(Long bookId, BookReview review) {
		// Solo utenti registrati (ruolo "USER") possono aggiungere recensioni
		Credentials me = credentialsService.getCurrentCredentials();
		if (!"USER".equals(me.getRole())) {
			throw new AccessDeniedException("Solo utenti registrati possono aggiungere recensioni");
		}

		Book book = bookRepo.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));

		reviewRepo.findByUserIdAndBookId(me.getUser().getId(), bookId).ifPresent(r -> {
			throw new IllegalStateException("Hai già recensito questo libro");
		});

		review.setUser(me.getUser());
		review.setBook(book);
		review.setCreatedAt(LocalDateTime.now());
		return reviewRepo.save(review);
	}

	@Override
	public void deleteReview(Long reviewId) {
		// Solo admin può cancellare recensioni
		Credentials me = credentialsService.getCurrentCredentials();
		if (!"ADMIN".equals(me.getRole())) {
			throw new AccessDeniedException("Solo gli amministratori possono cancellare recensioni");
		}
		reviewRepo.deleteById(reviewId);
	}

	@Override
	public Optional<BookReview> getReviewForCurrentUser(Long bookId) {
		
			Credentials me = credentialsService.getCurrentCredentials();
			if (me == null || me.getUser() == null) {		       
		        return Optional.empty();
		    }
			return reviewRepo.findByUserIdAndBookId(me.getUser().getId(), bookId);		
	}

	@Override
	public void updateReview(Long reviewId, BookReview updated) {
		Credentials me = credentialsService.getCurrentCredentials();
		if (!"USER".equals(me.getRole())) {
			throw new AccessDeniedException("Solo l'autore può modificare la propria recensione");
		}
		BookReview existing = reviewRepo.findById(reviewId)
				.orElseThrow(() -> new EntityNotFoundException("Review not found"));

		if (!existing.getUser().getId().equals(me.getUser().getId())) {
			throw new AccessDeniedException("Non puoi modificare recensioni di altri utenti");
		}

		existing.setTitle(updated.getTitle());
		existing.setRating(updated.getRating());
		existing.setText(updated.getText());
		existing.setCreatedAt(LocalDateTime.now());
		reviewRepo.save(existing);
	}

	@Override
	public Page<BookReview> findByBookId(Long bookId, Pageable pageable) {
		return reviewRepo.findByBookId(bookId, pageable);
	}

	@Override
	public Page<BookReview> searchReviews(String title, String username, LocalDate startDate, LocalDate endDate,
			Pageable pageable) {
		// convert LocalDate → LocalDateTime agli estremi della giornata
		LocalDateTime sd = startDate == null ? null : startDate.atStartOfDay();
		LocalDateTime ed = endDate == null ? null : endDate.atTime(23, 59, 59);
		return reviewRepo.search(title, username, sd, ed, pageable);
	}

	@Override
	public double getAverageRating(Long bookId) {
		return reviewRepo.findAverageRatingByBook(bookId).orElse(0.0);
	}

	@Override
	public long getTotalReviews(Long bookId) {
		return reviewRepo.countByBook_Id(bookId);
	}

	@Override
	public List<RatingCountResponseDto> getRatingCounts(Long bookId) {
	
		Map<Integer, Long> map = reviewRepo.findRatingCountsByBook(bookId).stream()
				.collect(Collectors.toMap(RatingCountResponseDto::getRating, RatingCountResponseDto::getCount));

		List<RatingCountResponseDto> result = new ArrayList<>();
		for (int i = 5; i >= 1; i--) {
			result.add(new RatingCountResponseDto(i, map.getOrDefault(i, 0L)));
		}
		return result;
	}
}