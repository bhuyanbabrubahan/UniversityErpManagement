package com.location.country.entity;

import java.util.ArrayList;
import java.util.List;

import com.location.enums.MasterStatus;
import com.location.state.entity.State;
import com.sms.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries", uniqueConstraints = {
		@UniqueConstraint(name = "uk_country_code", columnNames = "country_code") }, indexes = {
				@Index(name = "idx_country_name", columnList = "country_name") })

public class Country extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "country_name", nullable = false, length = 100)
	private String countryName;

	@Column(name = "country_code", nullable = false, unique = true, length = 10)
	private String countryCode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MasterStatus status;

	@ToString.Exclude
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private List<State> states = new ArrayList<>();

}
