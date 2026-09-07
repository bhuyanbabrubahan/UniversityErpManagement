package com.sms.entity;

import java.util.ArrayList;
import java.util.List;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 20)
	private String courseCode;

	@Column(nullable = false, length = 100)
	private String courseName;

	// production best practice (months)
	@Column(nullable = false)
	private Integer durationInMonths;

	@Column(length = 500)
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RecordStatus status;

	// 🔥 Relationship (Many courses → One department)
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	@ToString.Exclude
	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
	private List<Semester> semesters = new ArrayList<>();

	

}
