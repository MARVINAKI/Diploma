package com.example.diploma.controller;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

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
	public ResponseEntity<Boolean> updateImageAuthorizedUser(@RequestBody String image) {
		return ResponseEntity.ok().body(userService.updateImage(image));
	}
}
