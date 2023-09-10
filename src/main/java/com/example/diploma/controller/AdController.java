package com.example.diploma.controller;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.AdsDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.dto.ExtendedAdDTO;
import com.example.diploma.model.Ad;
import com.example.diploma.service.AdService;
import com.example.diploma.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

	private final AdService adService;
	private final ImageService imageService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdsDTO> getAllAds() {
		return ResponseEntity.ok(adService.getAllAds());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<ExtendedAdDTO> getAds(@PathVariable Integer id) {
		Optional<Ad> ad = adService.getAd(id);
		return ad.map(value -> ResponseEntity.ok(Ad.convertAdToExtendedAd(value))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/image/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) {
		return ResponseEntity.ok(imageService.getImageById(id));
	}

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdsDTO> getAdsMe() {
		return ResponseEntity.ok().body(adService.getAllAuthorsAds());
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdDTO> addAd(@RequestPart CreateOrUpdateAdDTO properties, @RequestPart MultipartFile image) {
		AdDTO adDTOResponse = adService.createAd(properties, image);
		return adDTOResponse != null ? ResponseEntity.ok(adDTOResponse) : ResponseEntity.badRequest().build();
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<AdDTO> updateAds(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDTO ad) {
		AdDTO adDTOResponse = adService.updateAd(id, ad);
		return adDTOResponse != null ? ResponseEntity.ok(adDTOResponse) : ResponseEntity.badRequest().build();
	}

	@PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Boolean> updateImage(@PathVariable Integer id, @RequestParam MultipartFile image) {
		return adService.updateImage(id, image) ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Boolean> removeAd(@PathVariable Integer id) {
		return adService.deleteAd(id) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
	}
}
