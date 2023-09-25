package com.example.diploma.service.impl;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.AdsDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.model.Ad;
import com.example.diploma.model.Image;
import com.example.diploma.model.User;
import com.example.diploma.repository.AdRepository;
import com.example.diploma.repository.ImageRepository;
import com.example.diploma.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;

import static com.example.diploma.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

	private Ad ad = new Ad();
	@InjectMocks
	private AdServiceImpl adService;
	@Mock
	private AdRepository adRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ImageRepository imageRepository;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private Authentication authentication;

	@BeforeEach
	void setUp() {
		ad.setId(TEST_AD_ID);
//		ad.setImage(TEST_IMAGE);
		ad.setTitle(TEST_AD_TITLE);
		ad.setPrice(TEST_AD_PRICE);
		ad.setComments(TEST_COMMENTS_LIST);
		ad.setAuthor(TEST_USER);
		ad.setDescription(TEST_AD_DESCRIPTION);

		TEST_ADS_LIST_SIZE_2.get(0).setImage(TEST_IMAGE);
		TEST_ADS_LIST_SIZE_2.get(0).setAuthor(TEST_USER);
		TEST_ADS_LIST_SIZE_2.get(0).setComments(TEST_COMMENTS_LIST);

		TEST_USER_1.setEmail(TEST_USER_EMAIL_1);
		TEST_USER_1.setUsername(TEST_USERNAME_1);
		TEST_USER_1.setFirstName(TEST_USER_FIRSTNAME_1);
		TEST_ADS_LIST_SIZE_2.get(1).setImage(TEST_IMAGE);
		TEST_ADS_LIST_SIZE_2.get(1).setAuthor(TEST_USER_1);
		TEST_ADS_LIST_SIZE_2.get(1).setComments(TEST_COMMENTS_LIST);

		SecurityContextHolder.setContext(securityContext);

		lenient().when(userRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(TEST_USER));
		lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
		lenient().when(authentication.getName()).thenReturn(TEST_USERNAME);
	}

	@Test
	void adExistsTest() {
		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.of(ad));

		boolean response = adService.adExists(TEST_AD_ID);

		verify(adRepository, times(1)).findById(TEST_AD_ID);

		assertTrue(response);
	}

	@Test
	void adDoesNotExistsTest() {
		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.empty());

		boolean response = adService.adExists(TEST_AD_ID);

		verify(adRepository, times(1)).findById(TEST_AD_ID);

		assertFalse(response);
	}

	@Test
	void getAllAdsTest() {
		when(adRepository.findAll()).thenReturn(TEST_ADS_LIST_SIZE_2);

		AdsDTO adsDTO = adService.getAllAds();

		verify(adRepository, times(1)).findAll();

		assertNotNull(adsDTO);
		assertEquals(adsDTO.getResults().size(), TEST_ADS_LIST_SIZE_2.size());
		assertEquals(TEST_ADS_LIST_SIZE_2.get(0).getDescription(), adsDTO.getResults().get(0).getDescription());
	}

	@Test
	void getAllAuthorsAdsTest() {
		when(adRepository.findAllByAuthor(TEST_USER)).thenReturn(TEST_ADS_LIST_SIZE_1);

		AdsDTO adsDTO = adService.getAllAuthorsAds();

		verify(userRepository, times(1)).findUserByUsername(TEST_USERNAME);
		verify(adRepository, times(1)).findAllByAuthor(TEST_USER);

		assertEquals(1, adsDTO.getResults().size());
		assertEquals(TEST_USER.getId(), adsDTO.getResults().get(0).getAuthor());
	}

	@Test
	void getAdTest() {
		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.of(TEST_AD));

		assertNotEquals(TEST_AD_ID, TEST_AD_ID_1);

		Optional<Ad> adOptional = adService.getAd(TEST_AD_ID);

		verify(adRepository, times(1)).findById(TEST_AD_ID);

		assertTrue(adOptional.isPresent());
		assertEquals(TEST_AD_PRICE, adOptional.get().getPrice());

		Optional<Ad> adOptional1 = adService.getAd(TEST_AD_ID_1);

		verify(adRepository, times(1)).findById(TEST_AD_ID_1);

		assertTrue(adOptional1.isEmpty());
	}

	@SneakyThrows
	@Test
	void createAdTest() {
		ad.setImage(new Image());
		ad.setAuthor(new User());

		assertNotEquals(ad.getImage().getImage(), TEST_MULTIPART_FILE.getBytes());

		CreateOrUpdateAdDTO createOrUpdateAdDTO = new CreateOrUpdateAdDTO();
		createOrUpdateAdDTO.setDescription(TEST_AD_DESCRIPTION_1);
		createOrUpdateAdDTO.setPrice(TEST_AD_PRICE_1);
		createOrUpdateAdDTO.setTitle(TEST_AD_TITLE_1);

		assertNotEquals(ad.getDescription(), createOrUpdateAdDTO.getDescription());
		assertNotEquals(ad.getPrice(), createOrUpdateAdDTO.getPrice());
		assertNotEquals(ad.getTitle(), createOrUpdateAdDTO.getTitle());

		MultipartFile file = TEST_MULTIPART_FILE;
		Image image = Image.convertToImageFromMultipartFile(file);

		AdDTO response = adService.createAd(createOrUpdateAdDTO, file);

		ad = Ad.convertToAdOnCreate(userRepository.findUserByUsername(TEST_USERNAME).get(), createOrUpdateAdDTO, image);

		verify(userRepository, times(2)).findUserByUsername(TEST_USERNAME);
		verify(imageRepository, times(1)).saveAndFlush(image);
		verify(adRepository, times(1)).save(ad);

		assertEquals(createOrUpdateAdDTO.getDescription(), response.getDescription());
		assertEquals(createOrUpdateAdDTO.getPrice(), response.getPrice());
		assertEquals(createOrUpdateAdDTO.getTitle(), response.getTitle());
		assertEquals(ad.getDescription(), response.getDescription());
		assertEquals(ad.getPrice(), response.getPrice());
		assertEquals(ad.getTitle(), response.getTitle());
		assertNotEquals(new User(), ad.getAuthor());
		assertEquals(TEST_USER, ad.getAuthor());
		assertNotEquals(ad.getImage(), new Image());
		assertEquals(image, ad.getImage());
	}

	@Test
	void updateAd() {
		ad.setImage(TEST_IMAGE);

		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.ofNullable(ad));

		CreateOrUpdateAdDTO createOrUpdateAdDTO = new CreateOrUpdateAdDTO();
		createOrUpdateAdDTO.setDescription(TEST_AD_DESCRIPTION_1);
		createOrUpdateAdDTO.setPrice(TEST_AD_PRICE_1);
		createOrUpdateAdDTO.setTitle(TEST_AD_TITLE_1);

		assertNotEquals(ad.getDescription(), createOrUpdateAdDTO.getDescription());
		assertNotEquals(ad.getPrice(), createOrUpdateAdDTO.getPrice());
		assertNotEquals(ad.getTitle(), createOrUpdateAdDTO.getTitle());

		AdDTO response = adService.updateAd(TEST_AD_ID, createOrUpdateAdDTO);

		Ad.convertToAdOnUpdate(ad, createOrUpdateAdDTO);

		verify(adRepository, times(1)).findById(TEST_AD_ID);
		verify(adRepository, times(1)).saveAndFlush(ad);

		assertEquals(ad.getDescription(), createOrUpdateAdDTO.getDescription());
		assertEquals(ad.getPrice(), createOrUpdateAdDTO.getPrice());
		assertEquals(ad.getTitle(), createOrUpdateAdDTO.getTitle());
		assertEquals(ad.getDescription(), response.getDescription());
		assertEquals(ad.getPrice(), response.getPrice());
		assertEquals(ad.getTitle(), response.getTitle());

	}

	@SneakyThrows
	@Test
	void updateImage() {
		ad.setImage(TEST_EMPTY_IMAGE);

		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.of(ad));

		assertNotEquals(TEST_EMPTY_IMAGE.getMediaType(), TEST_MULTIPART_FILE.getContentType());
		assertNotEquals(TEST_EMPTY_IMAGE.getImage(), TEST_MULTIPART_FILE.getBytes());

		boolean response = adService.updateImage(TEST_AD_ID, TEST_MULTIPART_FILE);

		verify(adRepository, times(1)).findById(TEST_AD_ID);
		verify(imageRepository, times(1)).saveAndFlush(ad.getImage());
		verify(adRepository, times(1)).saveAndFlush(ad);

		assertEquals(ad.getImage().getMediaType(), TEST_MULTIPART_FILE.getContentType());
		assertEquals(ad.getImage().getImage(), TEST_MULTIPART_FILE.getBytes());
		assertTrue(response);
	}

	@Test
	void deleteAd() {
		when(adRepository.findById(TEST_AD_ID)).thenReturn(Optional.of(ad));

		boolean response = adService.deleteAd(TEST_AD_ID);

		verify(adRepository, times(1)).deleteById(TEST_AD_ID);

		assertTrue(response);
	}
}