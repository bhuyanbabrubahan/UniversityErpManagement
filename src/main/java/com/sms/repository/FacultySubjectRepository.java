package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.FacultySubjectMapping;
import com.sms.enums.RecordStatus;

public interface FacultySubjectRepository
		extends JpaRepository<FacultySubjectMapping, Long>, JpaSpecificationExecutor<FacultySubjectMapping> {

	boolean existsByFacultyIdAndSubjectIdAndAcademicYearAndStatusNot(Long facultyId, Long subjectId,
			String academicYear, RecordStatus status);

	boolean existsByFacultyIdAndSubjectIdAndAcademicYearAndStatusNotAndIdNot(Long facultyId, Long subjectId,
			String academicYear, RecordStatus status, Long id);

	Optional<FacultySubjectMapping> findByIdAndStatusNot(Long id, RecordStatus status);

	Page<FacultySubjectMapping> findByStatusNot(RecordStatus status, Pageable pageable);

	long countByFacultyIdAndStatusNot(Long facultyId, RecordStatus status);

	// DASHBOARD
	long countByStatusNot(RecordStatus status);
	
	
	
	//======================
	// =====================================================
	// FACULTY ATTENDANCE - DETAILED MAPPING
	// =====================================================

	@Query("""
		    SELECT fsm
		    FROM FacultySubjectMapping fsm
		    JOIN FETCH fsm.faculty f
		    JOIN FETCH fsm.subject s
		    WHERE fsm.id = :id
		      AND fsm.faculty.id = :facultyId
		      AND fsm.status <> :status
		""")
		Optional<FacultySubjectMapping> findDetailedByIdAndFacultyIdAndStatusNot(
		        @Param("id") Long id,
		        @Param("facultyId") Long facultyId,
		        @Param("status") RecordStatus status
		);

	
	List<FacultySubjectMapping>
	findByFacultyIdAndAcademicYearAndStatus(
	        Long facultyId,
	        String academicYear,
	        RecordStatus status
	);



}