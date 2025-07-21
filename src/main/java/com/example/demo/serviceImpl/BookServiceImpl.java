package com.example.demo.serviceImpl;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.BookFormDto;
import com.example.demo.dto.BookWithCoverDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookImage;
import com.example.demo.entity.Credentials;
import com.example.demo.repository.BookImageRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.serviceImpl.service.AuthorService;
import com.example.demo.serviceImpl.service.BookService;
import com.example.demo.serviceImpl.service.CredentialsService;
import com.example.demo.serviceImpl.service.ErrorLogService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private BookImageRepository imageRepo;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private ErrorLogService errorLogService;

	@Override
	@Transactional
	public Book saveBook(Long id, BookFormDto form) {
	    // autorizzazioni e caricamento/creazione (resta identico)
	    Credentials me = credentialsService.getCurrentCredentials();
	    if (!"ADMIN".equals(me.getRole())) {
	        throw new AccessDeniedException("Solo ADMIN possono modificare libri");
	    }
	    Book book = (id == null || id == 0L) ? new Book() : getBookById(id);
	    book.setTitle(form.getTitle());
	    book.setPublicationYear(form.getPublicationYear());
	    // autori (idem)
	    List<Author> authors = authorService.getAllAuthors().stream()
	        .filter(a -> form.getAuthorIds().contains(a.getId()))
	        .collect(Collectors.toList());
	    book.setAuthors(authors);

	    // **1. Carico le nuove immagini e le metto in una lista temporanea**
	    List<BookImage> newlyAdded = new ArrayList<>();
	    for (MultipartFile file : form.getImages()) {
	        if (file.isEmpty()) continue;
	        BookImage img = new BookImage();
	        img.setBook(book);
	        try {
	            img.setImage(file.getBytes());
	        } catch (IOException e) {
	            errorLogService.saveErrorLog("Errore upload immagine", e);
	        }
	        img.setContentType(file.getContentType());
	        img.setCover(false);
	        book.getImages().add(img);      // viene aggiunta nella collection di Book
	        newlyAdded.add(img);            // ma la salvo anche qui
	    }

	    // **2. Resetto tutte le cover**
	    book.getImages().forEach(i -> i.setCover(false));

	    // **3. Se ho scelto una nuova cover, la cerco nella lista newlyAdded**
	    if (form.getNewCoverIndex() != null) {
	        int idx = form.getNewCoverIndex();
	        if (idx >= 0 && idx < newlyAdded.size()) {
	            newlyAdded.get(idx).setCover(true);
	        }
	    }
	    // **4. Altrimenti consideriamo la cover preesistente**
	    else if (form.getExistingCoverImageId() != null) {
	        Long existingId = form.getExistingCoverImageId();
	        book.getImages().stream()
	            .filter(i -> existingId.equals(i.getId()))
	            .findFirst()
	            .ifPresent(i -> i.setCover(true));
	    }

	    // **5. Salvo tutto in un colpo solo**
	    return bookRepo.save(book);
	}

	@Override
	public Book getBookById(Long id) {

		return bookRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
	}	

	@Override
	public Page<BookWithCoverDto> searchBooks(String title, Integer year, Pageable pageable) {
	    String t = (title != null && !title.isBlank()) ? title.trim() : null;
	    return bookRepo.searchBooks(t, year, pageable)
	                   .map(BookWithCoverDto::new);
	}
	
	@Override
	public Page<BookWithCoverDto> getAllBooks(Pageable pageable) {
	    return bookRepo.findAll(pageable)
	                   .map(BookWithCoverDto::new);
	}
	
	@Override
	public void deleteBook(Long id) throws AccessDeniedException {

		Credentials me = credentialsService.getCurrentCredentials();
		if (!"ADMIN".equals(me.getRole())) {
			throw new AccessDeniedException("Solo gli amministratori possono cancellare libri");
		}
		bookRepo.deleteById(id);
	}

	@Override
	public BookImage getImageById(Long imageId) {

		return imageRepo.findById(imageId).orElseThrow(() -> new IllegalArgumentException("Image not found"));
	}

	@Override
	public void addImageToBook(Long bookId, byte[] data, String contentType) {

		Book book = getBookById(bookId);
		BookImage img = new BookImage();
		try {
			img.setBook(book);
			img.setImage(data);
			img.setContentType(contentType);
			imageRepo.save(img);
		} catch (Exception e) {
			errorLogService.saveErrorLog("Errore inserimento della foto " + "del libro" + bookId.toString(), e);
		}

		book.getImages().add(img);
	}

	@Override
	public List<BookImage> getImagesForBook(Long bookId) {

		Book book = getBookById(bookId);
		return imageRepo.findAllByBook(book);
	}

	@Override
	public BookFormDto mapImgtoBook(Book existing) {
		BookFormDto form = new BookFormDto();
		form.setId(existing.getId());
		form.setTitle(existing.getTitle());
		form.setPublicationYear(existing.getPublicationYear());
		form.setAuthorIds(existing.getAuthors().stream().map(Author::getId).toList());

		existing.getImages().stream()
        .filter(BookImage::isCover)
        .findFirst()
        .ifPresent(img -> {            
            form.setExistingCoverImageId(img.getId());
        });

		return form;

	}

	@Override
	public void deleteImage(Long bookId, Long imageId) {

		BookImage img = imageRepo.findById(imageId)
				.orElseThrow(() -> new EntityNotFoundException("Immagine non trovata"));

		if (!img.getBook().getId().equals(bookId)) {
			throw new IllegalArgumentException("Immagine non appartiene a questo libro");
		}
		imageRepo.delete(img);
	}

}