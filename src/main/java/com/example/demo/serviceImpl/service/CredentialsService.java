package com.example.demo.serviceImpl.service;

import com.example.demo.entity.Credentials;

public interface CredentialsService {

	Credentials saveCredentials(Credentials credentials);

	Credentials findByEmail(String username);
	
	Credentials getCurrentCredentials();
}
