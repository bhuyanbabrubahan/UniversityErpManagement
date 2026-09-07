package com.sms.erp.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.*;
import com.sms.erp.transport.entity.Transport;

public interface TransportRepository extends JpaRepository<Transport, Long>, JpaSpecificationExecutor<Transport> {
    Optional<Transport> findByCode(String code);
    boolean existsByCode(String code);
}
