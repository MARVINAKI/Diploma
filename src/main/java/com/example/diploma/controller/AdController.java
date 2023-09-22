package com.example.diploma.controller;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.AdsDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.dto.ExtendedAdDTO;
import com.example.diploma.model.Ad;
import com.example.diploma.service.AdService;
import com.example.diploma.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Класс контроллер для работы с объявлениями и связанными классами<p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

	private final AdService adService;
	private final ImageService imageService;

	/**
	 * Поиск всех объявлений, всех пользователей<p>
	 * {@link AdService#getAllAds()}<p>
	 *
	 * @return искомые объявления<p>
	 */
	@Operation(summary = "Получение всех объявлений",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					)
			})
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdsDTO> getAllAds() {
		return ResponseEntity.ok(adService.getAllAds());
	}

	/**
	 * Поиск конкретного объявления по идентификационному номеру<p>
	 * {@link AdService#getAd(Integer)}<p>
	 *
	 * @param id идентификационный номер<p>
	 * @return {@link ExtendedAdDTO} искомый результат в виде DTO модели объявления<p>
	 */
	@Operation(summary = "Получение информации об объявлении",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<ExtendedAdDTO> getAds(@PathVariable Integer id) {
		Optional<Ad> ad = adService.getAd(id);
		return ad.map(value -> ResponseEntity.ok(Ad.convertAdToExtendedAd(value))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Получение изображения конкретного объявления<p>
	 * {@link ImageService#getImageById(Integer)}<p>
	 *
	 * @param id идентификационный номер объявления<p>
	 * @return массив байт изображения объявления<p>
	 */
	@Operation(summary = "Получение изображения объявления",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					)
			})
	@GetMapping(value = "/{id}/image", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) {

		return ResponseEntity.ok(imageService.getImageById(id));
	}

	/**
	 * Получение списка всех объявлений авторизованного пользователя<p>
	 * {@link AdService#getAllAuthorsAds()}<p>
	 *
	 * @return список объявлений<p>
	 */
	@Operation(summary = "Получение объявлений авторизованного пользователя",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					)
			})
	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdsDTO> getAdsMe() {
		return ResponseEntity.ok().body(adService.getAllAuthorsAds());
	}

	/**
	 * Добавление нового объявления авторизованным пользователем<p>
	 * {@link AdService#createAd(CreateOrUpdateAdDTO, MultipartFile)}<p>
	 *
	 * @param properties DTO модель с данными объявления<p>
	 * @param image      {@link MultipartFile} представление загруженного файла изображения<p>
	 * @return {@link AdDTO} созданное объявление<p>
	 */
	@Operation(summary = "Добавление объявления",
			responses = {
					@ApiResponse(
							responseCode = "201",
							description = "Created"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					)
			})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdDTO> addAd(@RequestPart CreateOrUpdateAdDTO properties, @RequestPart MultipartFile image) {
		AdDTO adDTOResponse = adService.createAd(properties, image);
		return adDTOResponse != null ? ResponseEntity.ok(adDTOResponse) : ResponseEntity.badRequest().build();
	}

	/**
	 * Поиск и обновление информации об объявлении<p>
	 * {@link AdService#updateAd(Integer, CreateOrUpdateAdDTO)}<p>
	 *
	 * @param id идентификационный номер объявления<p>
	 * @param ad {@link CreateOrUpdateAdDTO} DTO модель с данными для обновления<p>
	 * @return {@link AdDTO} обновленное объявление<p>
	 */
	@Operation(summary = "Обновление информации об объявлении",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "403",
							description = "Forbidden"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@PatchMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdDTO> updateAds(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDTO ad) {
		AdDTO adDTOResponse = adService.updateAd(id, ad);
		return adDTOResponse != null ? ResponseEntity.ok(adDTOResponse) : ResponseEntity.badRequest().build();
	}

	/**
	 * Поиск и обновление изображения объявления<p>
	 * {@link AdService#updateImage(Integer, MultipartFile)}<p>
	 *
	 * @param id    идентификационный номер объявления<p>
	 * @param image {@link MultipartFile} представление загруженного файла изображения<p>
	 * @return <b>true/false</b>
	 */
	@Operation(summary = "Обновление картинки объявления",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "403",
							description = "Forbidden"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Boolean> updateImage(@PathVariable Integer id, @RequestParam MultipartFile image) {
		return adService.updateImage(id, image) ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
	}

	/**
	 * Поиск и удаления объявления<p>
	 * {@link AdService#deleteAd(Integer)}<p>
	 *
	 * @param id идентификационный номер объявления<p>
	 * @return <b>true/false</b>
	 */
	@Operation(summary = "Удаление объявления",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "204",
							description = "No content"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "403",
							description = "Forbidden"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Boolean> removeAd(@PathVariable Integer id) {
		return adService.deleteAd(id) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
	}
}
