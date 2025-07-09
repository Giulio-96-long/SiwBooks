package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
	
    Optional<Credentials> findByEmail(String email);

	boolean existsByEmail(String email);
}
