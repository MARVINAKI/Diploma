package com.example.diploma.controller;

import com.example.diploma.dto.Comment;
import com.example.diploma.dto.Comments;
import com.example.diploma.dto.CreateOrUpdateComment;
import com.example.diploma.service.AdService;
import com.example.diploma.service.CommentService;
import com.example.diploma.service.CommentsService;
import com.example.diploma.service.CreateOrUpdateCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

	private final CommentService commentService;
	private final CommentsService commentsService;
	private final CreateOrUpdateCommentService createOrUpdateCommentService;
	private final AdService adService;

	@GetMapping("/{id}/comments")
	public ResponseEntity<Comments> findCommentsOfAd(@PathVariable Integer id) {
		return ResponseEntity.ok(new Comments());
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<Comment> addCommentToAd(
			@PathVariable Integer id,
			@RequestBody CreateOrUpdateComment createOrUpdateComment
	) {
		return ResponseEntity.ok(new Comment());
	}

	@PatchMapping("/{adId}/comments/{commentId}")
	public ResponseEntity<Comment> updateCommentOfAd(
			@PathVariable Integer adId,
			@PathVariable Integer commentId,
			@RequestBody CreateOrUpdateComment createOrUpdateComment
	) {
		return ResponseEntity.ok(new Comment());
	}

	@DeleteMapping("/{adId}/comments/{commentId}")
	public ResponseEntity<Boolean> deleteComment(
			@PathVariable Integer adId,
			@PathVariable Integer commentId
	) {
		return ResponseEntity.ok(false);
	}
}

