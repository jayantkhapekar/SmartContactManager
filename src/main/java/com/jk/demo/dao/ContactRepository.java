package com.jk.demo.dao;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jk.demo.entities.Contact;
import com.jk.demo.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// pagination

	@Query("from Contact as c where c.user.id =:userid")
	public Page<Contact> findContactsByUser(@Param("userid") int userId , Pageable pageable);
	
	// Search

	public List<Contact> findByNameContainingAndUser(String name , User user);
	

}
