package com.sms.erp.installment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.installment.entity.Installment;

public interface InstallmentRepository extends JpaRepository<Installment, Long>, JpaSpecificationExecutor<Installment> {
    Optional<Installment> findByCode(String code);
    boolean existsByCode(String code);
}
