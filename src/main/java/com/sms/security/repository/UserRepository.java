package com.sms.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByRole(Role admin);

	Optional<User> findByEmailAndStatus(String email, UserStatus status);

	boolean existsByMobileNumber(String mobileNumber);

	Optional<User> findByMobileNumber(String mobileNumber);

	Long countByRoleAndStatus(Role admin, UserStatus active);
	
	boolean existsByMobileNumberAndIdNot(
	        String mobileNumber,
	        Long userId
	);
	
	Optional<User> findByIdAndRole(Long id, Role role);

	Optional<User> findByIdAndRoleAndStatus(Long id, Role role, UserStatus status);
	
	// =====================================================
	// STUDENT USER RECONCILIATION
	// =====================================================

	

	List<User> findByRole(Role role);

	

	// =====================================================
	// STUDENT USER MATCHING
	// =====================================================

	@Query("""
	    select u
	    from User u
	    where lower(trim(u.email)) = lower(trim(:email))
	    and u.role = :role
	    and u.status = :status
	    and not exists (
	        select s.id
	        from Student s
	        where s.user = u
	    )
	    """)
	List<User> findMatchingUsersByEmail(
	        String email,
	        Role role,
	        UserStatus status
	);

	@Query("""
	    select u
	    from User u
	    where trim(u.mobileNumber) = trim(:mobileNumber)
	    and u.role = :role
	    and u.status = :status
	    and not exists (
	        select s.id
	        from Student s
	        where s.user = u
	    )
	    """)
	List<User> findMatchingUsersByMobile(
	        String mobileNumber,
	        Role role,
	        UserStatus status
	);
	
	@Query("""
		    select u
		    from User u
		    where u.role = :role
		    and u.status = :status
		    and u.student is null
		    order by u.id
		    """)
		List<User> findUnmappedUsersByRoleAndStatus(
		        @Param("role") Role role,
		        @Param("status") UserStatus status
		);
	
}