package com.example.diploma.controller;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CommentsDTO;
import com.example.diploma.dto.CreateOrUpdateCommentDTO;
import com.example.diploma.service.CommentService;
import com.example.diploma.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Класс контроллер для работы с классом комментарии и связанными классами<p>
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

	private final CommentService commentService;
	private final ImageService imageService;

	/**
	 * Получение изображения пользователя для отображения в комментариях (аватара)<p>
	 * {@link ImageService#getImageById(Integer)}<p>
	 *
	 * @param id идентификационный номер изображения<p>
	 * @return представления байт изображения<p>
	 */
	@Operation(summary = "Получение изобрадения пользователя",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					)
			})
	@GetMapping(value = "/{id}/comments/image", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<byte[]> getAuthorImage(@PathVariable Integer id) {
		return ResponseEntity.ok(imageService.getImageById(id));
	}

	/**
	 * Получение всех комментариев конкретного объявления<p>
	 * {@link CommentService#findAllCommentsOfAd(Integer)}<p>
	 *
	 * @param id идентификационный номер объявления<p>
	 * @return {@link CommentsDTO} список всех комментариев<p>
	 */
	@Operation(summary = "Получение комментариев объявления",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@GetMapping("/{id}/comments")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<CommentsDTO> findCommentsOfAd(@PathVariable Integer id) {
		return ResponseEntity.ok().body(commentService.findAllCommentsOfAd(id));
	}

	/**
	 * Добавление нового комментария к объявлению<p>
	 * {@link CommentService#createCommentByAd(Integer, CreateOrUpdateCommentDTO)}<p>
	 *
	 * @param id                       идентификационный номер объявления<p>
	 * @param createOrUpdateCommentDTO {@link CreateOrUpdateCommentDTO} DTO модель с данными нового комментария<p>
	 * @return {@link CommentDTO} созданный комментарий<p>
	 */
	@Operation(summary = "Добавление комментария к объявлению",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@PostMapping("/{id}/comments")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<CommentDTO> addCommentToAd(@PathVariable Integer id, @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
		CommentDTO commentDTO = commentService.createCommentByAd(id, createOrUpdateCommentDTO);
		return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
	}

	/**
	 * Обновление определенного комментария у конкретного объявления<p>
	 * {@link CommentService#updateCommentOfAd(Integer, Integer, CreateOrUpdateCommentDTO)}<p>
	 *
	 * @param adId                     идентификационный номер объявления<p>
	 * @param commentId                идентификационный номер комментария<p>
	 * @param createOrUpdateCommentDTO {@link CreateOrUpdateCommentDTO} модель с данными для обновления комментария<p>
	 * @return {@link CommentDTO} обновленный комментарий<p>
	 */
	@Operation(summary = "Обновление комментария",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "403",
							description = "Forbidden"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@PatchMapping("/{adId}/comments/{commentId}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<CommentDTO> updateCommentOfAd(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
		CommentDTO commentDTO = commentService.updateCommentOfAd(adId, commentId, createOrUpdateCommentDTO);
		return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().build();
	}

	/**
	 * Удаление комментария объявления<p>
	 * {@link CommentService#deleteComment(Integer, Integer)}<p>
	 *
	 * @param adId      идентификационны номер объявления<p>
	 * @param commentId идентификационный номер комментария<p>
	 * @return <b>true/false</b>
	 */
	@Operation(summary = "Удаление комментария",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "OK"
					),
					@ApiResponse(
							responseCode = "401",
							description = "Unauthorized"
					),
					@ApiResponse(
							responseCode = "403",
							description = "Forbidden"
					),
					@ApiResponse(
							responseCode = "404",
							description = "Not found"
					)
			})
	@DeleteMapping("/{adId}/comments/{commentId}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Boolean> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
		return commentService.deleteComment(adId, commentId) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
	}
}

