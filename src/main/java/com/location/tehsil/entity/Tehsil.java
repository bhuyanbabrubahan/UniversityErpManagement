package com.location.tehsil.entity;

import java.util.ArrayList;
import java.util.List;

import com.location.district.entity.District;
import com.location.enums.MasterStatus;
import com.location.village.entity.Village;
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
@Table(name = "tehsils", uniqueConstraints = {
		@UniqueConstraint(name = "uk_district_tehsil", columnNames = { "district_id", "tehsil_name" }) }, indexes = {
				@Index(name = "idx_tehsil_district", columnList = "district_id") })
public class Tehsil extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tehsil_name", nullable = false, length = 150)
	private String tehsilName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MasterStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id", nullable = false)
	private District district;

	@ToString.Exclude
	@OneToMany(mappedBy = "tehsil", fetch = FetchType.LAZY)
	private List<Village> villages = new ArrayList<>();

}