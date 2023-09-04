package com.example.diploma.dto;

import com.example.diploma.model.Image;
import lombok.Data;

@Data
public class ExtendedAd {

	private Integer pk;
	private String authorFirstName;
	private String authorLastName;
	private String description;
	private String email;
	private Image image;
	private String phone;
	private Integer price;
	private String title;
}
