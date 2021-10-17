package com.jk.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	private String studentName;
	private String course;
	private int fee;

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Student(int id, String studentName, String course, int fee) {
		super();
		this.id = id;
		this.studentName = studentName;
		this.course = course;
		this.fee = fee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", studentName=" + studentName + ", course=" + course + ", fee=" + fee + "]";
	}
	
	
	

}
