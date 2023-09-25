package com.example.diploma.service.impl;

import com.example.diploma.constant.Role;
import com.example.diploma.dto.RegisterDTO;
import com.example.diploma.model.User;
import com.example.diploma.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.diploma.constant.Constant.*;
import static com.example.diploma.constant.Constant.TEST_USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	private final User testUser = new User();
	private final RegisterDTO registerDTO = new RegisterDTO();
	@InjectMocks
	private AuthServiceImpl authService;
	@Mock
	private UserService userService;
	@Mock
	private PasswordEncoder encoder;

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
		testUser.setComments(TEST_COMMENTS_LIST);
		testUser.setAds(TEST_ADS_LIST_SIZE_2);
		testUser.setRegistrationDate(TEST_USER_REGISTRATION_DATE);

		registerDTO.setFirstName(TEST_USER_FIRSTNAME_1);
		registerDTO.setLastName(TEST_USER_LASTNAME_1);
		registerDTO.setUsername(TEST_USERNAME_1);
		registerDTO.setPassword(TEST_USER_NEW_PASSWORD);
		registerDTO.setRole(TEST_USER_ROLE_USER);
		registerDTO.setPhone(TEST_USER_PHONE_1);
	}

	@Test
	void loginTest() {
		when(userService.getUser(TEST_USERNAME)).thenReturn(Optional.of(testUser));
		when(userService.userExists(TEST_USERNAME)).thenReturn(true);
		when(encoder.matches(TEST_USER_PASSWORD, testUser.getPassword())).thenReturn(true);

		boolean response = authService.login(TEST_USERNAME, TEST_USER_PASSWORD);

		verify(userService, times(1)).getUser(TEST_USERNAME);
		verify(userService, times(1)).userExists(TEST_USERNAME);
		verify(encoder, times(1)).matches(TEST_USER_PASSWORD, testUser.getPassword());

		assertTrue(response);
	}

	@Test
	void loginFailedTest() {
		assertThrows(UsernameNotFoundException.class, () -> authService.login(TEST_USERNAME_WRONG, TEST_USER_PASSWORD));

		verify(userService, times(1)).getUser(TEST_USERNAME_WRONG);
		verify(userService, never()).userExists(anyString());
		verify(encoder, never()).matches(anyString(), anyString());
	}

	@Test
	void registerTest() {
		when(userService.userExists(registerDTO.getUsername())).thenReturn(false);

		boolean response = authService.register(registerDTO);

		verify(userService, times(1)).userExists(registerDTO.getUsername());
		verify(encoder, times(1)).encode(registerDTO.getPassword());
		verify(userService, times(1)).createUser(any(User.class));

		assertTrue(response);
	}

	@Test
	void registerFailedTest() {
		when(userService.userExists(registerDTO.getUsername())).thenReturn(true);

		boolean response = authService.register(registerDTO);

		verify(userService, times(1)).userExists(registerDTO.getUsername());
		verify(encoder, never()).encode(anyString());
		verify(userService, never()).createUser(any(User.class));

		assertFalse(response);
	}
}