package com.sms.erp.hod.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.enums.RecordStatus;
import com.sms.erp.hod.entity.Hod;


public interface HodRepository
        extends JpaRepository<Hod, Long>,
                JpaSpecificationExecutor<Hod> {

    // =====================================================
    // FIND BY CODE
    // =====================================================

    Optional<Hod> findByCode(String code);

    // =====================================================
    // DUPLICATE CODE
    // =====================================================

    boolean existsByCode(String code);

    // =====================================================
    // FIND NON-DELETED RECORD
    // =====================================================

    Optional<Hod> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );
}
