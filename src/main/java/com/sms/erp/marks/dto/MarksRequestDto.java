package com.sms.erp.marks.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MarksRequestDto {
    private String code;
    private String academicYear;
    private String semester;
    private String exam;
    private String course;
    private String subject;
    private String student;
    private String faculty;
    private Integer maxMarks;
    private Integer obtainedMarks;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
