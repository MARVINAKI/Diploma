package com.example.diploma.service.impl;

import com.example.diploma.exception.ImageNotFoundException;
import com.example.diploma.model.Image;
import com.example.diploma.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.diploma.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

	@InjectMocks
	private ImageServiceImpl imageService;
	@Mock
	private ImageRepository imageRepository;

	@Test
	void getImageByIdTest() {
		when(imageRepository.findById(TEST_IMAGE_ID)).thenReturn(Optional.of(TEST_IMAGE));

		byte[] expectedImage = imageService.getImageById(TEST_IMAGE_ID);

		verify(imageRepository, times(1)).findById(TEST_IMAGE_ID);

		assertDoesNotThrow(ImageNotFoundException::new);
		assertSame(expectedImage, TEST_IMAGE.getImage());
		assertNotSame(expectedImage, TEST_EMPTY_IMAGE.getImage());
	}

	@Test
	void getExceptionWhenFindImageByWrongId() {
		when(imageRepository.findById(TEST_IMAGE_ID_WRONG)).thenReturn(Optional.empty());

		assertThrows(ImageNotFoundException.class, () -> imageService.getImageById(TEST_IMAGE_ID_WRONG));

		verify(imageRepository, times(1)).findById(TEST_IMAGE_ID_WRONG);
	}
}