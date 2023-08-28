package com.example.diploma.service;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.Comments;
import com.example.diploma.dto.CreateOrUpdateComment;

public interface CommentService {

	Comments findAllCommentsOfAd(Integer id);

	CommentDTO createCommentByAd(Integer id, CreateOrUpdateComment comment);

	CommentDTO updateCommentOfAd(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment);

	boolean deleteComment(Integer adId, Integer commentId);
}
