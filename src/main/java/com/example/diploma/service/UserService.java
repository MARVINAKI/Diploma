package com.example.diploma.service;

import com.example.diploma.dto.NewPasswordDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

	void createUser(User user);

	boolean userExists(String email);

	Optional<User> getUser(String username);

	UserDTO getAuthorizedUser();

	boolean updatePassword(NewPasswordDTO newPasswordDTO);

	UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO);

	boolean updateImage(MultipartFile image);
}
