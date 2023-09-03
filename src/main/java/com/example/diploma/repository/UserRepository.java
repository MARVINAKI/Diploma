package com.example.diploma.repository;

import com.example.diploma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findUserByUsername(String username);

	@Query(value = "update User u set u.password = :newPassword where u.username = :username")
	void updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);

}
