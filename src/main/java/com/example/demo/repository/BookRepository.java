package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("""
			  SELECT b
			    FROM Book b
			   WHERE (:title IS NULL
			          OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
			     AND (:year  IS NULL
			          OR b.publicationYear = :year)
			""")
	Page<Book> searchBooks(@Param("title") String title, @Param("year") Integer year, Pageable pageable);
}
