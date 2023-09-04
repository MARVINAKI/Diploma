package com.example.diploma.service;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.Ads;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AdService {

	boolean adExists(Integer id);

	Ads getAllAds();

	Ads getAllAuthorsAds();

	Optional<Ad> getAd(Integer id);

	Optional<Image> getImage(Integer id);

	AdDTO createNewAd(AdDTO adDTO);

	AdDTO updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd);

	boolean updateImage(Integer id, MultipartFile image);

	boolean deleteAd(Integer id);
}
