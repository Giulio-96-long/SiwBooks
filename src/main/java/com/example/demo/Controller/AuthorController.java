package com.example.demo.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AuthorDto;
import com.example.demo.entity.Author;
import com.example.demo.serviceImpl.service.AuthorService;

@Controller
@RequestMapping("/authors")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@GetMapping
	public String listAuthors(@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName, @RequestParam(required = false) Integer birthYear,
			@RequestParam(required = false) Integer deathYear, @RequestParam(required = false) String nationality,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "firstName") String sortField,
			@RequestParam(defaultValue = "asc") String sortDir, Model model) {

		Sort sort = Sort.by(sortField);
		sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Author> authorsPage = authorService.searchAuthors(firstName, lastName, birthYear, deathYear, nationality,
				pageable);

		model.addAttribute("authorsPage", authorsPage);
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		model.addAttribute("birthYear", birthYear);
		model.addAttribute("deathYear", deathYear);
		model.addAttribute("nationality", nationality);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);

		return "authors/list";
	}

	@GetMapping("/{id}")
	public String showAuthor(@PathVariable Long id, Model model) {

		Author a = authorService.getAuthorById(id);
		model.addAttribute("author", a);
		return "authors/detail";
	}

	@GetMapping("/{id}/photo")
	public ResponseEntity<byte[]> servePhoto(@PathVariable Long id) {

		Author a = authorService.getAuthorById(id);
		byte[] photo = a.getPhoto();
		if (photo == null || photo.length == 0) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, a.getPhotoContentType()).body(photo);
	}

	@GetMapping("/new")
	public String newAuthorForm(Model model) {

		model.addAttribute("author", new Author());
		return "authors/form";

	}

	@PostMapping
	public String createAuthor(@
			ModelAttribute Author author, 
			@RequestParam("photoFile") MultipartFile photoFile) {

		authorService.createAuthor(author, photoFile);
		return "redirect:/authors";

	}

	@GetMapping("/edit/{id}")
	public String editAuthorForm(@PathVariable Long id, Model model) {
		model.addAttribute("author", authorService.getAuthorById(id));
		return "authors/form";
	}

	@PostMapping("/edit/{id}")
	public String updateAuthor(@PathVariable Long id, @ModelAttribute Author author,
			@RequestParam(name = "photoFile", required = false) MultipartFile photoFile) {

		authorService.updateAuthor(id, author, photoFile);
		return "redirect:/authors/" + id;

	}

	@DeleteMapping("/{id}/photo")
	public String deleteAuthorPhoto(@PathVariable Long id) {

		authorService.deleteImage(id);
		return "redirect:/authors/" + id;

	}

	@PostMapping("/{id}/delete")
	public String deleteAuthor(@PathVariable Long id) {

		authorService.deleteAuthor(id);
		return "redirect:/authors";

	}

}
