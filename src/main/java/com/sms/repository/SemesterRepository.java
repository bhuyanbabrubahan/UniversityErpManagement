package com.sms.repository;


import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Semester;
import com.sms.enums.RecordStatus;


public interface SemesterRepository 
        extends JpaRepository<Semester, Long>,
                JpaSpecificationExecutor<Semester> {



    // ==========================
    // FIND ACTIVE RECORD
    // ==========================

    Optional<Semester> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );



    // ==========================
    // PAGINATION
    // EXCLUDE DELETED
    // ==========================

    Page<Semester> findByStatusNot(
    		RecordStatus status,
            Pageable pageable
    );



    // ==========================
    // DUPLICATE SEMESTER CHECK
    // Course + Semester Number
    // ==========================


    boolean existsByCourseIdAndSemesterNumber(
            Long courseId,
            Integer semesterNumber
    );



    boolean existsByCourseIdAndSemesterNumberAndIdNot(
            Long courseId,
            Integer semesterNumber,
            Long id
    );



    // ==========================
    // DATE OVERLAP CHECK
    // CREATE
    // ==========================


    @Query("""
            SELECT COUNT(s) > 0
            FROM Semester s
            WHERE s.course.id = :courseId

            AND s.status <> com.sms.enums.RecordStatus.DELETED

            AND (
                    :startDate <= s.semesterEndDate
                    AND
                    :endDate >= s.semesterStartDate
            )
            """)
    boolean existsSemesterOverlap(
            @Param("courseId") Long courseId,

            @Param("startDate") LocalDate startDate,

            @Param("endDate") LocalDate endDate
    );




    // ==========================
    // DATE OVERLAP CHECK
    // UPDATE
    // Ignore Current ID
    // ==========================


    @Query("""
            SELECT COUNT(s) > 0
            FROM Semester s
            WHERE s.course.id = :courseId

            AND s.id <> :id

            AND s.status <> com.sms.enums.RecordStatus.DELETED

            AND (
                    :startDate <= s.semesterEndDate
                    AND
                    :endDate >= s.semesterStartDate
            )
            """)
    boolean existsSemesterOverlapForUpdate(
            @Param("id") Long id,

            @Param("courseId") Long courseId,

            @Param("startDate") LocalDate startDate,

            @Param("endDate") LocalDate endDate
    );

    
    //DASHBOARD
    long countByStatusNot(RecordStatus status);

}