package com.example.diploma.service.impl;

import com.example.diploma.model.Image;
import com.example.diploma.repository.ImageRepository;
import com.example.diploma.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	@Override
	@Transactional
	public byte[] getImageById(Integer id) {
		Image image = imageRepository.findById(id).orElseThrow();
		return image.getImage();
	}
}
