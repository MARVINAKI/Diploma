package com.example.diploma.dto;

import lombok.Data;

import java.util.List;

@Data
public class Ads {

	private Integer count;
	private List<AdDTO> results;

	public static Ads convertListToAds(List<AdDTO> adDTOList) {
		Ads ads = new Ads();
		ads.setCount(adDTOList.size());
		ads.setResults(adDTOList);
		return ads;
	}
}
