package com.example.diploma.service;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CommentsDTO;
import com.example.diploma.dto.CreateOrUpdateCommentDTO;

public interface CommentService {

	CommentsDTO findAllCommentsOfAd(Integer id);

	CommentDTO createCommentByAd(Integer id, CreateOrUpdateCommentDTO comment);

	CommentDTO updateCommentOfAd(Integer adId, Integer commentId, CreateOrUpdateCommentDTO createOrUpdateCommentDTO);

	boolean deleteComment(Integer adId, Integer commentId);
}
