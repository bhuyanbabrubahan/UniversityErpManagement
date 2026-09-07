package com.sms.fee.repository;


import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.FeeStructure;
import com.sms.fee.enums.FeeType;



public interface FeeStructureRepository
        extends JpaRepository<FeeStructure, Long>,
                JpaSpecificationExecutor<FeeStructure> {


    // Find by id

    Optional<FeeStructure> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );


    // Get all active records

    Page<FeeStructure> findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );


    


    // Duplicate check for create

    boolean existsByCourseIdAndSemesterIdAndFeeTypeAndAcademicYearAndStatusNot(
            Long courseId,
            Long semesterId,
            FeeType feeType,
            String academicYear,
            RecordStatus status
    );


    // Duplicate check for update

    boolean existsByCourseIdAndSemesterIdAndFeeTypeAndAcademicYearAndStatusNotAndIdNot(
            Long courseId,
            Long semesterId,
            FeeType feeType,
            String academicYear,
            RecordStatus status,
            Long id
    );


    // Dashboard count

    long countByStatusNot(
            RecordStatus status
    );
    
 // Calculate total fee

    @Query("""
            SELECT COALESCE(SUM(f.amount),0)
            FROM FeeStructure f
            WHERE f.course.id = :courseId
            AND f.semester.id = :semesterId
            AND f.academicYear = :academicYear
            AND f.status = com.sms.enums.RecordStatus.ACTIVE
            """)
    BigDecimal findTotalFeeByCourseAndSemester(
            @Param("courseId") Long courseId,
            @Param("semesterId") Long semesterId,
            @Param("academicYear") String academicYear
    );

}