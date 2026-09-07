package com.sms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.ExamType;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
        name = "exams",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_exam_subject_date_type",
                        columnNames = {
                                "subject_id",
                                "exam_date",
                                "exam_type"
                        }
                )

        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // Basic Information
    // ==========================

    @Column(nullable = false, length = 100)
    private String examName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExamType examType;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "maximum_marks", nullable = false)
    private Integer maximumMarks;

    @Column(name = "passing_marks", nullable = false)
    private Integer passingMarks;

    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordStatus status;

    // ==========================
    // Relationships
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "semester_id",
            nullable = false
    )
    @ToString.Exclude
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subject_id",
            nullable = false
    )
    @ToString.Exclude
    private Subject subject;
    
    
    
     // =====================================================
 	 // Creation Information
 	 // =====================================================
 	
 	 @Column(
 	         name = "created_by_user_id"
 	 )
 	 private Long createdByUserId;
 	
 	
 	 @Column(
 	         name = "created_by",
 	         length = 100
 	 )
 	 private String createdBy;
 	
 	
 	 @Column(
 	         name = "created_date"
 	 )
 	 private LocalDateTime createdDate;
 	
 	
 	 // =====================================================
 	 // Update Information
 	 // =====================================================
 	
 	 @Column(
 	         name = "updated_by_user_id"
 	 )
 	 private Long updatedByUserId;
 	
 	
 	 @Column(
 	         name = "updated_by",
 	         length = 100
 	 )
 	 private String updatedBy;
 	
 	
 	 @Column(
 	         name = "updated_date"
 	 )
 	 private LocalDateTime updatedDate;
 	 
 	 
 	 

}