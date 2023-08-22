package com.example.diploma.dto;

import lombok.Data;

@Data
public class CreateOrUpdateComment {

	private String text; //		minLength: 8	maxLength: 64
}
