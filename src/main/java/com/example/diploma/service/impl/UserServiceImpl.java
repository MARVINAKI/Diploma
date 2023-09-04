package com.example.diploma.service.impl;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public void createUser(User user) {
		userRepository.save(user);
	}

	@Override
	@Transactional
	public boolean userExists(String username) {
		return userRepository.findUserByUsername(username).isPresent();
	}

	@Override
	@Transactional
	public Optional<User> getUser(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	@Transactional
	public Optional<Image> getImage(Integer id) {
		return userRepository.findImageById(id);
	}

	@Override
	@Transactional
	public UserDTO getAuthorizedUser() {
		User user = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		return User.convertUserToUserDTO(user);
	}

	@Override
	@Transactional
	public boolean updatePassword(NewPassword newPassword) {
		User currentUser = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		if (encoder.matches(newPassword.getCurrentPassword(), currentUser.getPassword())) {
			currentUser.setPassword(encoder.encode(newPassword.getNewPassword()));
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public UpdateUser updateUserInfo(UpdateUser updateUser) {
		User currentUser = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		User.convertOnUserUpdate(currentUser, updateUser);
		return updateUser;
	}

	@SneakyThrows
	@Override
	@Transactional
	public boolean updateImage(MultipartFile image) {
		String username = getUsernameOfAuthorizedUser();
		if (!image.isEmpty() && userExists(username)) {
			User currentUser = userRepository.findUserByUsername(username).orElseThrow();
			Image imageOfUser = userRepository.findImage(username).orElse(new Image());
			imageOfUser.setMediaType(image.getContentType());
			imageOfUser.setImage(image.getBytes());
			currentUser.setImage(imageOfUser);
			return true;
		}
		return false;
	}

	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
