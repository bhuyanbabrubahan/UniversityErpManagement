package com.sms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.entity.Course;
import com.sms.enums.RecordStatus;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course>{

	// Fetch single ACTIVE course
	Optional<Course> findByIdAndStatusNot(Long id, RecordStatus status);

	// Get all ACTIVE courses with pagination
	Page<Course> findByStatusNot(RecordStatus status, Pageable pageable);

	// Used while deleting Department
	boolean existsByDepartment_Id(Long departmentId);

	// Duplicate validation (Create)
	boolean existsByCourseName(String courseName);

	boolean existsByCourseCode(String courseCode);

	// Duplicate validation (Update)
	boolean existsByCourseNameAndIdNot(String courseName, Long id);

	boolean existsByCourseCodeAndIdNot(String courseCode, Long id);
	

	long countByDepartment_Id(Long departmentId);
	
	Optional<Course> findByIdAndStatus(Long id, RecordStatus status);
	
	
	//DASHBOARD
	long countByStatusNot(RecordStatus status);
}
