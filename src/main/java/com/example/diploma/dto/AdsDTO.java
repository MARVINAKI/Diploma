package com.example.diploma.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdsDTO {

	private Integer count;
	private List<AdDTO> results;

	public static AdsDTO convertListToAds(List<AdDTO> adDTOList) {
		AdsDTO adsDTO = new AdsDTO();
		adsDTO.setCount(adDTOList.size());
		adsDTO.setResults(adDTOList);
		return adsDTO;
	}
}
