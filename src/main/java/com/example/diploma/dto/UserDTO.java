package com.example.diploma.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

	private Integer id;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private String role;
	private String image;
	private LocalDateTime registrationDate;
	private Integer numberOfComments;
	private Integer numberOfAds;
}
