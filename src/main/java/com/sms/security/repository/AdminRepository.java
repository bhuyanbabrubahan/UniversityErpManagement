package com.sms.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.enums.RecordStatus;
import com.sms.security.entity.Admin;

public interface AdminRepository
        extends JpaRepository<Admin, Long>,
                JpaSpecificationExecutor<Admin> {


    // Find admin by id and ignore deleted records
    Optional<Admin> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );


    // Find admin by logged-in user and active status
    Optional<Admin> findByUserIdAndStatus(
            Long userId,
            RecordStatus status
    );


    // Find admin by user and ignore deleted records
    Optional<Admin> findByUserIdAndStatusNot(
            Long userId,
            RecordStatus status
    );


    // Check duplicate employee code except deleted
    boolean existsByEmployeeCodeAndStatusNot(
            String employeeCode,
            RecordStatus status
    );


    // Pagination excluding deleted records
    Page<Admin> findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );

    long countByStatusNot(RecordStatus status);
    
    boolean existsByEmployeeCode(String employeeCode);

}