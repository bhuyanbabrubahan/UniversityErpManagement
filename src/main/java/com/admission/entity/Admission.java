package com.admission.entity;

import java.time.LocalDate;

import com.admission.enums.AdmissionStatus;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Course;
import com.sms.entity.Department;
import com.sms.entity.Semester;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "admissions", indexes = {

		@Index(name = "idx_student", columnList = "student_id"),

		@Index(name = "idx_department", columnList = "department_id"),

		@Index(name = "idx_course", columnList = "course_id"),

		@Index(name = "idx_admission_status", columnList = "admission_status")

})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Admission extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "admission_number", nullable = false, unique = true, length = 20)
	private String admissionNumber;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@Column(name = "academic_year", nullable = false, length = 20)
	private String academicYear;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "semester_id", nullable = false)
	private Semester semester;

	@Column(name = "admission_date", nullable = false)
	private LocalDate admissionDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "admission_status", nullable = false)
	private AdmissionStatus admissionStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "record_status", nullable = false)
	private RecordStatus status;

	@Column(length = 500)
	private String remarks;

}