package com.example.demo.serviceImpl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChangePasswordForm;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Credentials;
import com.example.demo.entity.User;
import com.example.demo.repository.CredentialsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.serviceImpl.service.CredentialsService;
import com.example.demo.serviceImpl.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final CredentialsRepository credRepo;
	private final PasswordEncoder passwordEncoder;
	private final CredentialsService credentialsService;

	public UserServiceImpl(UserRepository userRepo, CredentialsRepository credRepo, PasswordEncoder passwordEncoder,
			CredentialsService credentialsService) {
		this.userRepo = userRepo;
		this.credRepo = credRepo;
		this.passwordEncoder = passwordEncoder;
		this.credentialsService = credentialsService;
	}

	@Override
	public void register(RegisterRequestDto dto) {

		if (credRepo.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("Email già esistente");
		}

		// creo l’User
		User user = new User();
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());

		// creo le Credentials
		Credentials cred = new Credentials();
		cred.setEmail(dto.getEmail());
		cred.setPassword(passwordEncoder.encode(dto.getPassword()));
		cred.setRole("USER");

		user.setCredentials(cred);
		cred.setUser(user);

		userRepo.save(user);
	}

	@Override
	public User getUser(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
	}

	@Override
	@Transactional
	public UserDto getProfile() {
		Credentials credential = credentialsService.getCurrentCredentials();
		User user = credential.getUser();
		UserDto dto = new UserDto();
		dto.setEmail(credential.getEmail());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		return dto;
	}

	@Override
	@Transactional
	public UserDto updateProfile(UserDto dto) {
		Credentials credential = credentialsService.getCurrentCredentials();
		User user = credential.getUser();
		credential.setEmail(dto.getEmail());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		userRepo.save(user);
		return dto;
	}

	@Override
	@Transactional
	public void deleteProfile() {
		Credentials credential = credentialsService.getCurrentCredentials();
		userRepo.delete(credential.getUser());
	}

	@Override
	@Transactional
	public void changePassword(ChangePasswordForm form) {
		Credentials credential = credentialsService.getCurrentCredentials();
		if (!passwordEncoder.matches(form.getOldPassword(), credential.getPassword())) {
			throw new BadCredentialsException("Password corrente sbagliata");
		}
		credential.setPassword(passwordEncoder.encode(form.getNewPassword()));
		credRepo.save(credential);
	}

}
