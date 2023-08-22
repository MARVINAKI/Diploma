package com.example.diploma.service.impl;

import com.example.diploma.repository.CommentRepository;
import com.example.diploma.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

	private final CommentRepository commentRepository;
}
