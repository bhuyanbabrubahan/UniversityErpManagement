package com.sms.erp.curriculum.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.erp.curriculum.dto.CurriculumRequestDto;
import com.sms.erp.curriculum.dto.CurriculumResponseDto;
import com.sms.erp.curriculum.dto.CurriculumSearchDto;
import com.sms.erp.curriculum.entity.Curriculum;
import com.sms.erp.curriculum.mapper.CurriculumMapper;
import com.sms.erp.curriculum.repository.CurriculumRepository;
import com.sms.erp.curriculum.service.CurriculumService;
import com.sms.erp.curriculum.specification.CurriculumSpecification;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CurriculumServiceImpl
        implements CurriculumService {

    private final CurriculumRepository repository;

    private final CurriculumMapper mapper;

    // =====================================================
    // FIND ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CurriculumResponseDto> findAll(
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
                        CurriculumSpecification.bySearch(null),
                        pageable
                )
                .map(mapper::toResponse);
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<CurriculumResponseDto> search(
            CurriculumSearchDto search,
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
                        CurriculumSpecification.bySearch(search),
                        pageable
                )
                .map(mapper::toResponse);
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public CurriculumResponseDto findById(Long id) {

        validateId(id);

        Curriculum curriculum =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: "
                                                + id
                                )
                        );

        return mapper.toResponse(curriculum);
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public CurriculumResponseDto create(
            CurriculumRequestDto request) {

        // -------------------------------------------------
        // 1. Validate request
        // -------------------------------------------------

        validateRequest(request);

        // -------------------------------------------------
        // 2. Normalize code
        // -------------------------------------------------

        String code =
                request.getCode()
                        .trim()
                        .toUpperCase();

        // -------------------------------------------------
        // 3. Duplicate code
        //
        // Soft deleted record bhi same code rakhta hai.
        // Isliye globally unique code maintain kar rahe hain.
        // -------------------------------------------------

        if (repository.existsByCode(code)) {

            throw new IllegalArgumentException(
                    "Curriculum code already exists: "
                            + code
            );
        }

        // -------------------------------------------------
        // 4. DTO -> Entity
        // -------------------------------------------------

        Curriculum curriculum =
                mapper.toEntity(request);

        curriculum.setCode(code);

        // -------------------------------------------------
        // 5. Current user
        // -------------------------------------------------

        Long currentUserId =
                SecurityUtils.getCurrentUserId();

        String currentUser =
                SecurityUtils.getCurrentFullName();

        LocalDateTime now =
                LocalDateTime.now();

        // -------------------------------------------------
        // 6. Record status
        // -------------------------------------------------

        curriculum.setStatus(
                parseCreateStatus(
                        request.getStatus()
                )
        );

        // -------------------------------------------------
        // 7. CREATE AUDIT
        // -------------------------------------------------

        curriculum.setCreatedByUserId(
                currentUserId
        );

        curriculum.setCreatedBy(
                currentUser
        );

        curriculum.setCreatedAt(
                now
        );

        // -------------------------------------------------
        // 8. UPDATE AUDIT
        // -------------------------------------------------

        curriculum.setUpdatedByUserId(
                currentUserId
        );

        curriculum.setUpdatedBy(
                currentUser
        );

        curriculum.setUpdatedAt(
                now
        );

        // -------------------------------------------------
        // 9. SAVE
        // -------------------------------------------------

        Curriculum saved =
                repository.save(curriculum);

        return mapper.toResponse(saved);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public CurriculumResponseDto update(
            Long id,
            CurriculumRequestDto request) {

        // -------------------------------------------------
        // 1. Validate ID
        // -------------------------------------------------

        validateId(id);

        // -------------------------------------------------
        // 2. Validate request
        // -------------------------------------------------

        validateRequest(request);

        // -------------------------------------------------
        // 3. Find NON-DELETED record
        // -------------------------------------------------

        Curriculum current =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: "
                                                + id
                                )
                        );

        // -------------------------------------------------
        // 4. Normalize requested code
        // -------------------------------------------------

        String requestedCode =
                request.getCode()
                        .trim()
                        .toUpperCase();

        // -------------------------------------------------
        // 5. Duplicate code
        // -------------------------------------------------

        if (!current.getCode()
                .equalsIgnoreCase(requestedCode)
                && repository.existsByCode(requestedCode)) {

            throw new IllegalArgumentException(
                    "Curriculum code already exists: "
                            + requestedCode
            );
        }

        // -------------------------------------------------
        // 6. Preserve CREATE audit
        // -------------------------------------------------

        Long createdByUserId =
                current.getCreatedByUserId();

        String createdBy =
                current.getCreatedBy();

        LocalDateTime createdAt =
                current.getCreatedAt();

        // -------------------------------------------------
        // 7. Update normal fields
        // -------------------------------------------------

        mapper.updateEntity(
                request,
                current
        );

        current.setCode(
                requestedCode
        );

        // -------------------------------------------------
        // 8. Restore CREATE audit
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
        // 9. Record status
        // -------------------------------------------------

        current.setStatus(
                parseUpdateStatus(
                        request.getStatus()
                )
        );

        // -------------------------------------------------
        // 10. UPDATE audit
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
        // 11. SAVE
        // -------------------------------------------------

        Curriculum updated =
                repository.save(current);

        return mapper.toResponse(updated);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void delete(Long id) {

        // -------------------------------------------------
        // 1. Validate ID
        // -------------------------------------------------

        validateId(id);

        // -------------------------------------------------
        // 2. Find record including DELETED
        // -------------------------------------------------

        Curriculum current =
                repository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Curriculum not found: "
                                                + id
                                )
                        );

        // -------------------------------------------------
        // 3. Already deleted check
        // -------------------------------------------------

        if (current.getStatus()
                == RecordStatus.DELETED) {

            throw new IllegalArgumentException(
                    "Curriculum is already deleted: "
                            + id
            );
        }

        // -------------------------------------------------
        // 4. SOFT DELETE
        // -------------------------------------------------

        current.setStatus(
                RecordStatus.DELETED
        );

        // -------------------------------------------------
        // 5. DELETE AUDIT
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
        // 6. SAVE
        // -------------------------------------------------

        repository.save(current);
    }

    // =====================================================
    // VALIDATE ID
    // =====================================================

    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid curriculum ID: " + id
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
            CurriculumRequestDto request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Curriculum request is required."
            );
        }

        if (isBlank(request.getCode())) {

            throw new IllegalArgumentException(
                    "Curriculum code is required."
            );
        }

        if (isBlank(request.getAcademicYear())) {

            throw new IllegalArgumentException(
                    "Academic year is required."
            );
        }

        if (isBlank(request.getCourse())) {

            throw new IllegalArgumentException(
                    "Course is required."
            );
        }

        if (isBlank(request.getSemester())) {

            throw new IllegalArgumentException(
                    "Semester is required."
            );
        }

        if (isBlank(request.getSubject())) {

            throw new IllegalArgumentException(
                    "Subject is required."
            );
        }

        if (isBlank(request.getCredits())) {

            throw new IllegalArgumentException(
                    "Credits are required."
            );
        }

        if (isBlank(request.getContactHours())) {

            throw new IllegalArgumentException(
                    "Contact hours are required."
            );
        }

        if (isBlank(request.getStatus())) {

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
            String value) {

        if (isBlank(value)) {

            throw new IllegalArgumentException(
                    "Status is required."
            );
        }

        try {

            RecordStatus status =
                    RecordStatus.valueOf(
                            value.trim()
                                    .toUpperCase()
                    );

            if (status == RecordStatus.DELETED) {

                throw new IllegalArgumentException(
                        "New curriculum cannot be created "
                                + "with DELETED status."
                );
            }

            return status;

        } catch (IllegalArgumentException ex) {

            if (ex.getMessage() != null
                    && ex.getMessage()
                    .startsWith(
                            "New curriculum")) {

                throw ex;
            }

            throw new IllegalArgumentException(
                    "Invalid curriculum status. "
                            + "Allowed values: ACTIVE, INACTIVE."
            );
        }
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private RecordStatus parseUpdateStatus(
            String value) {

        if (isBlank(value)) {

            throw new IllegalArgumentException(
                    "Status is required."
            );
        }

        try {

            RecordStatus status =
                    RecordStatus.valueOf(
                            value.trim()
                                    .toUpperCase()
                    );

            if (status == RecordStatus.DELETED) {

                throw new IllegalArgumentException(
                        "Use delete operation to delete "
                                + "a curriculum."
                );
            }

            return status;

        } catch (IllegalArgumentException ex) {

            if (ex.getMessage() != null
                    && ex.getMessage()
                    .startsWith(
                            "Use delete")) {

                throw ex;
            }

            throw new IllegalArgumentException(
                    "Invalid curriculum status. "
                            + "Allowed values: ACTIVE, INACTIVE."
            );
        }
    }

    // =====================================================
    // BLANK CHECK
    // =====================================================

    private boolean isBlank(String value) {

        return value == null
                || value.trim().isEmpty();
    }
}
