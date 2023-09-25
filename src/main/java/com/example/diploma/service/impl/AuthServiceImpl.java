package com.example.diploma.service.impl;

import com.example.diploma.dto.RegisterDTO;
import com.example.diploma.model.User;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.diploma.service.AuthService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация интерфейса {@link AuthService}<p>
 * Класс сервис {@link AuthServiceImpl} предназначен для регистрации и авторизации пользователей<p>
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserService userService;
	private final PasswordEncoder encoder;

	/**
	 * Метод проверки подлинности пользователя с использованием класса шифрования пароля {@link PasswordEncoder}<p>
	 * {@link UserService#getUser(String)}<p>
	 * {@link UserService#userExists(String)}<p>
	 * {@link PasswordEncoder#matches(CharSequence, String)}<p>
	 *
	 * @param username имя пользователя<p>
	 * @param password пароль пользователя<p>
	 * @return <b>true/false</b><p>
	 * @throws UsernameNotFoundException при отсутствии зарегистрированного пользователя<p>
	 */
	@Override
	@Transactional
	public boolean login(String username, String password) {
		User user = userService.getUser(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (!userService.userExists(username)) {
			return false;
		}
		return encoder.matches(password, user.getPassword());
	}

	/**
	 * Метиод регистрации новых пользователей, проверкой ранней регистрации, применяется шифровщик {@link PasswordEncoder}<p>
	 * {@link UserService#userExists(String)}<p>
	 * {@link PasswordEncoder#encode(CharSequence)}
	 * {@link UserService#createUser(User)}<p>
	 *
	 * @param registerDTO DTO класс {@link RegisterDTO} с данными пользователя для регистрации<p>
	 * @return <b>true/false</b><p>
	 */
	@Override
	@Transactional
	public boolean register(RegisterDTO registerDTO) {
		if (userService.userExists(registerDTO.getUsername())) {
			return false;
		}
		User user = User.convertRegisterToUser(registerDTO);
		user.setPassword(encoder.encode(registerDTO.getPassword()));
		userService.createUser(user);
		return true;
	}
}
