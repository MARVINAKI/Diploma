package com.example.diploma.dto;

import lombok.Data;

@Data
public class NewPassword {

	private String currentPassword; //		minLength: 8	maxLength: 16
	private String newPassword; //		minLength: 8	maxLength: 16
}
