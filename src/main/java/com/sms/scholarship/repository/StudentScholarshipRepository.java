package com.sms.scholarship.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.scholarship.enums.ScholarshipStatus;

import com.sms.enums.RecordStatus;
import com.sms.scholarship.entity.StudentScholarship;



public interface StudentScholarshipRepository
        extends JpaRepository<StudentScholarship, Long>,
                JpaSpecificationExecutor<StudentScholarship> {



    // =====================================================
    // Find By Id (Soft Delete)
    // =====================================================


    Optional<StudentScholarship> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );





    // =====================================================
    // Get All Active Records
    // =====================================================


    Page<StudentScholarship> findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );





    // =====================================================
    // Duplicate Scholarship Check
    // =====================================================


    boolean existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNot(
            Long studentId,
            Long admissionId,
            String academicYear,
            RecordStatus status
    );





    // =====================================================
    // Duplicate Check For Update
    // =====================================================


    boolean existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNotAndIdNot(
            Long studentId,
            Long admissionId,
            String academicYear,
            RecordStatus status,
            Long id
    );



		
			
		 // =====================================================
		 // Fee Dashboard
		 // =====================================================
		
		 // Total Active Scholarships
		 long countByStatusNot(
		         RecordStatus status
		 );
		
		 // Total Approved Scholarship Amount
		 @Query("""
		        SELECT COALESCE(SUM(s.approvedAmount),0)
		        FROM StudentScholarship s
		        WHERE s.status <> :status
		        AND s.scholarshipStatus=:scholarshipStatus
		        """)
		 BigDecimal getTotalApprovedScholarshipAmount(
		         @Param("status")
		         RecordStatus status,
		
		         @Param("scholarshipStatus")
		         ScholarshipStatus scholarshipStatus
		 );
		
		 // Total Approved Scholarship Students
		 
		 long countByScholarshipStatusAndStatusNot(
		         ScholarshipStatus scholarshipStatus, 
		         RecordStatus status
		 );
		 
		 List<StudentScholarship> findByStudentIdAndStatusNotOrderByCreatedAtDesc(
			        Long studentId,
			        RecordStatus status
			);
		 
		 List<StudentScholarship> findTop10ByStatusNotOrderByCreatedAtDesc(
			        RecordStatus status
			);

		 long countByStudent_Id(Long studentId);
}