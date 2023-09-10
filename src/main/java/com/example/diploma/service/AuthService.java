package com.example.diploma.service;

import com.example.diploma.dto.RegisterDTO;

public interface AuthService {
	boolean login(String userName, String password);

	boolean register(RegisterDTO registerDTO);
}
