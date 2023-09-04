package com.example.diploma.controller;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> findUserImage(@PathVariable String id) {
		Optional<Image> image = userService.getImage(Integer.valueOf(id));
		return image.map(value -> ResponseEntity.ok(value.getImage())).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> findInfoAboutAuthorizedUser() {
		return ResponseEntity.ok().body(userService.getAuthorizedUser());
	}

	@PostMapping("/set_password")
	public ResponseEntity<Boolean> updateUserPassword(@RequestBody NewPassword newPassword) {
		return ResponseEntity.ok().body(userService.updatePassword(newPassword));
	}

	@PatchMapping("/me")
	public ResponseEntity<UpdateUser> updateInfoAboutAuthorizedUser(@RequestBody UpdateUser updateUser) {
		UpdateUser updateUserResponse = userService.updateUserInfo(updateUser);
		return updateUserResponse != null ? ResponseEntity.ok(updateUserResponse) : ResponseEntity.badRequest().build();
	}

	@PatchMapping("/me/image")
	public ResponseEntity<Boolean> updateImageAuthorizedUser(@RequestParam MultipartFile image) {
		return userService.updateImage(image) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
	}
}
