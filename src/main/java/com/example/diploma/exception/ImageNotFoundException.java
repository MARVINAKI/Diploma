package com.example.diploma.exception;

public class ImageNotFoundException extends RuntimeException {

	public ImageNotFoundException() {
		super("Image not found");
	}
}
