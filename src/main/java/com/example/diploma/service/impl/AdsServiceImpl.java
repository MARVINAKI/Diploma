package com.example.diploma.service.impl;

import com.example.diploma.repository.AdRepository;
import com.example.diploma.service.AdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

	private final AdRepository adRepository;
}
