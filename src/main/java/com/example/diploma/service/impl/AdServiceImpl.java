package com.example.diploma.service.impl;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.AdsDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.exception.AdNotFoundException;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.ImageRepository;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link AdService}<p>
 * Класс сервис {@link AdServiceImpl} предназначен для управления классом объявления {@link Ad} и связанными классами<p>
 */
@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

	private final AdRepository adRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;

	/**
	 * Метод проверки существования объекта в БД по идентификационному номеру<p>
	 * {@link AdRepository#findById(Object)}<p>
	 *
	 * @param id - идентификационный номер объекта<p>
	 * @return <b>true/false</b><p>
	 */
	@Override
	@Transactional
	public boolean adExists(Integer id) {
		return adRepository.findById(id).isPresent();
	}

	/**
	 * Метод получение всех имеющихся объявлений<p>
	 * {@link AdRepository#findAll()}<p>
	 *
	 * @return список объявлений в формате {@link AdsDTO}<p>
	 */
	@Override
	@Transactional
	public AdsDTO getAllAds() {
		List<AdDTO> adDTOList = adRepository.findAll().stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return AdsDTO.convertListToAds(adDTOList);
	}

	/**
	 * Метод получения всех имеющихся объявлений у авторизованного пользователя<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 * {@link AdRepository#findAllByAuthor(User)}<p>
	 *
	 * @return список объявлений в формате {@link AdsDTO}<p>
	 * @throws UsernameNotFoundException при ошибке поиска авторизованного пользователя<p>
	 */
	@Override
	@Transactional
	public AdsDTO getAllAuthorsAds() {
		String username = getUsernameOfAuthorizedUser();
		User author = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		List<AdDTO> adDTOList = adRepository.findAllByAuthor(author).stream().map(Ad::convertAdToAdDTO).collect(Collectors.toList());
		return AdsDTO.convertListToAds(adDTOList);
	}

	/**
	 * Метод поиска конкретного объявления по идентификационному номеру<p>
	 * {@link JpaRepository#findById(Object)}<p>
	 *
	 * @param id идентификационный номер объекта<p>
	 * @return {@link Optional} объект искомого объявления<p>
	 */
	@Override
	@Transactional
	public Optional<Ad> getAd(Integer id) {
		return adRepository.findById(id);
	}

	/**
	 * Метод создания нового объвления с проверкой авторизации<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 * {@link ImageRepository#saveAndFlush(Object)}<p>
	 * {@link AdRepository#save(Object)}<p>
	 *
	 * @param createOrUpdateAdDTO DTO Class {@link CreateOrUpdateAdDTO} содержит данные для создания объекта {@link Ad}<p>
	 * @param image               {@link MultipartFile} представление загруженного файла, полученного в запросе<p>
	 * @return {@link AdDTO} Object<p>
	 * @throws UsernameNotFoundException при ошибки поиска авторизованного пользователя<p>
	 */
	@Override
	@Transactional
	public AdDTO createAd(CreateOrUpdateAdDTO createOrUpdateAdDTO, MultipartFile image) {
		String username = getUsernameOfAuthorizedUser();
		User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		Image imageOfAd = Image.convertToImageFromMultipartFile(image);
		imageRepository.saveAndFlush(imageOfAd);
		Ad ad = Ad.convertToAdOnCreate(user, createOrUpdateAdDTO, imageOfAd);
		adRepository.save(ad);
		return Ad.convertAdToAdDTO(ad);
	}

	/**
	 * Метод обновления данных у конкретного объявления<p>
	 * {@link AdRepository#findById(Object)}<p>
	 * {@link AdRepository#saveAndFlush(Object)}<p>
	 *
	 * @param id                  идентификационный номер объявления<p>
	 * @param createOrUpdateAdDTO DTO Class {@link CreateOrUpdateAdDTO} содержит данные для обновления объекта {@link Ad}<p>
	 * @return {@link AdDTO} Object<p>
	 */
	@Override
	@Transactional
	public AdDTO updateAd(Integer id, CreateOrUpdateAdDTO createOrUpdateAdDTO) {
		Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
		Ad.convertToAdOnUpdate(ad, createOrUpdateAdDTO);
		adRepository.saveAndFlush(ad);
		return Ad.convertAdToAdDTO(ad);
	}

	/**
	 * Метод обновления картинки у конкретного объявления<p>
	 * {@link AdRepository#findById(Object)}<p>
	 * {@link ImageRepository#saveAndFlush(Object)}<p>
	 * {@link AdRepository#saveAndFlush(Object)}<p>
	 *
	 * @param id    идентификационный номер объявления<p>
	 * @param image {@link MultipartFile} представление загруженного файла, полученного в запросе<p>
	 * @return <b>true/false</b><p>
	 */
	@SneakyThrows
	@Override
	@Transactional
	public boolean updateImage(Integer id, MultipartFile image) {
		if (!image.isEmpty()) {
			Ad ad = adRepository.findById(id).orElseThrow(AdNotFoundException::new);
			Image imageOfAd = ad.getImage();
			imageOfAd.setMediaType(image.getContentType());
			imageOfAd.setImage(image.getBytes());
			imageRepository.saveAndFlush(imageOfAd);
			ad.setImage(imageOfAd);
			adRepository.saveAndFlush(ad);
			return true;
		}
		return false;
	}

	/**
	 * Метод удаления конкретного объявления<p>
	 * {@link AdRepository#findById(Object)}<p>
	 * {@link AdRepository#deleteById(Object)}<p>
	 *
	 * @param id идентификационный номер объявления<p>
	 * @return <b>true/false</b><p>
	 */
	@Override
	@Transactional
	public boolean deleteAd(Integer id) {
		if (adRepository.findById(id).isPresent()) {
			adRepository.deleteById(id);
			return true;
		}
		return false;
	}

	/**
	 * Проверка авторизации пользователя<p>
	 * {@link Authentication}<p>
	 * {@link SecurityContextHolder#getContext()}<p>
	 *
	 * @return {@link String} имя авторизованного пользователя<p>
	 * @throws org.springframework.security.core.AuthenticationException при ошибки в проверке авторизации<p>
	 */
	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
