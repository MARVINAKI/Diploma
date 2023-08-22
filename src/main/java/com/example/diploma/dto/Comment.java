package com.example.diploma.dto;

import lombok.Data;

@Data
public class Comment {

	private Integer author;
	private String authorImage;
	private String authorFirstName;
	private Integer createAt;
	private Integer pk;
	private String text;
}
