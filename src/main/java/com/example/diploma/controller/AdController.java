package com.example.diploma.controller;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.Ads;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.dto.ExtendedAd;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import com.example.diploma.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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


	@GetMapping
	public ResponseEntity<Ads> findAllAds() {
		return ResponseEntity.ok(adService.getAllAds());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExtendedAd> findAdById(@PathVariable Integer id) {
		Optional<Ad> ad = adService.getAd(id);
		return ad.map(value -> ResponseEntity.ok(Ad.convertAdToExtendedAd(value))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> findAdImage(@PathVariable String id) {
		Optional<Image> image = adService.getImage(Integer.valueOf(id));
		return image.map(value -> ResponseEntity.ok(value.getImage())).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/me")
	public ResponseEntity<Ads> findAdsByAuthorizedUser() {
		return ResponseEntity.ok().body(adService.getAllAuthorsAds());
	}

	@PostMapping("/newAd")
	public ResponseEntity<AdDTO> addNewAd(@RequestBody AdDTO adDTO) {
		AdDTO adDTOResponse = adService.createNewAd(adDTO);
		return adDTOResponse != null ? ResponseEntity.ok(adDTOResponse) : ResponseEntity.badRequest().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AdDTO> updateAdInformation(@PathVariable Integer id, @RequestBody CreateOrUpdateAd ad) {
		AdDTO adDTOResponse = adService.updateAd(id, ad);
		return adDTOResponse != null ? ResponseEntity.ok(adDTOResponse) : ResponseEntity.badRequest().build();
	}

	@PatchMapping("/{id}/image")
	public ResponseEntity<Boolean> updateImageOfAd(@PathVariable Integer id, @RequestParam MultipartFile image) {
		return adService.updateImage(id, image) ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteAdById(@PathVariable Integer id) {
		return adService.deleteAd(id) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
	}
}
