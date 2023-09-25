package com.example.diploma.service.impl;

import com.example.diploma.exception.ImageNotFoundException;
import com.example.diploma.model.Image;
import com.example.diploma.repository.ImageRepository;
import com.example.diploma.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация интерфейса {@link ImageService}<p>
 * Класс сервис {@link ImageServiceImpl} предназначен для управления классом изображения {@link Image} и связанными классами<p>
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;

	/**
	 * Метод получения предварительного массива байт изображения из БД, без методов реализации, для дальнейшей обработки в связанных классах<p>
	 * {@link ImageRepository#findById(Object)}<p>
	 *
	 * @param id идентификационный номер объекта<p>
	 * @return массив байт изображения<p>
	 * @throws ImageNotFoundException при отсутсвии объекта в БД<p>
	 */
	@Override
	@Transactional
	public byte[] getImageById(Integer id) {
		Image image = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
		return image.getImage();
	}
}
