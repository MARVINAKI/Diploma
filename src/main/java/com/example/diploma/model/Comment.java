package com.example.diploma.model;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CreateOrUpdateCommentDTO;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	private String text;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;

	@ManyToOne
	@JoinColumn(name = "ad_id")
	private Ad ad;

	public static CommentDTO convertCommentToCommentDTO(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setAuthor(comment.getAuthor().getId());
//		commentDTO.setAuthorImage("/" + comment.getAuthor().getImage().getId() + "/comments/image");
		commentDTO.setAuthorImage("/users/image/"+comment.getAuthor().getImage().getId());
		commentDTO.setAuthorFirstName(comment.getAuthor().getFirstName());
		commentDTO.setCreatedAt(commentDTO.getCreatedAt());
		commentDTO.setPk(comment.getId());
		commentDTO.setText(comment.getText());
		return commentDTO;
	}

	public static Comment convertNewCommentToComment(CreateOrUpdateCommentDTO createOrUpdateCommentDTO, User author, Ad ad) {
		Comment comment = new Comment();
		comment.setText(createOrUpdateCommentDTO.getText());
		comment.setAuthor(author);
		comment.setAd(ad);
		return comment;
	}
}
