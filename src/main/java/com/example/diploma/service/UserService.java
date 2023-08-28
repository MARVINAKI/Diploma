package com.example.diploma.service;

import com.example.diploma.dto.NewPassword;
import com.example.diploma.dto.UpdateUser;
import com.example.diploma.dto.UserDTO;
import com.example.diploma.model.User;

public interface UserService {

	void createUser(User user);

	boolean userExists(String email);

	User getUser(String username);

	UserDTO getAuthorizedUser();

	boolean updatePassword(NewPassword newPassword);

	UpdateUser updateUserInfo(UpdateUser updateUser);

	boolean updateImage(String image);
}
