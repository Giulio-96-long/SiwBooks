package com.example.demo.util;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Credentials;
import com.example.demo.repository.CredentialsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		Optional<Credentials> maybeCred = credentialsRepository.findByEmail(email);
		if (maybeCred.isEmpty()) {			
			throw new UsernameNotFoundException("Nessun utente trovato con email: " + email);
		}

		Credentials cred = maybeCred.get();

		String role = cred.getRole();		
		if (!role.toUpperCase().startsWith("ROLE_")) {
		    role = "ROLE_" + role.toUpperCase();
		}
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

		return new org.springframework.security.core.userdetails
				.User(cred.getEmail(), cred.getPassword(), authorities);
	}

}