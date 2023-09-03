package com.example.diploma.service.impl;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.Comments;
import com.example.diploma.dto.CreateOrUpdateComment;
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

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final AdRepository adRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public Comments findAllCommentsOfAd(Integer id) {
		List<CommentDTO> commentDTOList = commentRepository.findAll().stream()
				.filter(comment -> comment.getAd().getId().equals(id))
				.map(Comment::convertCommentToCommentDTO)
				.collect(Collectors.toList());
		return Comments.convertListToComments(commentDTOList);
	}

	@Override
	@Transactional
	public CommentDTO createCommentByAd(Integer id, CreateOrUpdateComment createOrUpdateComment) {
		CommentDTO commentDTO = new CommentDTO();
		String username = getUsernameOfAuthorizedUser();
		Optional<User> author = userRepository.findUserByUsername(username);
		Optional<Ad> ad = adRepository.findById(id);
		if (ad.isPresent() && author.isPresent()) {
			Comment comment = Comment.convertNewCommentToComment(createOrUpdateComment, author.get(), ad.get());
			commentDTO = Comment.convertCommentToCommentDTO(comment);
			commentRepository.save(comment);
		}
		return commentDTO;
	}

	@Override
	@Transactional
	public CommentDTO updateCommentOfAd(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
		CommentDTO commentDTO = new CommentDTO();
		Optional<Comment> comment = commentRepository.findCommentByIdAndAd_Id(commentId, adId);
		if (comment.isPresent()) {
			comment.get().setText(createOrUpdateComment.getText());
			commentDTO = Comment.convertCommentToCommentDTO(comment.get());
			commentRepository.save(comment.get());
		}
		return commentDTO;
	}

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

	private static String getUsernameOfAuthorizedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
