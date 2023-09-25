package com.example.diploma.repository;

import com.example.diploma.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Класс репозиторий для работы с данными класса комментарии {@link Comment}<p>
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	/**
	 * Поиск комментария по идентификационному номеру объявления и комментария<p>
	 *
	 * @param commentId идентификационный номер комментария<p>
	 * @param adId      идентификационный номер объявления<p>
	 * @return {@link Optional} объект искомого комментария<p>
	 */
	Optional<Comment> findCommentByIdAndAd_Id(Integer commentId, Integer adId);
}
