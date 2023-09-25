package com.example.diploma.service.impl;

import com.example.diploma.dto.NewPasswordDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.ImageRepository;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Реализация интерфейса {@link UserService}<p>
 * Класс сервис {@link UserServiceImpl} предназначен для управления классом пользователь {@link User} и связанными классами<p>
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final PasswordEncoder encoder;

	/**
	 * Метод создания нового пользователя с пустым аватаром<p>
	 * {@link ImageRepository#saveAndFlush(Object)}<p>
	 * {@link UserRepository#save(Object)}<p>
	 *
	 * @param user {@link User} модель для создания пользователя<p>
	 */
	@Override
	@Transactional
	public void createUser(User user) {
		Image emptyImage = new Image();
		imageRepository.saveAndFlush(emptyImage);
		user.setImage(emptyImage);
		userRepository.save(user);
	}

	/**
	 * Метод проверки существования объекта в БД по имени пользователя<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 *
	 * @param username имя пользователя<p>
	 * @return <b>true.false</b>
	 */
	@Override
	@Transactional
	public boolean userExists(String username) {
		return userRepository.findUserByUsername(username.toLowerCase()).isPresent();
	}

	/**
	 * Метод получения конкретного пользователя<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 *
	 * @param username имя пользователя<p>
	 * @return {@link Optional} объект искомого пользователя<p>
	 */
	@Override
	@Transactional
	public Optional<User> getUser(String username) {
		return userRepository.findUserByUsername(username.toLowerCase());
	}

	/**
	 * Метод получения авторизованного пользователя<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 *
	 * @return {@link Optional} объект искомого пользователя<p>
	 */
	@Override
	@Transactional
	public Optional<User> getAuthorizedUser() {
		return userRepository.findUserByUsername(getUsernameOfAuthorizedUser());
	}

	/**
	 * Метод обновления пароля авторизованного пользователя с использованием шифрования пароля<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 * {@link PasswordEncoder#matches(CharSequence, String)}<p>
	 * {@link PasswordEncoder#encode(CharSequence)}<p>
	 *
	 * @param newPasswordDTO DTO класс с данными об актуальном и новом пароле<p>
	 * @return <b>true/false</b>
	 * @throws UsernameNotFoundException при ошибки в аутентификации<p>
	 */
	@Override
	@Transactional
	public boolean updatePassword(NewPasswordDTO newPasswordDTO) {
		String username = getUsernameOfAuthorizedUser();
		User currentUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (encoder.matches(newPasswordDTO.getCurrentPassword(), currentUser.getPassword())) {
			currentUser.setPassword(encoder.encode(newPasswordDTO.getNewPassword()));
			return true;
		}
		return false;
	}

	/**
	 * Метод обновления данных авторизованного пользователя (кроме данных авторизации)<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 *
	 * @param updateUserDTO DTO класс с данными для изменения<p>
	 * @return {@link UpdateUserDTO} обновленные данные<p>
	 * @throws UsernameNotFoundException при ошибке в аутентификации<p>
	 */
	@Override
	@Transactional
	public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO) {
		String username = getUsernameOfAuthorizedUser();
		User currentUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		User.convertOnUserUpdate(currentUser, updateUserDTO);
		return updateUserDTO;
	}

	/**
	 * Метод обновления изоражения авторизованного пользователя<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 * {@link ImageRepository#saveAndFlush(Object)}<p>
	 * {@link UserRepository#saveAndFlush(Object)}<p>
	 *
	 * @param image {@link MultipartFile} представление загруженного файла, полученного в запросе<p>
	 * @return <b>true/false</b>
	 * @throws UsernameNotFoundException при ошибке в аутентификации<p>
	 */
	@SneakyThrows
	@Override
	@Transactional
	public boolean updateImage(MultipartFile image) {
		String username = getUsernameOfAuthorizedUser();
		User currentUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (!image.isEmpty()) {
			Image imageOfUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)).getImage();
			imageOfUser.setMediaType(image.getContentType());
			imageOfUser.setImage(image.getBytes());
			imageRepository.saveAndFlush(imageOfUser);
			currentUser.setImage(imageOfUser);
			userRepository.saveAndFlush(currentUser);
			return true;
		}
		return false;
	}

	/**
	 * Проверка авторизации пользователя<p>
	 * {@link Authentication}<p>
	 * {@link SecurityContextHolder#getContext()}<p>
	 *
	 * @return {@link String} имя авторизованного пользователя<p>
	 * @throws org.springframework.security.core.AuthenticationException при ошибки в проверке авторизации<p>
	 */
	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
