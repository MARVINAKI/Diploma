package com.example.diploma.service.impl;

import com.example.diploma.dto.Register;
import com.example.diploma.model.User;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.diploma.service.AuthService;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserService userService;
	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public boolean login(String username, String password) {
		if (!userService.userExists(username)) {
			return false;
		}
		return encoder.matches(password, userService.getUser(username).getPassword());
	}

	@Override
	@Transactional
	public boolean register(Register register) {
		if (userService.userExists(register.getUsername())) {
			return false;
		}
		User user = User.convertRegisterToUser(register);
		user.setPassword(encoder.encode(register.getPassword()));
		userService.createUser(user);
		return true;
	}
}
