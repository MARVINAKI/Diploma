package com.example.diploma.service.impl;

import com.example.diploma.dto.NewPasswordDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.ImageRepository;
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
	private final ImageRepository imageRepository;
	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public void createUser(User user) {
		Image emptyImage = new Image();
		imageRepository.saveAndFlush(emptyImage);
		user.setImage(emptyImage);
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
	public UserDTO getAuthorizedUser() {
		User user = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		return User.convertUserToUserDTO(user);
	}

	@Override
	@Transactional
	public boolean updatePassword(NewPasswordDTO newPasswordDTO) {
		User currentUser = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		if (encoder.matches(newPasswordDTO.getCurrentPassword(), currentUser.getPassword())) {
			currentUser.setPassword(encoder.encode(newPasswordDTO.getNewPassword()));
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO) {
		User currentUser = userRepository.findUserByUsername(getUsernameOfAuthorizedUser()).orElseThrow();
		User.convertOnUserUpdate(currentUser, updateUserDTO);
		return updateUserDTO;
	}

	@SneakyThrows
	@Override
	@Transactional
	public boolean updateImage(MultipartFile image) {
		String username = getUsernameOfAuthorizedUser();
		User currentUser = userRepository.findUserByUsername(username).orElseThrow();
		if (!image.isEmpty()) {
			Image imageOfUser = userRepository.findUserByUsername(username).orElseThrow().getImage();
			imageOfUser.setMediaType(image.getContentType());
			imageOfUser.setImage(image.getBytes());
			imageRepository.saveAndFlush(imageOfUser);
			currentUser.setImage(imageOfUser);
			userRepository.saveAndFlush(currentUser);
			return true;
		}
		return false;
	}

	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
