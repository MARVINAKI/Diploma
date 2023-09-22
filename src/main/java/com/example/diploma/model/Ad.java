package com.example.diploma.model;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.CreateOrUpdateAdDTO;
import com.example.diploma.dto.ExtendedAdDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ads")
public class Ad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "price")
	private Integer price;
	@Column(name = "title")
	private String title;
	@Column(name = "description")
	private String description;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;
	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;
	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	public static AdDTO convertAdToAdDTO(Ad ad) {
		AdDTO adDTO = new AdDTO();
		adDTO.setAuthor(ad.getAuthor().getId());
		adDTO.setImage("/ads/image/" + ad.getImage().getId());
		adDTO.setPk(ad.getId());
		adDTO.setPrice(ad.getPrice());
		adDTO.setTitle(ad.getTitle());
		adDTO.setDescription(ad.getDescription());
		return adDTO;
	}

	public static ExtendedAdDTO convertAdToExtendedAd(Ad ad) {
		ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
		extendedAdDTO.setPk(ad.getId());
		extendedAdDTO.setAuthorFirstName(ad.getAuthor().getFirstName());
		extendedAdDTO.setAuthorLastName(ad.getAuthor().getLastName());
		extendedAdDTO.setDescription(ad.getDescription());
		extendedAdDTO.setEmail(ad.getAuthor().getEmail());
		extendedAdDTO.setImage("/ads/" + ad.getImage().getId() + "/image");
		extendedAdDTO.setPhone(ad.getAuthor().getPhone());
		extendedAdDTO.setPrice(ad.getPrice());
		extendedAdDTO.setTitle(ad.getTitle());
		return extendedAdDTO;
	}

	public static Ad convertToAdOnCreate(User user, CreateOrUpdateAdDTO createOrUpdateAdDTO, Image image) {
		Ad ad = new Ad();
		ad.setAuthor(user);
		ad.setImage(image);
		ad.setTitle(createOrUpdateAdDTO.getTitle());
		ad.setPrice(createOrUpdateAdDTO.getPrice());
		ad.setDescription(createOrUpdateAdDTO.getDescription());
		return ad;
	}

	public static void convertToAdOnUpdate(Ad ad, CreateOrUpdateAdDTO createOrUpdateAdDTO) {
		ad.setTitle(createOrUpdateAdDTO.getTitle());
		ad.setPrice(createOrUpdateAdDTO.getPrice());
		ad.setDescription(createOrUpdateAdDTO.getDescription());
	}
}