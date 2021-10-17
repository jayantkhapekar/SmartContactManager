package com.jk.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jk.demo.entities.Student;

@Repository

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
