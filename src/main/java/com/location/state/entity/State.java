package com.location.state.entity;

import java.util.ArrayList;
import java.util.List;

import com.location.country.entity.Country;
import com.location.district.entity.District;
import com.location.enums.MasterStatus;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "states", uniqueConstraints = {
		@UniqueConstraint(name = "uk_country_state", columnNames = { "country_id", "state_name" }) }, indexes = {
				@Index(name = "idx_state_country", columnList = "country_id"),
				@Index(name = "idx_state_code", columnList = "state_code") })
public class State extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "state_name", nullable = false, length = 150)
	private String stateName;

	@Column(name = "state_code", nullable = false, length = 20)
	private String stateCode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MasterStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", nullable = false)
	private Country country;

	@ToString.Exclude
	@OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
	private List<District> districts = new ArrayList<>();
}