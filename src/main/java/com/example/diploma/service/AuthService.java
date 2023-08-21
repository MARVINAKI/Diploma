package com.example.diploma.service;

import com.example.diploma.dto.Register;

public interface AuthService {
	boolean login(String userName, String password);

	boolean register(Register register);
}
