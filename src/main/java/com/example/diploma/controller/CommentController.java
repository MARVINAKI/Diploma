package com.example.diploma.controller;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.Comments;
import com.example.diploma.dto.CreateOrUpdateComment;
import com.example.diploma.service.CommentService;
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

	@GetMapping("/{id}/comments")
	public ResponseEntity<Comments> findCommentsOfAd(@PathVariable Integer id) {
		return ResponseEntity.ok().body(commentService.findAllCommentsOfAd(id));
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<CommentDTO> addCommentToAd(@PathVariable Integer id, @RequestBody CreateOrUpdateComment createOrUpdateComment) {
		CommentDTO commentDTO = commentService.createCommentByAd(id, createOrUpdateComment);
		return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
	}

	@PatchMapping("/{adId}/comments/{commentId}")
	public ResponseEntity<CommentDTO> updateCommentOfAd(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateComment createOrUpdateComment) {
		CommentDTO commentDTO = commentService.updateCommentOfAd(adId, commentId, createOrUpdateComment);
		return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
	}

	@DeleteMapping("/{adId}/comments/{commentId}")
	public ResponseEntity<Boolean> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
		return commentService.deleteComment(adId, commentId) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
	}
}

