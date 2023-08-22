package com.example.diploma.service.impl;

import com.example.diploma.repository.ExtendedAdRepository;
import com.example.diploma.service.ExtendedAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtendedAdServiceImpl implements ExtendedAdService {

	private final ExtendedAdRepository extendedAdRepository;
}
