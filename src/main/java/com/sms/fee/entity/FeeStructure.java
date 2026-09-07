package com.sms.fee.entity;


import java.math.BigDecimal;

import com.sms.common.entity.BaseEntity;
import com.sms.entity.Course;
import com.sms.entity.Semester;
import com.sms.enums.RecordStatus;
import com.sms.fee.enums.FeeType;

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
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
    name = "fee_structures",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_course_semester_fee_type",
            columnNames = {
                "course_id",
                "semester_id",
                "fee_type"
            }
        )
    }
)
@Getter
@Setter
public class FeeStructure extends BaseEntity {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="course_id",
            nullable=false
    )
    private Course course;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="semester_id",
            nullable=false
    )
    private Semester semester;



    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private FeeType feeType;



    @Column(
            nullable=false,
            precision=10,
            scale=2
    )
    private BigDecimal amount;



    private String academicYear;


    @Enumerated(EnumType.STRING)
    private RecordStatus status;

}