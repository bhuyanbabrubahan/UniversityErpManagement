package com.sms.erp.curriculum.entity;


import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "erp_curriculum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curriculum extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "course")
    private String course;

    @Column(name = "semester")
    private String semester;

    @Column(name = "subject")
    private String subject;

    @Column(name = "credits")
    private String credits;

    @Column(name = "contact_hours")
    private String contactHours;

	@Enumerated(EnumType.STRING)
	@Column(name = "record_status", nullable = false)
	private RecordStatus status;

    @Column(name = "description", nullable = false)
    private String description;
}
