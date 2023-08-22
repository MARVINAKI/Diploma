package com.example.diploma.controller;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.User;
import com.example.diploma.service.NewPasswordService;
import com.example.diploma.service.UpdateUserService;
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
	private final NewPasswordService newPasswordService;
	private final UpdateUserService updateUserService;

	@GetMapping("/me")
	public ResponseEntity<User> findInfoAboutAuthorizedUser() {
		Integer userIdForService = 1;
		return ResponseEntity.ok().body(new User());
	}

	@PostMapping("/set_password")
	public ResponseEntity<Boolean> updateUserPassword(@RequestBody NewPassword newPassword) {
		return ResponseEntity.ok().body(false);
	}

	@PatchMapping("/me")
	public ResponseEntity<UpdateUser> updateInfoAboutAuthorizedUser(@RequestBody UpdateUser updateUser) {
		return ResponseEntity.ok().body(updateUser);
	}

	@PatchMapping("/me/image")
	public ResponseEntity<String> updateImageAuthorizedUser(@RequestBody String image) {
		return ResponseEntity.ok().body(image);
	}
}
