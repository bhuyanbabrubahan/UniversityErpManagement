package com.sms.erp.hod.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.erp.hod.dto.HodRequestDto;
import com.sms.erp.hod.dto.HodResponseDto;
import com.sms.erp.hod.dto.HodSearchDto;
import com.sms.erp.hod.entity.Hod;
import com.sms.erp.hod.mapper.HodMapper;
import com.sms.erp.hod.repository.HodRepository;
import com.sms.erp.hod.service.HodService;
import com.sms.erp.hod.specification.HodSpecification;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HodServiceImpl implements HodService {

    private final HodRepository repository;

    private final HodMapper mapper;

    // =====================================================
    // FIND ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<HodResponseDto> findAll(
            int page,
            int size) {

        validatePagination(page, size);

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "id"
                        )
                );

        return repository
                .findAll(
                        HodSpecification.bySearch(null),
                        pageable
                )
                .map(mapper::toResponse);
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<HodResponseDto> search(
            HodSearchDto search,
            int page,
            int size) {

        validatePagination(page, size);

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "id"
                        )
                );

        return repository
                .findAll(
                        HodSpecification.bySearch(search),
                        pageable
                )
                .map(mapper::toResponse);
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public HodResponseDto findById(Long id) {

        validateId(id);

        Hod hod =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "HOD not found: " + id
                                )
                        );

        return mapper.toResponse(hod);
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public HodResponseDto create(
            HodRequestDto request) {

        validateRequest(request);

        String code =
                request.getCode()
                        .trim()
                        .toUpperCase();

        // -------------------------------------------------
        // DUPLICATE CODE
        // -------------------------------------------------

        if (repository.existsByCode(code)) {

            throw new IllegalArgumentException(
                    "HOD code already exists: " + code
            );
        }

        // -------------------------------------------------
        // DTO -> ENTITY
        // -------------------------------------------------

        Hod hod =
                mapper.toEntity(request);

        hod.setCode(code);

        // -------------------------------------------------
        // STATUS
        // -------------------------------------------------

        hod.setStatus(
                parseCreateStatus(
                        request.getStatus()
                )
        );

        // -------------------------------------------------
        // CURRENT USER
        // -------------------------------------------------

        Long currentUserId =
                SecurityUtils.getCurrentUserId();

        String currentUser =
                SecurityUtils.getCurrentFullName();

        LocalDateTime now =
                LocalDateTime.now();

        // -------------------------------------------------
        // CREATE AUDIT
        // -------------------------------------------------

        hod.setCreatedByUserId(
                currentUserId
        );

        hod.setCreatedBy(
                currentUser
        );

        hod.setCreatedAt(
                now
        );

        // -------------------------------------------------
        // UPDATE AUDIT
        // -------------------------------------------------

        hod.setUpdatedByUserId(
                currentUserId
        );

        hod.setUpdatedBy(
                currentUser
        );

        hod.setUpdatedAt(
                now
        );

        // -------------------------------------------------
        // SAVE
        // -------------------------------------------------

        Hod saved =
                repository.save(hod);

        return mapper.toResponse(saved);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public HodResponseDto update(
            Long id,
            HodRequestDto request) {

        validateId(id);

        validateRequest(request);

        // -------------------------------------------------
        // FIND NON-DELETED RECORD
        // -------------------------------------------------

        Hod current =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "HOD not found: " + id
                                )
                        );

        // -------------------------------------------------
        // NORMALIZE CODE
        // -------------------------------------------------

        String requestedCode =
                request.getCode()
                        .trim()
                        .toUpperCase();

        // -------------------------------------------------
        // DUPLICATE CODE
        // -------------------------------------------------

        if (!current.getCode()
                .equalsIgnoreCase(requestedCode)
                && repository.existsByCode(requestedCode)) {

            throw new IllegalArgumentException(
                    "HOD code already exists: "
                            + requestedCode
            );
        }

        // -------------------------------------------------
        // PRESERVE CREATE AUDIT
        // -------------------------------------------------

        Long createdByUserId =
                current.getCreatedByUserId();

        String createdBy =
                current.getCreatedBy();

        LocalDateTime createdAt =
                current.getCreatedAt();

        // -------------------------------------------------
        // UPDATE NORMAL FIELDS
        // -------------------------------------------------

        mapper.updateEntity(
                request,
                current
        );

        current.setCode(
                requestedCode
        );

        // -------------------------------------------------
        // RESTORE CREATE AUDIT
        // -------------------------------------------------

        current.setCreatedByUserId(
                createdByUserId
        );

        current.setCreatedBy(
                createdBy
        );

        current.setCreatedAt(
                createdAt
        );

        // -------------------------------------------------
        // STATUS
        // -------------------------------------------------

        current.setStatus(
                parseUpdateStatus(
                        request.getStatus()
                )
        );

        // -------------------------------------------------
        // UPDATE AUDIT
        // -------------------------------------------------

        current.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        current.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );

        current.setUpdatedAt(
                LocalDateTime.now()
        );

        // -------------------------------------------------
        // SAVE
        // -------------------------------------------------

        Hod updated =
                repository.save(current);

        return mapper.toResponse(updated);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void delete(Long id) {

        validateId(id);

        // -------------------------------------------------
        // FIND RECORD INCLUDING DELETED
        // -------------------------------------------------

        Hod current =
                repository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "HOD not found: " + id
                                )
                        );

        // -------------------------------------------------
        // ALREADY DELETED
        // -------------------------------------------------

        if (current.getStatus()
                == RecordStatus.DELETED) {

            throw new IllegalArgumentException(
                    "HOD is already deleted: " + id
            );
        }

        // -------------------------------------------------
        // SOFT DELETE
        // -------------------------------------------------

        current.setStatus(
                RecordStatus.DELETED
        );

        // -------------------------------------------------
        // DELETE AUDIT
        // -------------------------------------------------

        current.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        current.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );

        current.setUpdatedAt(
                LocalDateTime.now()
        );

        repository.save(current);
    }

    // =====================================================
    // VALIDATE ID
    // =====================================================

    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid HOD ID: " + id
            );
        }
    }

    // =====================================================
    // VALIDATE PAGINATION
    // =====================================================

    private void validatePagination(
            int page,
            int size) {

        if (page < 0) {

            throw new IllegalArgumentException(
                    "Page number cannot be negative."
            );
        }

        if (size <= 0 || size > 100) {

            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100."
            );
        }
    }

    // =====================================================
    // VALIDATE REQUEST
    // =====================================================

    private void validateRequest(
            HodRequestDto request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "HOD request is required."
            );
        }

        if (isBlank(request.getCode())) {

            throw new IllegalArgumentException(
                    "HOD code is required."
            );
        }

        if (isBlank(request.getEmployeeCode())) {

            throw new IllegalArgumentException(
                    "Employee code is required."
            );
        }

        if (isBlank(request.getFullName())) {

            throw new IllegalArgumentException(
                    "Full name is required."
            );
        }

        if (isBlank(request.getDepartment())) {

            throw new IllegalArgumentException(
                    "Department is required."
            );
        }

        if (isBlank(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Email is required."
            );
        }

        if (isBlank(request.getPhone())) {

            throw new IllegalArgumentException(
                    "Phone is required."
            );
        }

        if (isBlank(request.getDesignation())) {

            throw new IllegalArgumentException(
                    "Designation is required."
            );
        }

        if (request.getStatus() == null) {

            throw new IllegalArgumentException(
                    "Status is required."
            );
        }

        if (isBlank(request.getDescription())) {

            throw new IllegalArgumentException(
                    "Description is required."
            );
        }
    }

    // =====================================================
    // CREATE STATUS
    // =====================================================

    private RecordStatus parseCreateStatus(
            RecordStatus status) {

        if (status == null) {

            throw new IllegalArgumentException(
                    "Status is required."
            );
        }

        if (status == RecordStatus.DELETED) {

            throw new IllegalArgumentException(
                    "New HOD cannot be created "
                            + "with DELETED status."
            );
        }

        return status;
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private RecordStatus parseUpdateStatus(
            RecordStatus status) {

        if (status == null) {

            throw new IllegalArgumentException(
                    "Status is required."
            );
        }

        if (status == RecordStatus.DELETED) {

            throw new IllegalArgumentException(
                    "Use delete operation to delete a HOD."
            );
        }

        return status;
    }

    // =====================================================
    // BLANK CHECK
    // =====================================================

    private boolean isBlank(String value) {

        return value == null
                || value.trim().isEmpty();
    }
}
