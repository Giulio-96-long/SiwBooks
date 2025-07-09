package com.example.demo.serviceImpl;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Credentials;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.serviceImpl.service.AuthorService;
import com.example.demo.serviceImpl.service.CredentialsService;
import com.example.demo.serviceImpl.service.ErrorLogService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AuthorRepository authorRepo;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private ErrorLogService errorLogService;

	@Autowired
	private BookRepository bookRepository;

	@Override
	public Author createAuthor(Author author, MultipartFile photo) {
		// solo ADMIN può creare autori
		Credentials me = credentialsService.getCurrentCredentials();
		if (!"ADMIN".equals(me.getRole())) {
			throw new AccessDeniedException("Solo gli amministratori possono creare autori");
		}

		if (photo != null && !photo.isEmpty()) {
			try {
				author.setPhoto(photo.getBytes());
			} catch (IOException e) {
				errorLogService.saveErrorLog("Errore inserimento della foto " + "dell'autore" + author.getFirstName()
						+ " " + " " + author.getLastName(), e);
			}
			author.setPhotoContentType(photo.getContentType());
		}
		return authorRepo.save(author);
	}

	@Override
	public Author updateAuthor(Long id, Author author, MultipartFile photo) {
		// solo ADMIN può modificare autori
		Credentials me = credentialsService.getCurrentCredentials();
		if (!"ADMIN".equals(me.getRole())) {
			throw new AccessDeniedException("Solo gli amministratori possono modificare autori");
		}
		Author existing = getAuthorById(id);
		existing.setFirstName(author.getFirstName());
		existing.setLastName(author.getLastName());
		existing.setBirthDate(author.getBirthDate());
		existing.setDeathDate(author.getDeathDate());
		existing.setNationality(author.getNationality());
		if (photo != null && !photo.isEmpty()) {
			try {
				existing.setPhoto(photo.getBytes());
			} catch (IOException e) {
				errorLogService.saveErrorLog("Errore modifca della foto " + "dell'autore" + author.getFirstName() + " "
						+ " " + author.getLastName(), e);
			}
			existing.setPhotoContentType(photo.getContentType());
		}
		return authorRepo.save(existing);
	}

	@Override
	public Author getAuthorById(Long id) {
		return authorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Author not found"));
	}

	@Override
	public List<Author> getAllAuthors() {
		return authorRepo.findAll();
	}

	@Override
	@Transactional
	public void deleteAuthor(Long authorId) {
		Credentials me = credentialsService.getCurrentCredentials();
		if (!"ADMIN".equals(me.getRole())) {
			throw new AccessDeniedException("Solo gli amministratori possono cancellare autori");
		}
		Author author = authorRepo.findById(authorId)
				.orElseThrow(() -> new EntityNotFoundException("Autore non trovato"));

		for (Book book : author.getBooks()) {
			book.getAuthors().remove(author);
			bookRepository.save(book);
		}

		authorRepo.delete(author);
	}

	@Override
	public Page<Author> searchAuthors(String firstName, String lastName, Integer birthYear, Integer deathYear,
			String nationality, Pageable pageable) {
		String fn = (firstName != null && !firstName.isBlank()) ? firstName.trim() : null;
		String ln = (lastName != null && !lastName.isBlank()) ? lastName.trim() : null;
		String nat = (nationality != null && !nationality.isBlank()) ? nationality.trim() : null;
		return authorRepo.searchAuthors(fn, ln, birthYear, deathYear, nat, pageable);
	}

	@Override
	public void deleteImage(Long authorId) {

		Author imgAuthor = authorRepo.findById(authorId)
				.orElseThrow(() -> new EntityNotFoundException("Immagine non trovata"));

		imgAuthor.setPhoto(null);
		authorRepo.save(imgAuthor);
	}

}