package com.example.demo.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Author;
import com.example.demo.serviceImpl.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class StringToAuthorConverter implements Converter<String,Author> {

	  private final AuthorService authorService;

	  public StringToAuthorConverter(AuthorService authorService) {
	    this.authorService = authorService;
	  }

	  @Override
	  public Author convert(String source) {
	    if (source == null || source.isBlank()) return null;
	    try {
	      Long id = Long.valueOf(source);
	      return authorService.getAuthorById(id);
	    } catch (NumberFormatException e) {
	      return null;
	    }
	  }
	}