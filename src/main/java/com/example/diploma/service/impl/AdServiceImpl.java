package com.example.diploma.service.impl;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.Ads;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

	private final AdRepository adRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public boolean adExists(Integer id) {
		return adRepository.findById(id).isPresent();
	}

	@Override
	@Transactional
	public Ads getAllAds() {
		List<AdDTO> adDTOList = adRepository.findAll().stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return Ads.convertListToAds(adDTOList);
	}

	@Override
	@Transactional
	public Ads getAllAuthorsAds() {
		User author = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		List<AdDTO> adDTOList = adRepository.findAllByAuthor_Id(author.getId()).stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return Ads.convertListToAds(adDTOList);
	}

	@Override
	@Transactional
	public Optional<Ad> getAd(Integer id) {
		return adRepository.findById(id);
	}

	@Override
	public Optional<Image> getImage(Integer id) {
		return adRepository.findImageById(id);
	}

	@Override
	@Transactional
	public AdDTO createNewAd(AdDTO adDTO) {
		User author = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		adRepository.save(Ad.convertAdDtoToAd(author, adDTO));
		return adDTO;
	}

	@Override
	@Transactional
	public AdDTO updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd) {
		Ad ad = adRepository.findById(id).orElseThrow();
		Ad.convertOnAdUpdate(ad, createOrUpdateAd);
		return Ad.convertAdToAdDTO(ad);
	}

	@SneakyThrows
	@Override
	@Transactional
	public boolean updateImage(Integer id, MultipartFile image) {
		if (!image.isEmpty() && adExists(id)) {
			Ad ad = adRepository.findById(id).orElseThrow();
			Image imageOfAd = adRepository.findImage(id).orElse(new Image());
			imageOfAd.setMediaType(image.getContentType());
			imageOfAd.setImage(image.getBytes());
			ad.setImage(imageOfAd);
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean deleteAd(Integer id) {

		if (adExists(id)) {
			adRepository.deleteById(id);
			return true;
		}
		return false;
	}

	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
