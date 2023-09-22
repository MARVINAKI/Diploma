package com.example.diploma.service.impl;

import com.example.diploma.constant.Role;
import com.example.diploma.dto.CommentDTO;
import com.example.diploma.dto.CreateOrUpdateCommentDTO;
import com.example.diploma.model.Comment;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.CommentRepository;
import com.example.diploma.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static com.example.diploma.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

	private final User testUser = new User();
	@InjectMocks
	private CommentServiceImpl commentService;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private AdRepository adRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;

	@BeforeEach
	public void setUp() {
		testUser.setId(TEST_USER_ID);
		testUser.setEmail(TEST_USER_EMAIL);
		testUser.setFirstName(TEST_USER_FIRSTNAME);
		testUser.setLastName(TEST_USER_LASTNAME);
		testUser.setPhone(TEST_USER_PHONE);
		testUser.setRole(Role.ADMIN);
		testUser.setUsername(TEST_USERNAME);
		testUser.setPassword(TEST_USER_PASSWORD);
		testUser.setImage(TEST_IMAGE);
		testUser.setAds(List.of(TEST_AD));
		testUser.setRegistrationDate(TEST_USER_REGISTRATION_DATE);

		TEST_AD.setAuthor(testUser);

		TEST_COMMENT.setAuthor(testUser);
		TEST_COMMENT.setAd(TEST_AD);
		TEST_COMMENT_1.setAuthor(testUser);
		TEST_COMMENT_1.setAd(TEST_AD);


		SecurityContextHolder.setContext(securityContext);

		lenient().when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
		lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
		lenient().when(authentication.getName()).thenReturn(TEST_USERNAME);
	}

	@Test
	void findAllCommentsOfAdTest() {
		when(commentRepository.findAll()).thenReturn(TEST_COMMENTS_LIST);

		commentService.findAllCommentsOfAd(TEST_AD_ID);

		verify(commentRepository, times(1)).findAll();

		assertEquals(commentRepository.findAll().size(), commentService.findAllCommentsOfAd(TEST_AD_ID).getResults().size());
		assertEquals(Comment.convertCommentToCommentDTO(TEST_COMMENT), commentService.findAllCommentsOfAd(TEST_AD_ID).getResults().get(0));
	}

	@Test
	void createCommentByAdTest() {
		CreateOrUpdateCommentDTO createOrUpdateCommentDTO = new CreateOrUpdateCommentDTO();
		createOrUpdateCommentDTO.setText("text for test");
		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.of(TEST_AD));

		assertNull(TEST_AD.getComments());
		assertNull(testUser.getComments());

		CommentDTO commentDTO = commentService.createCommentByAd(TEST_AD_ID, createOrUpdateCommentDTO);

		verify(userRepository, times(1)).findUserByUsername(TEST_USERNAME);
		verify(adRepository, times(1)).findById(TEST_AD_ID);
		verify(commentRepository, times(1)).save(any(Comment.class));

		assertEquals(commentDTO.getText(), createOrUpdateCommentDTO.getText());
	}

	@Test
	void updateCommentOfAdTest() {
		CreateOrUpdateCommentDTO createOrUpdateCommentDTO = new CreateOrUpdateCommentDTO();
		createOrUpdateCommentDTO.setText("text for test");
		when(commentRepository.findCommentByIdAndAd_Id(TEST_COMMENT_ID, TEST_AD_ID)).thenReturn(Optional.of(TEST_COMMENT));

		CommentDTO commentDTO = commentService.updateCommentOfAd(TEST_AD_ID, TEST_COMMENT_ID, createOrUpdateCommentDTO);

		verify(commentRepository, times(1)).findCommentByIdAndAd_Id(TEST_COMMENT_ID, TEST_AD_ID);
		verify(commentRepository, times(1)).save(any(Comment.class));

		assertEquals(commentDTO.getText(), createOrUpdateCommentDTO.getText());
	}

	@Test
	void deleteCommentTest() {
		when(commentRepository.findCommentByIdAndAd_Id(TEST_COMMENT_ID, TEST_AD_ID)).thenReturn(Optional.of(TEST_COMMENT));

		boolean response = commentService.deleteComment(TEST_AD_ID, TEST_COMMENT_ID);

		verify(commentRepository, times(1)).findCommentByIdAndAd_Id(TEST_COMMENT_ID, TEST_AD_ID);
		verify(commentRepository, times(1)).deleteById(TEST_COMMENT_ID);

		assertTrue(response);
	}

	@Test
	void noDeleteCommentTest() {
		when(commentRepository.findCommentByIdAndAd_Id(TEST_COMMENT_ID, TEST_AD_ID)).thenReturn(Optional.empty());

		boolean response = commentService.deleteComment(TEST_AD_ID, TEST_COMMENT_ID);

		verify(commentRepository, times(1)).findCommentByIdAndAd_Id(TEST_COMMENT_ID, TEST_AD_ID);
		verify(commentRepository, never()).deleteById(anyInt());

		assertFalse(response);
	}
}