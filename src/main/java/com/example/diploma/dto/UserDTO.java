package com.example.diploma.dto;

import com.example.diploma.constant.Role;
import com.example.diploma.model.Image;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

	private Integer id;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private Role role;
	private Image image;
	private LocalDateTime registrationDate;
	private Integer numberOfComments;
	private Integer numberOfAds;
}
