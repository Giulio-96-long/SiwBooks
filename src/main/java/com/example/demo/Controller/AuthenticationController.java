package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.serviceImpl.service.UserService;


@Controller
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@GetMapping({ "/", "/home" })
	public String home() {
		return "redirect:/books";
	}

	@GetMapping("/login")
	public String showLogin(Model model, @RequestParam(value = "error", required = false) String error) {

		model.addAttribute("loginRequest", new LoginRequestDto());
		model.addAttribute("loginError", error != null ? "Email o password non validi" : null);
		return "authentication/login";

	}

	@GetMapping("/register")
	public String showRegister(Model model) {

		model.addAttribute("registration", new RegisterRequestDto());
		return "authentication/register";

	}

	@PostMapping("/register")
	public String doRegister(@ModelAttribute("registration") RegisterRequestDto dto) {

		userService.register(dto);
		return "redirect:/login";

	}

	@GetMapping("/default")
	public String defaultAfterLogin() {
		return "redirect:/books";
	}	
	
}
