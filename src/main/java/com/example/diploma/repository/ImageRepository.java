package com.example.diploma.repository;

import com.example.diploma.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Класс репозиторий для работы с данными класса изображения {@link Image}<p>
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
