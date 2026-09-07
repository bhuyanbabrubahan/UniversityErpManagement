package com.sms.audit.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.sms.audit.dto.AuditLogResponseDto;
import com.sms.audit.dto.AuditLogSearchRequestDto;
import com.sms.audit.entity.AuditLog;
import com.sms.audit.mapper.AuditLogMapper;
import com.sms.audit.repository.AuditLogRepository;
import com.sms.audit.service.AuditLogService;
import com.sms.audit.specification.AuditLogSpecification;
import com.sms.enums.RecordStatus;
import com.sms.exception.ResourceNotFoundException;




@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {


    private static final Logger log =
            LoggerFactory.getLogger(AuditLogServiceImpl.class);



    private final AuditLogRepository auditLogRepository;

    private final AuditLogMapper auditLogMapper;




    // =====================================
    // CREATE AUDIT LOG
    // =====================================

    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public AuditLogResponseDto createAuditLog(
            AuditLogResponseDto request
    ) {


        log.info(
            "Creating audit log for module : {}",
            request.getModuleName()
        );


        AuditLog auditLog =
                AuditLog.builder()

                .action(request.getAction())

                .moduleName(request.getModuleName())

                .description(request.getDescription())

                .performedBy(request.getPerformedBy())

                .role(request.getRole())

                .ipAddress(request.getIpAddress())

                .requestMethod(request.getRequestMethod())

                .requestUrl(request.getRequestUrl())

                .timestamp(request.getTimestamp())

                .status(RecordStatus.ACTIVE)

                .build();



        AuditLog savedAuditLog =
                auditLogRepository.save(auditLog);



        log.info(
            "Audit log created successfully with id : {}",
            savedAuditLog.getId()
        );



        return auditLogMapper.convertToDto(
                savedAuditLog
        );

    }





    // =====================================
    // GET BY ID
    // =====================================

    @Override
    public AuditLogResponseDto getAuditLogById(
            Long id
    ) {


        log.info(
            "Fetching audit log by id : {}",
            id
        );


        AuditLog auditLog =
                auditLogRepository
                .findByIdAndStatus(
                        id,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        "Audit log not found with id : "
                        + id
                    )
                );



        return auditLogMapper.convertToDto(
                auditLog
        );

    }





    // =====================================
    // GET ALL
    // =====================================

    @Override
    public Page<AuditLogResponseDto> getAllAuditLogs(
            Pageable pageable
    ) {


        log.info(
            "Fetching all audit logs"
        );



        return auditLogRepository
                .findByStatus(
                    RecordStatus.ACTIVE,
                    pageable
                )
                .map(
                    auditLogMapper::convertToDto
                );

    }





    // =====================================
    // SEARCH
    // =====================================

    @Override
    public Page<AuditLogResponseDto> searchAuditLogs(
            AuditLogSearchRequestDto request,
            Pageable pageable
    ) {



        log.info(
            "Searching audit logs"
        );



        Specification<AuditLog> specification =
                AuditLogSpecification.search(request);



        return auditLogRepository
                .findAll(
                    specification,
                    pageable
                )
                .map(
                    auditLogMapper::convertToDto
                );

    }





    // =====================================
    // COUNT
    // =====================================

    @Override
    public long countAuditLogs() {


        return auditLogRepository
                .countByStatus(
                    RecordStatus.ACTIVE
                );

    }


}