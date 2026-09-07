package com.sms.erp.marks.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_marks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Marks {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
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
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
