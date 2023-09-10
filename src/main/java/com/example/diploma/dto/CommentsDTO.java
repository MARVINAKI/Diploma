package com.example.diploma.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentsDTO {

	private Integer count;
	private List<CommentDTO> results;

	public static CommentsDTO convertListToComments(List<CommentDTO> commentDTOList) {
		CommentsDTO commentsDTO = new CommentsDTO();
		commentsDTO.setCount(commentDTOList.size());
		commentsDTO.setResults(commentDTOList);
		return commentsDTO;
	}
}
