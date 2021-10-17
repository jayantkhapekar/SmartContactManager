package com.jk.demo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jk.demo.dao.StudentRepository;
import com.jk.demo.entities.Contact;
import com.jk.demo.entities.Student;
import com.jk.demo.helper.Message;
import com.jk.demo.service.StudentService;

@Controller

public class StudentController {

	@Autowired
	private StudentService service;

	@Autowired
	private StudentRepository studentRepository;

	@RequestMapping("/jay")
	public String jay(Model model) {

		model.addAttribute("jay", "jayant khapekar");

		return "index";
	}

	// go to add student page

	@RequestMapping("/new")
	public String addNew(Model model) {

		model.addAttribute("new", "new Student");

		return "new";
	}

	// save student data

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveStudent(@ModelAttribute("student") Student student, HttpSession httpSession) {

		service.save(student);
		httpSession.setAttribute("message", new Message("add successfully", "success"));

		return "redirect:/";
	}

	// show complete data in list

	@GetMapping("/")
	public String listShowHome(Model model) {

		List<Student> liststudent = service.listAll();

		model.addAttribute("liststudent", liststudent);

		return "index";

	}

	// delete student records

	@RequestMapping("/delete/{id}")
	public String deleteStudent(@PathVariable(name = "id") int id) {

		service.delete(id);
		return "redirect:/";
	}

	// update student handler
	@PostMapping("/update-student/{id}")
	public String updateStudentForm(@PathVariable(name = "id") int id, Model model) {

		model.addAttribute("titel", "update student");

		Student student = service.get(id);

		model.addAttribute("student", student);

		System.out.println("STD" + student);

		return "updatestudent";
	}

	// update handler student

	@RequestMapping(value = "/process/{id}", method = RequestMethod.POST)
	public String updateStudentHandler(@ModelAttribute Student student, Model model ,
			@PathVariable(name = "id") int id) {

		Student stud = this.studentRepository.findById(id).get();

		stud.setStudentName(student.getStudentName());
		stud.setCourse(student.getCourse());
		stud.setFee(student.getFee());
		
		studentRepository.save(stud);

		return "redirect:/";
	}

}
