package com.example.diploma.service;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

	void createUser(User user);

	boolean userExists(String email);

	Optional<User> getUser(String username);

	Optional<Image> getImage(Integer id);

	UserDTO getAuthorizedUser();

	boolean updatePassword(NewPassword newPassword);

	UpdateUser updateUserInfo(UpdateUser updateUser);

	boolean updateImage(MultipartFile image);
}
