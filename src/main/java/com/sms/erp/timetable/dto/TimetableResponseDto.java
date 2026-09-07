package com.sms.erp.timetable.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableResponseDto {

    private Long id;

    private String code;

    private String academicYear;

    private String semester;

    private String course;

    private String section;

    private String subject;

    private String faculty;

    private String room;

    private String dayOfWeek;

    private String startTime;

    private String endTime;

    private String period;

    private String status;

    private String description;

    // =====================================================
    // AUDIT
    // =====================================================

    private Long createdByUserId;

    private String createdBy;

    private LocalDateTime createdAt;

    private Long updatedByUserId;

    private String updatedBy;

    private LocalDateTime updatedAt;
}