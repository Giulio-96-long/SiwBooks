package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.ChangePasswordForm;
import com.example.demo.dto.UserDto;
import com.example.demo.serviceImpl.service.ErrorLogService;
import com.example.demo.serviceImpl.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/profile")
public class UserController {
	
	@Autowired
	private UserService userService;	

	@GetMapping
	public String viewProfile(Model model) {
		
			UserDto dto = userService.getProfile();
			model.addAttribute("userDto", dto);
			return "profile/view";
		
	}

	@PostMapping
	public String updateProfile(UserDto dto, RedirectAttributes ra) {

		userService.updateProfile(dto);
		ra.addFlashAttribute("success", "Profilo aggiornato!");
		return "redirect:/profile";
	}

	@PostMapping("/delete")
	public String deleteProfile(HttpServletRequest req) {
		userService.deleteProfile();
		req.getSession().invalidate();
		return "redirect:/";
	}

	@GetMapping("/change-password")
	public String changePasswordForm(Model model) {
		model.addAttribute("changePasswordForm", new ChangePasswordForm());
		return "profile/change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(@Valid ChangePasswordForm form, BindingResult br, RedirectAttributes ra) {
		if (br.hasErrors()) {
			return "profile/change-password";
		}
		try {
			userService.changePassword(form);
			ra.addFlashAttribute("success", "Password cambiata con successo!");
		} catch (BadCredentialsException ex) {
			br.rejectValue("oldPassword", "wrong.oldPassword", "Password corrente sbagliata");
			return "profile/change-password";
		}
		return "redirect:/profile";
	}
}