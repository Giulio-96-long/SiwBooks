package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.serviceImpl.service.UserService;

import jakarta.validation.Valid;


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
	public String doRegister(
	    @Valid @ModelAttribute("registration") RegisterRequestDto dto,
	    BindingResult br,
	    Model model) {

	    if (br.hasErrors()) {
	        return "authentication/register";
	    }

	    try {
	        userService.register(dto);
	    } catch (IllegalArgumentException e) {	     
	        br.rejectValue("email", "error.registration", e.getMessage());
	        return "authentication/register";
	    }

	    return "redirect:/login";
	}

	@GetMapping("/default")
	public String defaultAfterLogin() {
		return "redirect:/books";
	}	
	
}
