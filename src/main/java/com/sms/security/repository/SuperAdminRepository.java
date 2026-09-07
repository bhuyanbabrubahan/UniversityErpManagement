package com.sms.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sms.enums.RecordStatus;
import com.sms.security.entity.SuperAdmin;

@Repository
public interface SuperAdminRepository 
        extends JpaRepository<SuperAdmin,Long>,
        JpaSpecificationExecutor<SuperAdmin>{


    Optional<SuperAdmin>
    findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );


    Page<SuperAdmin>
    findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );
    
    
    // Find Super Admin by user and ignore deleted records
    Optional<SuperAdmin> findByUserIdAndStatusNot(
            Long userId,
            RecordStatus status
    );
    
    
    // Find Super Admin by logged-in user and active status
    Optional<SuperAdmin> findByUserIdAndStatus(
            Long userId,
            RecordStatus status
    );

    long countByStatusNot(RecordStatus status);

}