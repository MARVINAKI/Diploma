package com.example.diploma.service.impl;

import com.example.diploma.repository.AdRepository;
import com.example.diploma.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

	private final AdRepository adRepository;
}
