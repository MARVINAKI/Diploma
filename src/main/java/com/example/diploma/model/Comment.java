package com.example.diploma.model;

import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CreateOrUpdateComment;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {

	@Column(name = "created_at")
	private Integer createdAt = 123456789;		//???????дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
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
		commentDTO.setAuthorImage(comment.getAuthor().getImage());
		commentDTO.setAuthorFirstName(comment.getAuthor().getFirstName());
		commentDTO.setCreatedAt(commentDTO.getCreatedAt());
		commentDTO.setPk(comment.getId());
		commentDTO.setText(comment.getText());
		return commentDTO;
	}

	public static Comment convertNewCommentToComment(CreateOrUpdateComment createOrUpdateComment, User author, Ad ad) {
		Comment comment = new Comment();
		comment.setText(createOrUpdateComment.getText());
		comment.setAuthor(author);
		comment.setAd(ad);
		return comment;
	}
}
