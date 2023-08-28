package com.example.diploma.repository;

import com.example.diploma.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

	List<Ad> findAllByAuthor_IdLike(Integer id);
}
