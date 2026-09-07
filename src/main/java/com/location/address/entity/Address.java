package com.location.address.entity;

import com.location.address.enums.AddressType;
import com.location.village.entity.Village;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "house_number", nullable = false, length = 100)
	private String houseNumber;

	@Column(nullable = false, length = 150)
	private String street;

	@Column(length = 150)
	private String landmark;

	@Enumerated(EnumType.STRING)
	@Column(name = "address_type", nullable = false)
	private AddressType addressType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "village_id", nullable = false)
	@ToString.Exclude
	private Village village;

	@OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
	@ToString.Exclude
	private Faculty faculty;
	

}