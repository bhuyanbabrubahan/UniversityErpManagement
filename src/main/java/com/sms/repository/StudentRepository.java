package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.security.entity.User;

public interface StudentRepository
        extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByIdAndStatus(Long id, RecordStatus status);

    Optional<Student> findByIdAndStatusNot(Long id, RecordStatus status);

    Page<Student> findByStatus(RecordStatus status, Pageable pageable);

    long countByDepartmentIdAndStatus(Long departmentId, RecordStatus status);

    boolean existsByDepartment_Id(Long departmentId);

    long countByDepartment_Id(Long departmentId);

    Optional<Student> findByRollNumberAndStatus(
            String rollNumber,
            RecordStatus status
    );

    boolean existsByUserId(Long userId);

    boolean existsByAddressId(Long addressId);

    Optional<Student> findByUser(User user);

    Optional<Student> findByUserId(Long userId);

    
    Optional<Student> findByUserIdAndStatus(
            Long userId,
            RecordStatus status
    );

    // =====================================================
    // STUDENT DETAIL FETCH
    // =====================================================

    @EntityGraph(attributePaths = {
            // Student relations
            "department",
            "course",
            "currentSemester",
            "user",

            // Address
            "address",

            // Address hierarchy
            "address.village",
            "address.village.tehsil",
            "address.village.tehsil.district",
            "address.village.tehsil.district.state",
            "address.village.tehsil.district.state.country"
    })
    Optional<Student> findDetailedByIdAndStatus(
            Long id,
            RecordStatus status
    );

	 // =====================================================
	 // STUDENT USER MAPPING
	 // =====================================================
	
	 @EntityGraph(attributePaths = {"user"})
	 Optional<Student> findDetailedMappingByIdAndStatus(
	         Long id,
	         RecordStatus status
	 );
    
    // =====================================================
    // DASHBOARD
    // =====================================================

    long countByStatus(RecordStatus status);
    
    
    boolean existsByUserIdAndIdNot(
    		Long userId,
    		Long studentId);

	long countByUserIsNotNullAndStatus(RecordStatus status);

	long countByUserIsNullAndStatus(RecordStatus status);

	@Query("""
			select s
			from Student s
			where s.status = :status
			and s.user is null
			order by s.id
			""")
	List<Student> findStudentsWithoutUser(
			@Param("status") RecordStatus status
	);

	@Query("""
			select s
			from Student s
			where s.status = :status
			and s.user is not null
			and s.user.role <> com.sms.security.enums.Role.STUDENT
			order by s.id
			""")
	List<Student> findStudentsWithWrongUserRole(
			@Param("status") RecordStatus status
	);
	
	
	
	//=================================
	// =====================================================
	// FACULTY ATTENDANCE - ELIGIBLE STUDENTS
	// =====================================================

	@Query("""
	        select s
	        from Student s
	        where s.status = :status
	        and s.studentStatus = com.sms.enums.StudentStatus.ACTIVE
	        and s.currentSemester.id = :semesterId
	        and s.course.id = :courseId
	        order by s.rollNumber asc
	        """)
	List<Student> findEligibleStudentsForAttendance(
	        @Param("semesterId") Long semesterId,
	        @Param("courseId") Long courseId,
	        @Param("status") RecordStatus status
	);

	
	
}