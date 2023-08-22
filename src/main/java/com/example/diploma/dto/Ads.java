package com.example.diploma.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {

	private Integer count;
	private List<Ad> results;
}
