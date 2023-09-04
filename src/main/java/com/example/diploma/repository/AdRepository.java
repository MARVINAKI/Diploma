package com.example.diploma.repository;

import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

	List<Ad> findAllByAuthor_Id(Integer id);

	@Query(value = "select ad.image from Ad ad where ad.id = ?1")
	Optional<Image> findImage(Integer id);

	@Query(value = "select ad.image from Ad ad where ad.id = ?1")
	Optional<Image> findImageById(Integer id);
}
