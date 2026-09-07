package com.sms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "faculty_subject_mapping")
@Getter
@Setter
@NoArgsConstructor
public class FacultySubjectMapping extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faculty_id", nullable = false)
	@ToString.Exclude
	private Faculty faculty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable = false)
	@ToString.Exclude
	private Subject subject;
	
	@ManyToMany
	private List<Student> students;


	@Column(nullable = false)
	private LocalDate assignedDate;

	@Column(nullable = false, length = 20)
	private String academicYear;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RecordStatus status;


}