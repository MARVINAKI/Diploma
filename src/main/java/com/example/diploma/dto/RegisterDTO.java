package com.example.diploma.dto;

import com.example.diploma.constant.Role;
import lombok.Data;

@Data
public class RegisterDTO {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String phone;
	private Role role;
}
