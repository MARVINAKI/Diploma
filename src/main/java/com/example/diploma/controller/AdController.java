package com.example.diploma.controller;

import com.example.diploma.dto.Ad;
import com.example.diploma.dto.Ads;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.dto.ExtendedAd;
import com.example.diploma.service.AdService;
import com.example.diploma.service.AdsService;
import com.example.diploma.service.CreateOrUpdateAdService;
import com.example.diploma.service.ExtendedAdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

	private final AdService adService;
	private final AdsService adsService;
	private final ExtendedAdService extendedAdService;
	private final CreateOrUpdateAdService createOrUpdateAdService;

	@GetMapping
	public ResponseEntity<Ads> findAllAds() {
		return ResponseEntity.ok().body(new Ads());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExtendedAd> findAdById(@PathVariable Integer id) {
		return ResponseEntity.ok().body(new ExtendedAd());
	}

	@GetMapping("/me")
	public ResponseEntity<Ads> findAdsByAuthorizedUser() {
		Integer authorIdForService = 1;
		return ResponseEntity.ok().body(new Ads());
	}

	@PostMapping
	public ResponseEntity<Ad> addNewAd(@RequestBody Ad ad) {
		return ResponseEntity.ok().body(ad);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CreateOrUpdateAd> updateAdInformation(@PathVariable Integer id, @RequestBody CreateOrUpdateAd ad) {
		return ResponseEntity.ok().body(ad);
	}

	@PatchMapping("/{id}/image")
	public ResponseEntity<String> updateImageOfAd(@PathVariable Integer id, @RequestBody String image) {
		return ResponseEntity.ok().body(image);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteAdById(@PathVariable Integer id) {
		return ResponseEntity.ok().body(false);
	}
}
