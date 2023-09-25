package com.example.diploma.service.impl;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CommentsDTO;
import com.example.diploma.dto.CreateOrUpdateCommentDTO;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Comment;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.CommentRepository;
import com.example.diploma.repository.UserRepository;
import com.example.diploma.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link CommentService}<p>
 * Класс сервис {@link CommentServiceImpl} предназначен для управления классом комментарии {@link Comment} и связанными классами<p>
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final AdRepository adRepository;
	private final UserRepository userRepository;

	/**
	 * Метод поиска всех комментариев конкретного объявления<p>
	 * {@link CommentRepository#findAll()}<p>
	 *
	 * @param id идентификационный номер объявления<p>
	 * @return {@link CommentsDTO} список комментариев<p>
	 */
	@Override
	@Transactional
	public CommentsDTO findAllCommentsOfAd(Integer id) {
		List<CommentDTO> commentDTOList = commentRepository.findAll().stream()
				.filter(comment -> comment.getAd().getId().equals(id))
				.map(Comment::convertCommentToCommentDTO)
				.collect(Collectors.toList());
		return CommentsDTO.convertListToComments(commentDTOList);
	}

	/**
	 * Метод создания комментария для конкретного объявления с проверкой авторизации<p>
	 * {@link UserRepository#findUserByUsername(String)}<p>
	 * {@link AdRepository#findById(Object)}<p>
	 * {@link CommentRepository#save(Object)}<p>
	 *
	 * @param id                       идентификационный номер объявления<p>
	 * @param createOrUpdateCommentDTO DTO класс {@link CreateOrUpdateCommentDTO} с данными для создания комментария<p>
	 * @return {@link CommentDTO} созданный комментарий с связанными данными<p>
	 */
	@Override
	@Transactional
	public CommentDTO createCommentByAd(Integer id, CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
		CommentDTO commentDTO = new CommentDTO();
		String username = getUsernameOfAuthorizedUser();
		Optional<User> author = userRepository.findUserByUsername(username);
		Optional<Ad> ad = adRepository.findById(id);
		if (ad.isPresent() && author.isPresent()) {
			Comment comment = Comment.convertNewCommentToComment(createOrUpdateCommentDTO, author.get(), ad.get());
			commentDTO = Comment.convertCommentToCommentDTO(comment);
			commentRepository.save(comment);
		}
		return commentDTO;
	}

	/**
	 * Метод обновления конкретного комментария у определенного объявления<p>
	 * {@link CommentRepository#findCommentByIdAndAd_Id(Integer, Integer)}<p>
	 * {@link CommentRepository#save(Object)}<p>
	 *
	 * @param adId                     идентификационный номер объявления<p>
	 * @param commentId                идентификационный номер комментария<p>
	 * @param createOrUpdateCommentDTO DTO класс {@link CreateOrUpdateCommentDTO} с данными для обновления комментария<p>
	 * @return {@link CommentDTO} обновленный комментарий с связанными данными<p>
	 */
	@Override
	@Transactional
	public CommentDTO updateCommentOfAd(Integer adId, Integer commentId, CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
		CommentDTO commentDTO = new CommentDTO();
		Optional<Comment> comment = commentRepository.findCommentByIdAndAd_Id(commentId, adId);
		if (comment.isPresent()) {
			comment.get().setText(createOrUpdateCommentDTO.getText());
			commentDTO = Comment.convertCommentToCommentDTO(comment.get());
			commentRepository.save(comment.get());
		}
		return commentDTO;
	}

	/**
	 * Метод удаления конкретного комментария у определенного объявления<p>
	 * {@link CommentRepository#findCommentByIdAndAd_Id(Integer, Integer)}<p>
	 * {@link CommentRepository#deleteById(Object)}<p>
	 *
	 * @param adId      идентификационный номер объявления<p>
	 * @param commentId идентификационный номер комментария<p>
	 * @return <b>true/false</b>
	 */
	@Override
	@Transactional
	public boolean deleteComment(Integer adId, Integer commentId) {
		Optional<Comment> comment = commentRepository.findCommentByIdAndAd_Id(commentId, adId);
		if (comment.isPresent()) {
			commentRepository.deleteById(commentId);
			return true;
		}
		return false;
	}

	/**
	 * Проверка авторизации пользователя<p>
	 * {@link Authentication}<p>
	 * {@link SecurityContextHolder#getContext()}<p>
	 *
	 * @return {@link String} имя авторизованного пользователя<p>
	 * @throws org.springframework.security.core.AuthenticationException при ошибки в проверке авторизации<p>
	 */
	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
