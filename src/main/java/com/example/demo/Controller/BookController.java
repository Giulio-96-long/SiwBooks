package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.BookFormDto;
import com.example.demo.dto.BookWithCoverDto;
import com.example.demo.dto.RatingCountResponseDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookImage;
import com.example.demo.entity.BookReview;
import com.example.demo.serviceImpl.service.AuthorService;
import com.example.demo.serviceImpl.service.BookService;
import com.example.demo.serviceImpl.service.ReviewService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private ReviewService reviewService;

	@GetMapping
	public String listBooks(@RequestParam(required = false) String title, @RequestParam(required = false) Integer year,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size,
			@RequestParam(defaultValue = "title") String sortField, @RequestParam(defaultValue = "asc") String sortDir,
			Model model) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<BookWithCoverDto> booksPage = (title == null && year == null)
		        ? bookService.getAllBooks(pageable)
		        : bookService.searchBooks(title, year, pageable);

		model.addAttribute("booksPage", booksPage);
		model.addAttribute("title", title);
		model.addAttribute("year", year);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		return "books/list";

	}

	@GetMapping("/{id}")
	public String showBook(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model) {

		Book book = bookService.getBookById(id);

		// calcoli delle recensioni
		double avg = reviewService.getAverageRating(id);
		long total = reviewService.getTotalReviews(id);
		List<RatingCountResponseDto> counts = reviewService.getRatingCounts(id);

		model.addAttribute("book", book);
		model.addAttribute("averageRating", String.format("%.1f", avg));
		model.addAttribute("totalReviews", total);
		model.addAttribute("ratingCounts", counts);

		Optional<BookReview> myReview = reviewService.getReviewForCurrentUser(id);
		if (myReview.isPresent()) {
			model.addAttribute("reviewToEdit", myReview.get());
		} else {
			model.addAttribute("newReview", new BookReview());
		}

		Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
		model.addAttribute("reviewsPage", reviewService.findByBookId(id, pageable));

		return "books/detail";

	}

	@GetMapping(value = "/{bookId}/images/{imageId}", produces = { MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	@ResponseBody
	public byte[] serveImage(@PathVariable Long bookId, @PathVariable Long imageId) {

		BookImage img = bookService.getImageById(imageId);
		if (img == null || img.getImage().length == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return img.getImage();
	}

	@GetMapping("/new")
	public String newBookForm(Model model) {
		model.addAttribute("bookForm", new BookFormDto());
		model.addAttribute("allAuthors", authorService.getAllAuthors());
		return "books/form";
	}

	@PostMapping("/new")
	public String createBook(
	    @Valid @ModelAttribute("bookForm") BookFormDto form,
	    BindingResult br,
	    Model model) {

	    if (br.hasErrors()) {
	        model.addAttribute("allAuthors", authorService.getAllAuthors());
	        return "books/form";
	    }

	    Book saved = bookService.saveBook(0L, form);
	    return "redirect:/books/" + saved.getId();
	}


	@GetMapping("/edit/{id}")
	public String editBookForm(@PathVariable Long id, Model model) {

		Book existing = bookService.getBookById(id);
		BookFormDto form = bookService.mapImgtoBook(existing);
		List<Author> allAuthors = authorService.getAllAuthors();
		model.addAttribute("bookForm", form);
		model.addAttribute("book", existing);
		model.addAttribute("allAuthors", allAuthors);
		return "books/form";
	}

	@PostMapping("/edit/{id}")
	public String updateBook(
	    @PathVariable Long id,
	    @Valid @ModelAttribute("bookForm") BookFormDto form,
	    BindingResult br,
	    Model model) {

	    if (br.hasErrors()) {
	        model.addAttribute("book", bookService.getBookById(id));
	        model.addAttribute("allAuthors", authorService.getAllAuthors());
	        return "books/form";
	    }

	    Book saved = bookService.saveBook(id, form);
	    return "redirect:/books/" + saved.getId();
	}


	@PostMapping("/{id}/delete")
	public String deleteBook(@PathVariable Long id) {

		bookService.deleteBook(id);
		return "redirect:/books";

	}

	@DeleteMapping("/{bookId}/images/{imgId}")
	@ResponseBody
	public ResponseEntity<Void> deleteBookImage(@PathVariable Long bookId, @PathVariable Long imgId) {
	    bookService.deleteImage(bookId, imgId);
	    return ResponseEntity.ok().build(); 
	}
}