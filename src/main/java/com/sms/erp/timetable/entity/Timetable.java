package com.sms.erp.timetable.entity;

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
@Table(name = "erp_timetable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timetable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "code",
            nullable = false,
            unique = true
    )
    private String code;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "semester")
    private String semester;

    @Column(name = "course")
    private String course;

    @Column(name = "section")
    private String section;

    @Column(name = "subject")
    private String subject;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "room")
    private String room;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "period")
    private String period;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "record_status",
            nullable = false
    )
    private RecordStatus status;

    @Column(
            name = "description",
            nullable = false
    )
    private String description;
}