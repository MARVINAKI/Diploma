package com.example.diploma.dto;

import lombok.Data;

@Data
public class Login {

	private String username; //		minLength: 4	maxLength: 32
	private String password; //		minLength: 8	maxLength: 16
}

