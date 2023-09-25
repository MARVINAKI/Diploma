package com.example.diploma.controller;

import com.example.diploma.dto.NewPasswordDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.User;
import com.example.diploma.service.ImageService;
import com.example.diploma.service.UserService;
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
 * Класс контроллер для работы с классом пользователи и связанными<p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final ImageService imageService;

	/**
	 * Получение изображение конкретного пользователя<p>
	 * {@link ImageService#getImageById(Integer)}<p>
	 *
	 * @param id идентификационный номер изображения<p>
	 * @return массив байт изображения пользователя<p>
	 */
	@Operation(summary = "Получение изображения пользователя",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					)
			})
	@GetMapping(value = "/image/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
		return ResponseEntity.ok(imageService.getImageById(id));
	}

	/**
	 * Получение информации об авторизованном пользователе<p>
	 * {@link UserService#getAuthorizedUser()}<p>
	 *
	 * @return {@link UserDTO} DTO модель авторизованного пользователя<p>
	 */
	@Operation(summary = "Получение информации об авторизованном пользователе",
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
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<UserDTO> getUser() {
		Optional<User> user = userService.getAuthorizedUser();
		UserDTO userDTO = user.map(User::convertUserToUserDTO).orElseGet(UserDTO::new);
		return ResponseEntity.ok(userDTO);
	}

	/**
	 * Изменение пароля авторизованного пользователя<p>
	 * {@link UserService#updatePassword(NewPasswordDTO)}<p>
	 *
	 * @param newPasswordDTO {@link NewPasswordDTO} модель с потенциально актуальным паролем и новым<p>
	 * @return <b>true/false</b>
	 */
	@Operation(summary = "Обновление пароля",
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
					)
			})
	@PostMapping("/set_password")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Boolean> setPassword(@RequestBody NewPasswordDTO newPasswordDTO) {
		return ResponseEntity.ok().body(userService.updatePassword(newPasswordDTO));
	}

	/**
	 * Обновление информации об авторизованном пользователе<p>
	 * {@link UserService#updateUserInfo(UpdateUserDTO)}<p>
	 *
	 * @param updateUserDTO {@link UpdateUserDTO} модель с данными для обновления<p>
	 * @return {@link UpdateUserDTO} ответ в виде DTO модели обновленных данных<p>
	 */
	@Operation(summary = "Обновление информации об авторизованном пользователе",
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
	@PatchMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
		UpdateUserDTO updateUserDTOResponse = userService.updateUserInfo(updateUserDTO);
		return updateUserDTOResponse != null ? ResponseEntity.ok(updateUserDTOResponse) : ResponseEntity.badRequest().build();
	}

	/**
	 * Обновление изображения авторизованного пользователя<p>
	 * {@link UserService#updateImage(MultipartFile)}<p>
	 *
	 * @param image {@link MultipartFile} представление загруженного файла изображения<p>
	 * @return <b>true/false</b><p>
	 */
	@Operation(summary = "Обновление аватара авторизованного пользователя",
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
	@PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Boolean> updateUserImage(@RequestParam MultipartFile image) {
		return userService.updateImage(image) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
	}
}
