package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.util.StringToAuthorConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final StringToAuthorConverter stringToAuthorConverter;

  public WebConfig(StringToAuthorConverter stringToAuthorConverter) {
    this.stringToAuthorConverter = stringToAuthorConverter;
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(stringToAuthorConverter);
  }
}