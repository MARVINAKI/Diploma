package com.example.diploma.model;

import lombok.Data;
import lombok.SneakyThrows;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "media_type")
	private String mediaType;
	@Column(name = "image")
	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	private byte[] image;

	@SneakyThrows
	public static Image convertToImageFromMultipartFile(MultipartFile multipartFile) {
		Image image = new Image();
		image.setMediaType(multipartFile.getContentType());
		image.setImage(multipartFile.getBytes());
		return image;
	}
}
