package com.example.diploma.controller;

import com.example.diploma.dto.NewPasswordDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.service.ImageService;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final ImageService imageService;

	@GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE})
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
		return ResponseEntity.ok(imageService.getImageById(id));
	}

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<UserDTO> getUser() {
		return ResponseEntity.ok().body(userService.getAuthorizedUser());
	}

	@PostMapping("/set_password")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Boolean> setPassword(@RequestBody NewPasswordDTO newPasswordDTO) {
		return ResponseEntity.ok().body(userService.updatePassword(newPasswordDTO));
	}

	@PatchMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
		UpdateUserDTO updateUserDTOResponse = userService.updateUserInfo(updateUserDTO);
		return updateUserDTOResponse != null ? ResponseEntity.ok(updateUserDTOResponse) : ResponseEntity.badRequest().build();
	}

	@PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Boolean> updateUserImage(@RequestParam MultipartFile image) {
		return userService.updateImage(image) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
	}
}
