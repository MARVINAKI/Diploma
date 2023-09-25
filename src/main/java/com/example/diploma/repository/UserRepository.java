package com.example.diploma.repository;

import com.example.diploma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс репозиторий для работы с данными класса пользователь {@link User}<p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * Поиск пользователя по имени пользователя<p>
	 *
	 * @param username имя пользователя<p>
	 * @return {@link Optional} объект искомого пользователя<p>
	 */
	Optional<User> findUserByUsername(String username);

}
