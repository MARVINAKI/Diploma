package com.example.diploma.dto;

import lombok.Data;

import java.util.List;

@Data
public class Comments {

	private Integer count;
	private List<CommentDTO> results;

	public static Comments convertListToComments(List<CommentDTO> commentDTOList) {
		Comments comments = new Comments();
		comments.setCount(commentDTOList.size());
		comments.setResults(commentDTOList);
		return comments;
	}
}
