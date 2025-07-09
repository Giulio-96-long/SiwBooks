package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.RatingCountResponseDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookReview;
import com.example.demo.entity.User;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

	Page<BookReview> findByBookId(Long bookId, Pageable pageable);

	Optional<BookReview> findByUserAndBook(User user, Book book);

	Optional<BookReview> findByUserIdAndBookId(long userId, long bookId);

	@Query("""
	        SELECT r
	          FROM BookReview r
	         WHERE (:bookTitle IS NULL 
	                OR LOWER(r.book.title) LIKE LOWER(CONCAT('%',:bookTitle,'%')))
	           AND (:username  IS NULL 
	                OR LOWER(r.user.credentials.email) 
	                   LIKE LOWER(CONCAT('%',:username,'%')))
	           AND (:startDate IS NULL OR r.createdAt >= :startDate)
	           AND (:endDate   IS NULL OR r.createdAt <= :endDate)
	        """)
	    Page<BookReview> search(
	        @Param("bookTitle") String bookTitle,
	        @Param("username")  String username,
	        @Param("startDate") LocalDateTime startDate,
	        @Param("endDate")   LocalDateTime endDate,
	        Pageable pageable
	    );

	  //Valutazione media
    @Query("SELECT AVG(br.rating) FROM BookReview br WHERE br.book.id = :bookId")
    Optional<Double> findAverageRatingByBook(@Param("bookId") Long bookId);

    // Conteggio totale recensioni: metodo derivato
    long countByBook_Id(Long bookId);

    // Conteggio per ciascun rating, direttamente in DTO
    @Query("SELECT new com.example.demo.dto.RatingCountResponseDto(br.rating, COUNT(br)) "
         + "FROM BookReview br "
         + "WHERE br.book.id = :bookId "
         + "GROUP BY br.rating")
    List<RatingCountResponseDto> findRatingCountsByBook(@Param("bookId") Long bookId);
}
