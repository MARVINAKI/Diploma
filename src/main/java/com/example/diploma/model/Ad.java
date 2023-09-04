package com.example.diploma.model;

import com.example.diploma.dto.AdDTO;
import com.example.diploma.dto.CreateOrUpdateAd;
import com.example.diploma.dto.ExtendedAd;
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

	@OneToOne
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
		adDTO.setImage(ad.getImage());
		adDTO.setPk(ad.getId());
		adDTO.setPrice(ad.getPrice());
		adDTO.setTitle(ad.getTitle());
		adDTO.setDescription(ad.getDescription());
		return adDTO;
	}

	public static ExtendedAd convertAdToExtendedAd(Ad ad) {
		ExtendedAd extendedAd = new ExtendedAd();
		extendedAd.setPk(ad.getId());
		extendedAd.setAuthorFirstName(ad.getAuthor().getFirstName());
		extendedAd.setAuthorLastName(ad.getAuthor().getLastName());
		extendedAd.setDescription(ad.getDescription());
		extendedAd.setEmail(ad.getAuthor().getEmail());
		extendedAd.setImage(ad.getImage());
		extendedAd.setPhone(ad.getAuthor().getPhone());
		extendedAd.setPrice(ad.getPrice());
		extendedAd.setTitle(ad.getTitle());
		return extendedAd;
	}

	public static Ad convertAdDtoToAd(User author, AdDTO adDTO) {
		Ad ad = new Ad();
		ad.setImage(adDTO.getImage());
		ad.setPrice(adDTO.getPrice());
		ad.setTitle(adDTO.getTitle());
		ad.setDescription(adDTO.getDescription());
		ad.setAuthor(author);
		return ad;
	}

	public static void convertOnAdUpdate(Ad ad, CreateOrUpdateAd createOrUpdateAd) {
		ad.setTitle(createOrUpdateAd.getTitle());
		ad.setPrice(createOrUpdateAd.getPrice());
		ad.setDescription(createOrUpdateAd.getDescription());
	}
}