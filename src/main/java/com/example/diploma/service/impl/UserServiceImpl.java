package com.example.diploma.service.impl;

import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
}
