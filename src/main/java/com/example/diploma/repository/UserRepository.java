package com.example.diploma.repository;

import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findUserByUsername(String username);

	@Query(value = "select u.image from User u where u.username = ?1")
	Optional<Image> findImage(String username);

	@Query(value = "select u.image from User u where u.id=?1")
	Optional<Image> findImageById(Integer id);

	@Query(value = "update User u set u.password = ?1 where u.username = ?2")
	void updatePassword(String newPassword, String username);

}
