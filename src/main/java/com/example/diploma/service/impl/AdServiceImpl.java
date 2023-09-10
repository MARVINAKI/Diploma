package com.example.diploma.service.impl;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.AdsDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.ImageRepository;
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
	private final ImageRepository imageRepository;

	@Override
	@Transactional
	public boolean adExists(Integer id) {
		return adRepository.findById(id).isPresent();
	}

	@Override
	@Transactional
	public AdsDTO getAllAds() {
		List<AdDTO> adDTOList = adRepository.findAll().stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return AdsDTO.convertListToAds(adDTOList);
	}

	@Override
	@Transactional
	public AdsDTO getAllAuthorsAds() {
		User author = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		List<AdDTO> adDTOList = adRepository.findAllByAuthor(author).stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return AdsDTO.convertListToAds(adDTOList);
	}

	@Override
	@Transactional
	public Optional<Ad> getAd(Integer id) {
		return adRepository.findById(id);
	}

	@Override
	@Transactional
	public AdDTO createAd(CreateOrUpdateAdDTO createOrUpdateAdDTO, MultipartFile image) {
		User user = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		Image imageOfAd = Image.convertToImageMultiPartFile(image);
		imageRepository.saveAndFlush(imageOfAd);
		Ad ad = Ad.convertToAdOnCreate(user, createOrUpdateAdDTO, imageOfAd);
		adRepository.save(ad);
		return Ad.convertAdToAdDTO(ad);
	}

	@Override
	@Transactional
	public AdDTO updateAd(Integer id, CreateOrUpdateAdDTO createOrUpdateAdDTO) {
		Ad ad = adRepository.findById(id).orElseThrow();
		Ad.convertToAdOnUpdate(ad, createOrUpdateAdDTO);
		adRepository.saveAndFlush(ad);
		return Ad.convertAdToAdDTO(ad);
	}

	@SneakyThrows
	@Override
	@Transactional
	public boolean updateImage(Integer id, MultipartFile image) {
		if (!image.isEmpty()) {
			Ad ad = adRepository.findById(id).orElseThrow();
			Image imageOfAd = adRepository.findById(id).orElseThrow().getImage();
			imageOfAd.setMediaType(image.getContentType());
			imageOfAd.setImage(image.getBytes());
			imageRepository.saveAndFlush(imageOfAd);
			ad.setImage(imageOfAd);
			adRepository.saveAndFlush(ad);
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
