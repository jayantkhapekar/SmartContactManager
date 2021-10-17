package com.jk.demo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jk.demo.dao.UserRepository;
import com.jk.demo.entities.User;
import com.jk.demo.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/test")
	@ResponseBody
	public String test() {

		User user = new User();
		user.setName("thakre");
		user.setEmail("akshy@gmail.com");
		user.setPassword("1211441991");
		user.setAbout("progrmmer");
		userRepository.save(user);

		return "home";
	}
	
	
	

	@RequestMapping("/home")
	public String home(Model model) {

		model.addAttribute("title", "this is jayant");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "this is about");
		return "home";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "this is signup");
		model.addAttribute("user", new User());
		return "signup";
	}

	
	// login handler
	
	@RequestMapping("/signin")
	public String signin(Model model) {

		model.addAttribute("title", "this is signin");
		
		return "signin";
	}
	
	
	
	
	
	
	// handler for register

	@RequestMapping(value = "/do_register", method = RequestMethod.POST)

	public String registerUser(@ModelAttribute("user") User user,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				System.out.print(" You have not agreed the term and condition");
				throw new Exception("You have not agreed the term and condition");
			}

			System.out.print(" Agrreement " + agreement);
			System.out.print("User" + user);
			
			user.setImageUrl("default.png");

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("successfully  register  ...!! ", "Alert Successs"));

			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong", "Alert dangerous"));

			return "signup";
		}

	}

}
