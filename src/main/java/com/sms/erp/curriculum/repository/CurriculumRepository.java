package com.sms.erp.curriculum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.erp.curriculum.entity.Curriculum;
import com.sms.enums.RecordStatus;

public interface CurriculumRepository
        extends JpaRepository<Curriculum, Long>,
                JpaSpecificationExecutor<Curriculum> {

    // =====================================================
    // FIND BY CODE
    // =====================================================

    Optional<Curriculum> findByCode(String code);

    // =====================================================
    // CHECK CODE
    // =====================================================

    boolean existsByCode(String code);

    // =====================================================
    // NON-DELETED
    // =====================================================

    Optional<Curriculum> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );

    boolean existsByCodeAndStatusNot(
            String code,
            RecordStatus status
    );
}
