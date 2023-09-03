package com.example.diploma.service.impl;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.Ads;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.model.Ad;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Ads getAllAds() {
		List<AdDTO> adDTOList = adRepository.findAll().stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return Ads.convertListToAds(adDTOList);
	}

	@Override
	@Transactional
	public Ads getAllAuthorsAds() {
		String username = getUsernameOfAuthorizedUser();
		User author = userRepository.findUserByUsername(username).orElseThrow();
		List<AdDTO> adDTOList = adRepository.findAllByAuthor_IdLike(author.getId()).stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return Ads.convertListToAds(adDTOList);
	}

	@Override
	@Transactional
	public Optional<Ad> getAd(Integer id) {
		return adRepository.findById(id);
	}

	@Override
	@Transactional
	public AdDTO createNewAd(AdDTO adDTO) {
		AdDTO adDTOResponse = new AdDTO();
		String username = getUsernameOfAuthorizedUser();
		User author = userRepository.findUserByUsername(username).orElseThrow();
		if (author.getId().equals(adDTO.getAuthor())) {
			Ad ad = Ad.convertAdDtoToAd(author, adDTO);
			adRepository.save(ad);
			adDTOResponse = adDTO;
		}
		return adDTOResponse;
	}

	@Override
	@Transactional
	public AdDTO updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd) {
		AdDTO adDTOResponse = new AdDTO();
		Optional<Ad> ad = adRepository.findById(id);
		if (ad.isPresent()) {
			Ad convertAd = Ad.convertOnAdUpdate(ad.get(), createOrUpdateAd);
			adRepository.save(convertAd);
			adDTOResponse = Ad.convertAdToAdDTO(convertAd);
		}
		return adDTOResponse;
	}

	@Override
	@Transactional
	public boolean updateImage(Integer id, String image) {
		Optional<Ad> ad = adRepository.findById(id);
		if (ad.isPresent()) {
			ad.get().setImage(image);
			adRepository.save(ad.get());
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean deleteAd(Integer id) {
		Optional<Ad> ad = adRepository.findById(id);
		if (ad.isPresent()) {
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
