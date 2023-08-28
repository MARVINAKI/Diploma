package com.example.diploma.service.impl;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.User;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Override
	public void createUser(User user) {
		userRepository.save(user);
	}

	@Override
	public boolean userExists(String username) {
		return userRepository.findUserByUsername(username).isPresent();
	}

	@Override
	public User getUser(String username) {
		return userRepository.findUserByUsername(username).orElseThrow();
	}

	@Override
	public UserDTO getAuthorizedUser() {
		String username = getUsernameOfAuthorizedUser();
		User user = userRepository.findUserByUsername(username).orElseThrow();
		return User.convertUserToUserDTO(user);
	}

	@Override
	public boolean updatePassword(NewPassword newPassword) {
		String username = getUsernameOfAuthorizedUser();
		User user = userRepository.findUserByUsername(username).orElseThrow();
		if (encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
			userRepository.updatePassword(username, newPassword.getNewPassword());
			return true;
		}
		return false;
	}

	@Override
	public UpdateUser updateUserInfo(UpdateUser updateUser) {
		UpdateUser updateUserResponse = new UpdateUser();
		String username = getUsernameOfAuthorizedUser();
		Optional<User> user = userRepository.findUserByUsername(username);
		if (user.isPresent()) {
			userRepository.save(User.convertOnUserUpdate(user.get(), updateUser));
			updateUserResponse = updateUser;
		}
		return updateUserResponse;
	}

	@Override
	public boolean updateImage(String image) {
		String username = getUsernameOfAuthorizedUser();
		Optional<User> user = userRepository.findUserByUsername(username);
		if (user.isPresent()) {
			user.get().setImage(image);
			userRepository.save(user.get());
			return true;
		}
		return false;
	}

	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
