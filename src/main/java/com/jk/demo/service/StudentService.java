package com.jk.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jk.demo.dao.StudentRepository;
import com.jk.demo.entities.Student;


@Service
public class StudentService {
	
	@Autowired
	
	private StudentRepository studentRepository;
	
	// add student
	public void save(Student student) {
		
		studentRepository.save(student);
	}
	
	
	// show list 
	
	public List<Student> listAll(){
		
		
		return studentRepository.findAll();
	}
	
	// delete student records
	
	public void delete(int id) {
		studentRepository.deleteById(id);
	}
	
	// update student records
	
	public Student get(int id) {
		
		return studentRepository.findById(id).get();
		
	}
	

}
