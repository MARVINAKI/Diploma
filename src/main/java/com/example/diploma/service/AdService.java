package com.example.diploma.service;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.AdsDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.model.Ad;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AdService {

	boolean adExists(Integer id);

	AdsDTO getAllAds();

	AdsDTO getAllAuthorsAds();

	Optional<Ad> getAd(Integer id);

	AdDTO createAd(CreateOrUpdateAdDTO createOrUpdateAdDTO, MultipartFile image);

	AdDTO updateAd(Integer id, CreateOrUpdateAdDTO createOrUpdateAdDTO);

	boolean updateImage(Integer id, MultipartFile image);

	boolean deleteAd(Integer id);
}
