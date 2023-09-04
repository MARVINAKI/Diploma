package com.example.diploma.dto;

import com.example.diploma.model.Image;
import lombok.Data;

@Data
public class AdDTO {

	private Integer author;
	private Image image;
	private Integer pk;
	private Integer price;
	private String title;
	private String description;
}
