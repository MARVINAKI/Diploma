package com.example.diploma.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

	private Integer author;
	private String authorImage;
	private String authorFirstName;
	private LocalDateTime createdAt;
	private Integer pk;
	private String text;
}
