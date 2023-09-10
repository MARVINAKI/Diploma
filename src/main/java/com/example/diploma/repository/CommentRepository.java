package com.example.diploma.repository;

import com.example.diploma.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	Optional<Comment> findCommentByIdAndAd_Id(Integer commentId, Integer adId);
}
