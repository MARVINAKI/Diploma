package com.example.diploma.repository;

import com.example.diploma.model.Ad;
import com.example.diploma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

	List<Ad> findAllByAuthor(User user);

}
