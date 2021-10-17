package com.jk.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jk.demo.dao.ContactRepository;
import com.jk.demo.dao.UserRepository;
import com.jk.demo.entities.Contact;
import com.jk.demo.entities.User;
import com.jk.demo.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to response

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String username = principal.getName();
		System.out.print("USERNAME" + username);

		User user = userRepository.getUserByUserName(username);

		System.out.print("USERNAME" + user);

		model.addAttribute("user", user);

	}

	// dashbord home

	@RequestMapping("/index")
	public String dashbord(Model model, Principal principal) {

		String username = principal.getName();
		System.out.print("USERNMAE" + username);

		User user = userRepository.getUserByUserName(username);

		System.out.print("USERNMAE" + user);

		return "normal/user_dashbord";
	}

	// open add form handler

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("Contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing and contact from

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			String name = principal.getName();

			User user = this.userRepository.getUserByUserName(name);

			// proccessing and uploading file

			if (file.isEmpty()) {
				// their is no file
				contact.setImage("contact.png");

			} else {
				// now file is not empty upload file
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.print("Image uploaded");

			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.print("Contact" + contact);

			// message success

			session.setAttribute("message", new Message("your contact added successfully", "sucesss"));

		} catch (Exception e) {
			System.out.print("ERROR :" + e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong", "danger"));
		}

		return "normal/add_contact_form";

	}

	// Show Contact handler
	// per page = 5[n]
	// current page = 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {

		model.addAttribute("title", "show contacts");

		String username = principal.getName();

		User user = this.userRepository.getUserByUserName(username);

		Pageable pageable = PageRequest.of(page, 5);

		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// show particular contact deatil

	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid, Model model, Principal principal) {

		System.out.println("CID " + cid);

		Optional<Contact> contOptional = this.contactRepository.findById(cid);
		Contact contact = contOptional.get();

		// the user who login only data person seen data

		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		if (user.getId() == contact.getUser().getId())

			model.addAttribute("contact", contact);

		return "normal/contact_deatail";

	}

	// delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session,
			Principal principal) {

		Contact contact = this.contactRepository.findById(cid).get();

		// check Assignment

		User user = this.userRepository.getUserByUserName(principal.getName());

		user.getContacts().remove(contact);

		this.userRepository.save(user);

		session.setAttribute("message", new Message("Contact Delete successfully", "success"));

		return "redirect:/user/show-contacts/0";
	}

	// open form update handler

	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {

		model.addAttribute("title", "update contact");

		Optional<Contact> contacOptional = this.contactRepository.findById(cid);

		Contact contact = contacOptional.get();

		model.addAttribute("contact", contact);

		return "normal/update_form";
	}

	// update contact handler
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, HttpSession httpSession, Principal principal) {

		try {

			// old contact details

			Contact oldContactDetails = this.contactRepository.findById(contact.getCid()).get();

			if (!file.isEmpty()) {

				// work on file
				// update image

				// delete old photo

				File deleteFile = new ClassPathResource("static/img").getFile();
				File file2 = new File(deleteFile, oldContactDetails.getImage());
				file2.delete();

				// update new photo

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

			} else {

				contact.setImage(oldContactDetails.getImage());

			}

			String username = principal.getName();

			User user = this.userRepository.getUserByUserName(username);

			contact.setUser(user);

			this.contactRepository.save(contact);

			httpSession.setAttribute("message", new Message("update successfully", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("CONTACT" + contact.getName());
		System.out.println("CID" + contact.getCid());

		return "redirect:/user/" + contact.getCid() + "/contact";
	}

	// your profile handler
	@GetMapping("/profile")
	public String urProfile(Model model) {

		model.addAttribute("title", "This is your profile");

		return "normal/profile";
	}

	// open setting handler
	@GetMapping("/settings")
	public String openSettings() {

		return "normal/settings";
	}

	// change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			Principal principal , HttpSession session) {

		System.out.println("OLD " + oldPassword);
		System.out.println("NEW " + newPassword);

		String username = principal.getName();

		User currentUser = this.userRepository.getUserByUserName(username);
		
		System.out.println(currentUser.getPassword());
		
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())){
			
			// change password
		currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(currentUser);
		
		session.setAttribute("message", new Message("password changed successfully", "success"));
			
		}else {
			
			// error
			session.setAttribute("message", new Message(" please entere correct old password ", "danger"));
			
		}

		return "redirect:/user/index";
	}

}
