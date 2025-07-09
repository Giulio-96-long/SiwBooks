package com.example.demo.serviceImpl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Credentials;
import com.example.demo.repository.CredentialsRepository;
import com.example.demo.serviceImpl.service.CredentialsService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Service
public class CredentialsServiceImpl implements CredentialsService {

	private final CredentialsRepository credRepo;
	private final PasswordEncoder passwordEncoder;

	public CredentialsServiceImpl(CredentialsRepository credRepo, PasswordEncoder passwordEncoder) {
		this.credRepo = credRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Credentials saveCredentials(Credentials credentials) {
	
		credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
		return credRepo.save(credentials);
	}

	@Override
	public Credentials findByEmail(String email) {
		return credRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Username non trovato"));
	}

	@Override
	public Credentials getCurrentCredentials() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			return null;
		}

		Object principal = auth.getPrincipal();

		String email = null;

		if (principal instanceof OAuth2User) {
			OAuth2User oauth2User = (OAuth2User) principal;
			email = oauth2User.getAttribute("email");

		} else if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			email = userDetails.getUsername();

		} else if (principal instanceof String) {
			email = (String) principal;
		}

		if (email == null) {
			return null;
		}

		return credRepo.findByEmail(email).orElse(null);
	}
}