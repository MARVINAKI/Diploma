package com.example.diploma.controller;

import com.example.diploma.dto.LoginDTO;
import com.example.diploma.dto.RegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.diploma.service.AuthService;

/**
 * Класс контроллер авторизации и регистрации пользователя
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * Авторизация пользователя<p>
	 * {@link AuthService#login(String, String)}<p>
	 *
	 * @param loginDTO {@link LoginDTO} DTO модель с данными пользователя<p>
	 * @return успешная авторизация или ошибка входа<p>
	 */
	@Operation(summary = "Авторизация пользователя",
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
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
		if (authService.login(loginDTO.getUsername(), loginDTO.getPassword())) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	/**
	 * Регистрация нового пользователя<p>
	 * {@link AuthService#register(RegisterDTO)}<p>
	 *
	 * @param registerDTO {@link RegisterDTO} DTO модель с данными пользователя для регистрации<p>
	 * @return успешная регистрация или ошибка ввода данных запроса<p>
	 */
	@Operation(summary = "Регистрация пользователя")
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
		if (authService.register(registerDTO)) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
