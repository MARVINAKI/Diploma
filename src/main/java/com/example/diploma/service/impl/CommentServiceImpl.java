package com.example.diploma.service.impl;

import com.example.diploma.repository.CommentRepository;
import com.example.diploma.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
}
