package com.example.diploma.constant;

import com.example.diploma.model.Ad;
import com.example.diploma.model.Comment;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import lombok.SneakyThrows;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Constant {

	/**
	 * Constants by user
	 */
	public static final Integer TEST_USER_ID = 1;
	public static final String TEST_USER_EMAIL = "test@yandex.ru";
	public static final String TEST_USER_EMAIL_1 = "test111@yandex.ru";
	public static final String TEST_USER_FIRSTNAME = "testFirstname";
	public static final String TEST_USER_FIRSTNAME_1 = "testFirstname1";
	public static final String TEST_USER_LASTNAME = "testLastname";
	public static final String TEST_USER_LASTNAME_1 = "testLastname1";
	public static final String TEST_USER_PHONE = "+79179171717";
	public static final String TEST_USER_PHONE_1 = "+79179171718";
	public static final Role TEST_USER_ROLE_ADMIN = Role.ADMIN;
	public static final Role TEST_USER_ROLE_USER = Role.USER;
	public static final LocalDateTime TEST_USER_REGISTRATION_DATE = LocalDateTime.now();
	public static final String TEST_USERNAME = "test@yandex.ru";
	public static final String TEST_USERNAME_1 = "test111@yandex.ru";
	public static final String TEST_USERNAME_WRONG = "test@";
	public static final String TEST_USER_PASSWORD = "12345678";
	public static final String TEST_USER_NEW_PASSWORD = "87654321";
	public static final User TEST_USER = getTestUser();
	public static final User TEST_USER_1 = getTestUser();

	/**
	 * Constants by image
	 */
	public static final String PATH_TO_TEST_IMAGE = "src/test/resources/images/";
	public static final String FILENAME_TEST_IMAGE = "Marvin.jpg";
	public static final Integer TEST_IMAGE_ID = 2;
	public static final Integer TEST_IMAGE_ID_WRONG = 22;
	public static final Image TEST_IMAGE = getTestImage();
	public static final Image TEST_EMPTY_IMAGE = new Image();
	public static final MockMultipartFile TEST_MULTIPART_FILE = new MockMultipartFile("Image", "Image", TEST_IMAGE.getMediaType(), TEST_IMAGE.getImage());


	/**
	 * Constants by Ad
	 */
	public static final Integer TEST_AD_ID = 3;
	public static final Integer TEST_AD_ID_1 = 33;
	public static final Integer TEST_AD_PRICE = 1000;
	public static final Integer TEST_AD_PRICE_1 = 1111;
	public static final String TEST_AD_TITLE = "TEST TITLE";
	public static final String TEST_AD_TITLE_1 = "TEST TITLE 1";
	public static final String TEST_AD_DESCRIPTION = "TEST DESCRIPTION";
	public static final String TEST_AD_DESCRIPTION_1 = "TEST DESCRIPTION 1";
	public static final Ad TEST_AD = getTestAd(TEST_AD_ID, TEST_AD_PRICE, TEST_AD_TITLE, TEST_AD_DESCRIPTION);
	public static final Ad TEST_AD_1 = getTestAd(TEST_AD_ID_1, TEST_AD_PRICE_1, TEST_AD_TITLE_1, TEST_AD_DESCRIPTION_1);
	public static final List<Ad> TEST_ADS_LIST_SIZE_2 = List.of(TEST_AD, TEST_AD_1);
	public static final List<Ad> TEST_ADS_LIST_SIZE_1 = List.of(TEST_AD);

	/**
	 * Constants by comment
	 */
	public static final LocalDateTime TEST_CREATED_AT = LocalDateTime.now();
	public static final Integer TEST_COMMENT_ID = 4;
	public static final Integer TEST_COMMENT_ID_1 = 5;
	public static final String TEST_COMMENT_TEXT = "TEST TEXT";
	public static final String TEST_COMMENT_TEXT_1 = "TEST TEXT 1";
	public static final Comment TEST_COMMENT = getTestComment(TEST_COMMENT_ID, TEST_COMMENT_TEXT);
	public static final Comment TEST_COMMENT_1 = getTestComment(TEST_COMMENT_ID_1, TEST_COMMENT_TEXT_1);
	public static final List<Comment> TEST_COMMENTS_LIST = List.of(TEST_COMMENT, TEST_COMMENT_1);

	private static User getTestUser() {
		User testUser = new User();
		testUser.setId(TEST_USER_ID);
		testUser.setEmail(TEST_USER_EMAIL);
		testUser.setFirstName(TEST_USER_FIRSTNAME);
		testUser.setLastName(TEST_USER_LASTNAME);
		testUser.setPhone(TEST_USER_PHONE);
		testUser.setRole(TEST_USER_ROLE_ADMIN);
		testUser.setRegistrationDate(TEST_USER_REGISTRATION_DATE);
		testUser.setUsername(TEST_USERNAME);
		testUser.setPassword(TEST_USER_PASSWORD);
		return testUser;
	}

	@SneakyThrows
	private static Image getTestImage() {
		Image image = new Image();
		File file = ResourceUtils.getFile(PATH_TO_TEST_IMAGE + FILENAME_TEST_IMAGE);
		image.setId(TEST_IMAGE_ID);
		image.setMediaType(Files.probeContentType(Path.of(file.getPath())));
		image.setImage(Files.readAllBytes(file.toPath()));
		return image;
	}

	private static Ad getTestAd(Integer id, Integer price, String title, String description) {
		Ad ad = new Ad();
		ad.setId(id);
		ad.setPrice(price);
		ad.setTitle(title);
		ad.setDescription(description);
		return ad;
	}

	private static Comment getTestComment(Integer id, String text) {
		Comment comment = new Comment();
		comment.setCreatedAt(TEST_CREATED_AT);
		comment.setId(id);
		comment.setText(text);
		comment.setAuthor(getTestUser());
		return comment;
	}
}
