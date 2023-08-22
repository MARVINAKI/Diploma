package com.example.diploma.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAd {

	private String title; //	minLength: 4	maxLength: 32
	private Integer price; //	minimum: 0	maximum: 10000000
	private String description; //		minLength: 8	maxLength: 64
}
