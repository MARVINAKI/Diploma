package com.example.diploma.service;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.Ads;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.model.Ad;

import java.util.Optional;

public interface AdService {

	Ads getAllAds();

	Ads getAllAuthorsAds();

	Optional<Ad> getAd(Integer id);

	AdDTO createNewAd(AdDTO adDTO);

	AdDTO updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd);

	boolean updateImage(Integer id, String image);

	boolean deleteAd(Integer id);
}
