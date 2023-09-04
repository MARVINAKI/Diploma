package com.example.diploma.model;

import lombok.Data;

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
	private byte[] image;

}
