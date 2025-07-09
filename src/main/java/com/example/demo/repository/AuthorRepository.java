package com.example.demo.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

	@Query("""
			  SELECT a
			    FROM Author a
			   WHERE (:firstName  IS NULL OR LOWER(a.firstName) LIKE LOWER(CONCAT('%',:firstName,'%')))
			     AND (:lastName   IS NULL OR LOWER(a.lastName)  LIKE LOWER(CONCAT('%',:lastName,'%')))
			     AND (:birthYear  IS NULL OR FUNCTION('YEAR', a.birthDate) = :birthYear)
			     AND (:deathYear  IS NULL OR FUNCTION('YEAR', a.deathDate) = :deathYear)
			     AND (:nationality IS NULL OR LOWER(a.nationality) LIKE LOWER(CONCAT('%', :nationality, '%')))
			""")
	Page<Author> searchAuthors(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("birthYear") Integer birthYear, @Param("deathYear") Integer deathYear,
			@Param("nationality") String nationality, Pageable pageable);

}
