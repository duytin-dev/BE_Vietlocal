package com.vietlocal.app.domain.entity;

import com.vietlocal.app.domain.enums.GuideTier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "guides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String slug;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private GuideTier tier;

	@Column(columnDefinition = "TEXT")
	private String bio;

	@Column(columnDefinition = "TEXT")
	private String styleDescription;

	private String imageUrl;

	private String languages;

	@Builder.Default
	private double rating = 5.0;

	@Column(name = "price_per_day", nullable = false)
	private BigDecimal pricePerDay;
}
