package com.example.demo.Controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.BookReview;
import com.example.demo.serviceImpl.service.CredentialsService;
import com.example.demo.serviceImpl.service.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private CredentialsService credentialsService;

	@GetMapping
	public String listReviews(@RequestParam(required = false) String bookTitle,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(defaultValue = "0") int page, Model model) throws AccessDeniedException {

		String currentRole = credentialsService.getCurrentCredentials().getRole();
		if (!"ADMIN".equals(currentRole)) {
			throw new AccessDeniedException("Only admins can view all reviews");
		}
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
		Page<BookReview> reviewsPage = reviewService.searchReviews(bookTitle, username, startDate, endDate, pageable);

		model.addAttribute("reviewsPage", reviewsPage);
		model.addAttribute("bookTitle", bookTitle);
		model.addAttribute("username", username);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("currentRole", currentRole);
		return "reviews/list";

	}

	@PostMapping("/books/{bookId}")
	public String addReview(@PathVariable Long bookId, @ModelAttribute("newReview") BookReview newReview) {

		reviewService.addReview(bookId, newReview);
		return "redirect:/books/" + bookId;

	}

	@PostMapping("/books/{bookId}/edit/{reviewId}")
	public String editReview(@PathVariable Long bookId, @PathVariable Long reviewId,
			@ModelAttribute("reviewToEdit") BookReview reviewForm) {

		reviewService.updateReview(reviewId, reviewForm);
		return "redirect:/books/" + bookId;

	}

	@PostMapping("/{id}/delete")
	public String deleteReview(@PathVariable Long id) {

		reviewService.deleteReview(id);
		return "redirect:/reviews";

	}
}