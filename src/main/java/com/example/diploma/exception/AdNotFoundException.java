package com.example.diploma.exception;

public class AdNotFoundException extends RuntimeException{

	public AdNotFoundException() {
		super("Ad not found");
	}
}
