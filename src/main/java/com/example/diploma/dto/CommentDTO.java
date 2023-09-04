package com.example.diploma.dto;

import com.example.diploma.model.Image;
import lombok.Data;

@Data
public class CommentDTO {

	private Integer author;
	private Image authorImage;
	private String authorFirstName;
	private Integer createdAt;		//дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
	private Integer pk;
	private String text;
}
