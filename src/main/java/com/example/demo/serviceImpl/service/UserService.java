package com.example.demo.serviceImpl.service;

import com.example.demo.dto.ChangePasswordForm;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;

public interface UserService {

	void register(RegisterRequestDto user);

	User getUser(Long id);

	UserDto getProfile();

	void changePassword(ChangePasswordForm form);

	void deleteProfile();

	UserDto updateProfile(UserDto dto);

}
