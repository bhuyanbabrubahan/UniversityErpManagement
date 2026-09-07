package com.sms.erp.certificate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.certificate.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {
    Optional<Certificate> findByCode(String code);
    boolean existsByCode(String code);
}
