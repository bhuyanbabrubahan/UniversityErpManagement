package com.sms.entity;

import java.time.LocalDate;
import java.util.List;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(
    name = "semesters",
    indexes = {

        @Index(
            name = "idx_semester_course",
            columnList = "course_id"
        ),

        @Index(
            name = "idx_semester_status",
            columnList = "status"
        )

    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Semester extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 50
    )
    private String semesterName;

    @Column(nullable = false)
    private Integer semesterNumber;

    @Column(
            name = "semester_start_date",
            nullable = false
    )
    private LocalDate semesterStartDate;

    @Column(
            name = "semester_end_date",
            nullable = false
    )
    private LocalDate semesterEndDate;

    @Column(
            name = "total_working_days"
    )
    private Integer totalWorkingDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    @ToString.Exclude
    private Course course;

    @OneToMany(
            mappedBy = "semester",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private List<Subject> subjects;
    
    

}