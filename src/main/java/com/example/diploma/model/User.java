package com.example.diploma.model;

import com.example.diploma.constant.Role;
import com.example.diploma.dto.RegisterDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.dto.UserDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@RequiredArgsConstructor
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
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column(name = "registration_date")
	private LocalDateTime registrationDate = LocalDateTime.now();
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "image_id")
	private Image image;
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
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
		userDTO.setImage("/users/image/" + user.getImage().getId());
		userDTO.setRegistrationDate(user.getRegistrationDate());
		userDTO.setNumberOfComments(user.getComments().size());
		userDTO.setNumberOfAds(user.getAds().size());
		return userDTO;
	}

	public static User convertRegisterToUser(RegisterDTO registerDTO) {
		User user = new User();
		user.setEmail(registerDTO.getUsername());
		user.setFirstName(registerDTO.getFirstName());
		user.setLastName(registerDTO.getLastName());
		user.setPhone(registerDTO.getPhone());
		user.setRole(registerDTO.getRole());
		user.setUsername(registerDTO.getUsername().toLowerCase());
		user.setPassword(registerDTO.getPassword());
		return user;
	}

	public static void convertOnUserUpdate(User user, UpdateUserDTO updateUserDTO) {
		user.setFirstName(updateUserDTO.getFirstName());
		user.setLastName(updateUserDTO.getLastName());
		user.setPhone(updateUserDTO.getPhone());
	}

	@Column(name = "account_non_expired")
	public boolean isAccountNonExpired = true;

	@Column(name = "account_non_locked")
	private boolean isAccountNonLocked = true;

	@Column(name = "credentials_non_expired")
	public boolean isCredentialsNonExpired = true;

	@Column(name = "enabled")
	public boolean isEnabled = true;
}

