package com.sms.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Exam;
import com.sms.entity.Student;
import com.sms.enums.ExamType;
import com.sms.enums.RecordStatus;

public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {

	// ==========================
	// Duplicate Check CREATE
	// ==========================

	boolean existsBySubjectIdAndExamDateAndExamTypeAndStatusNot(Long subjectId, LocalDate examDate, ExamType examType,
			RecordStatus status);

	// ==========================
	// Duplicate Check UPDATE
	// ==========================

	boolean existsBySubjectIdAndExamDateAndExamTypeAndStatusNotAndIdNot(Long subjectId, LocalDate examDate,
			ExamType examType, RecordStatus status, Long id);

	// ==========================
	// Find Active Exam
	// ==========================

	@EntityGraph(attributePaths = {
	        "semester",
	        "subject"
	})
	Optional<Exam> findByIdAndStatusNot(Long id, RecordStatus status);

	// ==========================
	// ==========================

	@EntityGraph(attributePaths = {
	        "semester",
	        "subject"
	})
	Page<Exam> findByStatusNot(RecordStatus status, Pageable pageable);

	Optional<Student> findByIdAndStatus(Long examId, RecordStatus active);
	
	//DASHBOARD
	long countByStatusNot(RecordStatus status);

	long countByExamDateAfterAndStatusNot(
	        LocalDate examDate,
	        RecordStatus status
	);

	long countByExamDateBeforeAndStatusNot(
	        LocalDate examDate,
	        RecordStatus status
	);
	
	 
	
	@Query("""
			SELECT COUNT(r)
			FROM Result r
			WHERE r.exam.subject.id IN
			(
			    SELECT fsm.subject.id
			    FROM FacultySubjectMapping fsm
			    WHERE fsm.faculty.id = :facultyId
			)
			AND r.status <> :status
			""")
			long countByFacultyIdAndStatusNot(
			        @Param("facultyId") Long facultyId,
			        @Param("status") RecordStatus status
			);
	
	long countByStatus(RecordStatus status);
	

	
	
	
	@Query("""
			SELECT COUNT(e)
			FROM Exam e
			WHERE e.subject.semester.course.department.id =
			(
			    SELECT s.department.id
			    FROM Student s
			    WHERE s.id = :studentId
			)
			AND e.status <> :status
			""")
			long countByStudentIdAndStatusNot(
			        @Param("studentId") Long studentId,
			        @Param("status") RecordStatus status
			);
	
	
}