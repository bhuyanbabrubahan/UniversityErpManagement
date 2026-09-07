package com.sms.erp.timetable.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.erp.timetable.entity.Timetable;
import com.sms.enums.RecordStatus;

public interface TimetableRepository
        extends JpaRepository<Timetable, Long>,
                JpaSpecificationExecutor<Timetable> {

    // =====================================================
    // CODE
    // =====================================================

    Optional<Timetable> findByCode(String code);

    // =====================================================
    // NON-DELETED CODE
    // =====================================================

    boolean existsByCodeAndStatusNot(
            String code,
            RecordStatus status
    );

    // =====================================================
    // NON-DELETED RECORD
    // =====================================================

    Optional<Timetable> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );
}