package com.example.diploma.model;

import com.example.diploma.dto.Register;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "email")
	private String email;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "phone")
	private String phone;
	@Column(name = "user_role")
	private String role;
	@Column(name = "image")
	private String image;
	@Column(name = "registration_date")
	private LocalDateTime registrationDate = LocalDateTime.now();
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ad> ads;

	public static UserDTO convertUserToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setEmail(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setPhone(user.getPhone());
		userDTO.setRole(user.getRole());
		userDTO.setImage(user.getImage());
		userDTO.setRegistrationDate(user.getRegistrationDate());
		userDTO.setNumberOfComments(user.getComments().size());
		userDTO.setNumberOfAds(user.getAds().size());
		return userDTO;
	}

	public static User convertRegisterToUser(Register register) {
		User user = new User();
		user.setEmail(register.getUsername());
		user.setFirstName(register.getFirstName());
		user.setLastName(register.getLastName());
		user.setPhone(register.getPhone());
		user.setRole(register.getRole().toString());
		user.setUsername(register.getUsername());
		user.setPassword(register.getPassword());
		return user;
	}

	public static User convertOnUserUpdate(User user, UpdateUser updateUser) {
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		user.setPhone(user.getPhone());
		return user;
	}
}

