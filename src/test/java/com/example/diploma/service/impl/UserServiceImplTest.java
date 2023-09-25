package com.example.diploma.service.impl;

import com.example.diploma.constant.Role;
import com.example.diploma.dto.NewPasswordDTO;
import com.example.diploma.dto.UpdateUserDTO;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.ImageRepository;
import com.example.diploma.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.diploma.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	private final User testUser = new User();
	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ImageRepository imageRepository;
	@Mock
	private PasswordEncoder encoder;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;

	@BeforeEach
	void setUp() {
		testUser.setId(TEST_USER_ID);
		testUser.setEmail(TEST_USER_EMAIL);
		testUser.setFirstName(TEST_USER_FIRSTNAME);
		testUser.setLastName(TEST_USER_LASTNAME);
		testUser.setPhone(TEST_USER_PHONE);
		testUser.setRole(Role.ADMIN);
		testUser.setUsername(TEST_USERNAME);
		testUser.setPassword(TEST_USER_PASSWORD);
//		testUser.setImage(null);
//		testUser.setComments(TEST_COMMENTS_LIST);
//		testUser.setAds(TEST_ADS_LIST_SIZE_2);
		testUser.setRegistrationDate(TEST_USER_REGISTRATION_DATE);

		TEST_EMPTY_IMAGE.setId(null);
		TEST_EMPTY_IMAGE.setMediaType(null);
		TEST_EMPTY_IMAGE.setImage(null);

		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn(TEST_USERNAME);
		lenient().when(userService.getAuthorizedUser()).thenReturn(Optional.of(testUser));
	}

	@Test
	void createUserTest() {
		assertNull(testUser.getImage());
		assertEquals(TEST_EMPTY_IMAGE, new Image());

		userService.createUser(testUser);

		verify(imageRepository, times(1)).saveAndFlush(TEST_EMPTY_IMAGE);
		verify(userRepository, times(1)).save(testUser);

		assertEquals(TEST_EMPTY_IMAGE, new Image());
		assertNotNull(testUser.getImage());
		assertEquals(TEST_EMPTY_IMAGE, testUser.getImage());
	}

	@Test
	void userExistsTest() {
		userService.userExists(TEST_USERNAME);

		verify(userRepository, times(1)).findUserByUsername(TEST_USERNAME);

		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
		assertFalse(userService.userExists(TEST_USERNAME));

		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_USER));
		assertTrue(userService.userExists(TEST_USERNAME));
	}

	@Test
	void getUserTest() {
		userService.getUser(TEST_USERNAME);

		verify(userRepository, times(1)).findUserByUsername(TEST_USERNAME);

		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
		assertTrue(userService.getUser(TEST_USERNAME).isEmpty());

		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_USER));
		assertTrue(userService.getUser(TEST_USERNAME).isPresent());
		assertEquals(userService.getUser(TEST_USERNAME).get(), TEST_USER);
	}

	@Test
	void getAuthorizedUserTest() {
		userService.getAuthorizedUser();

		verify(userRepository, times(1)).findUserByUsername(TEST_USERNAME);

		assertSame(testUser, userRepository.findUserByUsername(TEST_USERNAME).get());
	}

	@Test
	void updatePasswordTest() {
		NewPasswordDTO newPasswordDTO = new NewPasswordDTO();
		newPasswordDTO.setCurrentPassword(TEST_USER_PASSWORD);
		newPasswordDTO.setNewPassword(TEST_USER_NEW_PASSWORD);

		assertEquals(TEST_USER_PASSWORD, testUser.getPassword());

		when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));
		when(encoder.matches(newPasswordDTO.getCurrentPassword(), testUser.getPassword())).thenReturn(true);

		boolean response = userService.updatePassword(newPasswordDTO);

		verify(userRepository, times(1)).findUserByUsername(TEST_USERNAME);
		verify(encoder, times(1)).encode(newPasswordDTO.getNewPassword());

		assertNotEquals(TEST_USER_PASSWORD, testUser.getPassword());
		assertTrue(response);
	}

	@Test
	void updateUserInfoTest() {
		UpdateUserDTO updateUserDTO = new UpdateUserDTO();
		updateUserDTO.setFirstName(TEST_USER_FIRSTNAME_1);
		updateUserDTO.setLastName(TEST_USER_LASTNAME_1);
		updateUserDTO.setPhone(TEST_USER_PHONE_1);

		assertEquals(testUser.getId(), TEST_USER_ID);
		assertEquals(testUser.getFirstName(), TEST_USER_FIRSTNAME);
		assertEquals(testUser.getLastName(), TEST_USER_LASTNAME);
		assertEquals(testUser.getPhone(), TEST_USER_PHONE);

		userService.updateUserInfo(updateUserDTO);

		verify(userRepository, only()).findUserByUsername(TEST_USERNAME);

		when(userRepository.findUserByUsername(TEST_USERNAME_WRONG)).thenThrow(UsernameNotFoundException.class);

		assertThrows(UsernameNotFoundException.class, () -> userRepository.findUserByUsername(TEST_USERNAME_WRONG));

		assertSame(updateUserDTO, userService.updateUserInfo(updateUserDTO));

		assertEquals(testUser.getId(), TEST_USER_ID);
		assertNotEquals(testUser.getFirstName(), TEST_USER_FIRSTNAME);
		assertNotEquals(testUser.getLastName(), TEST_USER_LASTNAME);
		assertNotEquals(testUser.getPhone(), TEST_USER_PHONE);
	}

	@SneakyThrows
	@Test
	void updateImage() {
		testUser.setImage(TEST_EMPTY_IMAGE);

		MockMultipartFile testMultipartFile = TEST_MULTIPART_FILE;

		Image image = new Image();
		image.setMediaType(testMultipartFile.getContentType());
		image.setImage(testMultipartFile.getBytes());

		assertNotEquals(image.getMediaType(), testUser.getImage().getMediaType());
		assertNotEquals(image.getImage(), testUser.getImage().getImage());

		userService.updateImage(testMultipartFile);

		ArgumentCaptor<Image> argumentCaptor = ArgumentCaptor.forClass(Image.class);

		verify(userRepository, times(2)).findUserByUsername(TEST_USERNAME);
		verify(imageRepository, times(1)).saveAndFlush(argumentCaptor.capture());
		verify(userRepository, times(1)).saveAndFlush(testUser);

		assertEquals(image.getMediaType(), testUser.getImage().getMediaType());
		assertEquals(image.getImage(), testUser.getImage().getImage());

		assertTrue(userService.updateImage(testMultipartFile));
	}
}