package com.example.diploma.repository;

import com.example.diploma.model.Ad;
import com.example.diploma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс репозиторий для работы с данными класса объявлений {@link Ad}<p>
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

	/**
	 * Поиск всех объявлених конкретного пользователя<p>
	 *
	 * @param user автор объявлений<p>
	 * @return список объявлений автора {@link List} of {@link Ad}
	 */
	List<Ad> findAllByAuthor(User user);

}
