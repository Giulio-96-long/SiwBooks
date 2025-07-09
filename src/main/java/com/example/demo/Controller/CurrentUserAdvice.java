package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.entity.Credentials;
import com.example.demo.serviceImpl.service.CredentialsService;

@ControllerAdvice
public class CurrentUserAdvice {

	@Autowired
    private CredentialsService credentialsService;

	@ModelAttribute("currentUser")
	public Credentials currentUser() {

		return credentialsService.getCurrentCredentials();
	}
}
