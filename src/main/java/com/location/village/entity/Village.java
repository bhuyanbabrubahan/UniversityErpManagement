package com.location.village.entity;

import com.location.enums.MasterStatus;
import com.location.tehsil.entity.Tehsil;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "villages", uniqueConstraints = { @UniqueConstraint(name = "uk_tehsil_village", columnNames = {
		"tehsil_id", "village_name", "pincode" }) }, indexes = {
				@Index(name = "idx_village_tehsil", columnList = "tehsil_id"),
				@Index(name = "idx_village_pincode", columnList = "pincode") })
public class Village extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "village_name", nullable = false, length = 200)
	private String villageName;

	@NotBlank(message = "Pincode is required")
	@Pattern(
	    regexp = "^[1-9][0-9]{5}$",
	    message = "Pincode must be exactly 6 digits"
	)
	private String pincode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MasterStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tehsil_id", nullable = false)
	private Tehsil tehsil;
}