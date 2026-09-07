package com.sms.erp.curriculum.dto;

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
public class CurriculumRequestDto {

    private String code;

    private String academicYear;

    private String course;

    private String semester;

    private String subject;

    private String credits;

    private String contactHours;

    private String status;

    private String description;
}
