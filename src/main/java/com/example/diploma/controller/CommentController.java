package com.example.diploma.controller;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CommentsDTO;
import com.example.diploma.dto.CreateOrUpdateCommentDTO;
import com.example.diploma.service.CommentService;
import com.example.diploma.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

	private final CommentService commentService;
	private final ImageService imageService;

	@GetMapping(value = "/{id}/comments/image", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<byte[]> getAuthorImage(@PathVariable Integer id) {
		return ResponseEntity.ok(imageService.getImageById(id));
	}

	@GetMapping("/{id}/comments")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<CommentsDTO> findCommentsOfAd(@PathVariable Integer id) {
		return ResponseEntity.ok().body(commentService.findAllCommentsOfAd(id));
	}

	@PostMapping("/{id}/comments")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<CommentDTO> addCommentToAd(@PathVariable Integer id, @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
		CommentDTO commentDTO = commentService.createCommentByAd(id, createOrUpdateCommentDTO);
		return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
	}

	@PatchMapping("/{adId}/comments/{commentId}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<CommentDTO> updateCommentOfAd(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
		CommentDTO commentDTO = commentService.updateCommentOfAd(adId, commentId, createOrUpdateCommentDTO);
		return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
	}

	@DeleteMapping("/{adId}/comments/{commentId}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Boolean> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
		return commentService.deleteComment(adId, commentId) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
	}
}

