package com.sms.erp.timetable.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.erp.timetable.dto.TimetableRequestDto;
import com.sms.erp.timetable.dto.TimetableResponseDto;
import com.sms.erp.timetable.dto.TimetableSearchDto;
import com.sms.erp.timetable.entity.Timetable;
import com.sms.erp.timetable.mapper.TimetableMapper;
import com.sms.erp.timetable.repository.TimetableRepository;
import com.sms.erp.timetable.service.TimetableService;
import com.sms.erp.timetable.specification.TimetableSpecification;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableServiceImpl
        implements TimetableService {

    private final TimetableRepository repository;

    private final TimetableMapper mapper;

    // =====================================================
    // FIND ALL
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<TimetableResponseDto> findAll(
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
                        TimetableSpecification.bySearch(null),
                        pageable
                )
                .map(mapper::toResponse);
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Page<TimetableResponseDto> search(
            TimetableSearchDto search,
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
                        TimetableSpecification.bySearch(
                                search
                        ),
                        pageable
                )
                .map(mapper::toResponse);
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public TimetableResponseDto findById(
            Long id) {

        validateId(id);

        Timetable timetable =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Timetable not found: "
                                                + id
                                )
                        );

        return mapper.toResponse(
                timetable
        );
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Override
    public TimetableResponseDto create(
            TimetableRequestDto request) {

        validateRequest(request);

        String code =
                normalize(
                        request.getCode()
                );

        // =================================================
        // DUPLICATE CODE
        // =================================================

        if (repository.existsByCodeAndStatusNot(
                code,
                RecordStatus.DELETED
        )) {

            throw new IllegalArgumentException(
                    "Timetable code already exists: "
                            + code
            );
        }

        // =================================================
        // ENTITY
        // =================================================

        Timetable timetable =
                mapper.toEntity(request);

        timetable.setCode(code);

        // =================================================
        // NORMALIZE DATA
        // =================================================

        normalizeEntity(timetable);

        // =================================================
        // STATUS
        // =================================================

        timetable.setStatus(
                parseCreateStatus(
                        request.getStatus()
                )
        );

        // =================================================
        // AUDIT
        // =================================================

        Long currentUserId =
                SecurityUtils.getCurrentUserId();

        String currentUser =
                SecurityUtils.getCurrentFullName();

        LocalDateTime now =
                LocalDateTime.now();

        timetable.setCreatedByUserId(
                currentUserId
        );

        timetable.setCreatedBy(
                currentUser
        );

        timetable.setCreatedAt(now);

        timetable.setUpdatedByUserId(
                currentUserId
        );

        timetable.setUpdatedBy(
                currentUser
        );

        timetable.setUpdatedAt(now);

        // =================================================
        // SAVE
        // =================================================

        Timetable saved =
                repository.save(timetable);

        return mapper.toResponse(saved);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Override
    public TimetableResponseDto update(
            Long id,
            TimetableRequestDto request) {

        validateId(id);

        validateRequest(request);

        Timetable current =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Timetable not found: "
                                                + id
                                )
                        );

        String requestedCode =
                normalize(
                        request.getCode()
                );

        // =================================================
        // DUPLICATE CODE
        // =================================================

        if (!current.getCode()
                .equalsIgnoreCase(requestedCode)
                && repository.existsByCodeAndStatusNot(
                        requestedCode,
                        RecordStatus.DELETED
                )) {

            throw new IllegalArgumentException(
                    "Timetable code already exists: "
                            + requestedCode
            );
        }

        // =================================================
        // PRESERVE CREATE AUDIT
        // =================================================

        Long createdByUserId =
                current.getCreatedByUserId();

        String createdBy =
                current.getCreatedBy();

        LocalDateTime createdAt =
                current.getCreatedAt();

        // =================================================
        // UPDATE DATA
        // =================================================

        mapper.updateEntity(
                request,
                current
        );

        current.setCode(
                requestedCode
        );

        normalizeEntity(current);

        // =================================================
        // RESTORE CREATE AUDIT
        // =================================================

        current.setCreatedByUserId(
                createdByUserId
        );

        current.setCreatedBy(
                createdBy
        );

        current.setCreatedAt(
                createdAt
        );

        // =================================================
        // STATUS
        // =================================================

        current.setStatus(
                parseUpdateStatus(
                        request.getStatus()
                )
        );

        // =================================================
        // UPDATE AUDIT
        // =================================================

        current.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        current.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );

        current.setUpdatedAt(
                LocalDateTime.now()
        );

        // =================================================
        // SAVE
        // =================================================

        Timetable updated =
                repository.save(current);

        return mapper.toResponse(updated);
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @Override
    public void delete(Long id) {

        validateId(id);

        Timetable current =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Timetable not found: "
                                                + id
                                )
                        );

        // =================================================
        // SOFT DELETE
        // =================================================

        current.setStatus(
                RecordStatus.DELETED
        );

        // =================================================
        // DELETE AUDIT
        // =================================================

        current.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        current.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );

        current.setUpdatedAt(
                LocalDateTime.now()
        );

        // =================================================
        // SAVE
        // =================================================

        repository.save(current);
    }

    // =====================================================
    // VALIDATE ID
    // =====================================================

    private void validateId(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid timetable ID: " + id
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
            TimetableRequestDto request) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Timetable request is required."
            );
        }

        validateRequired(
                request.getCode(),
                "Timetable code is required."
        );

        validateRequired(
                request.getAcademicYear(),
                "Academic year is required."
        );

        validateRequired(
                request.getSemester(),
                "Semester is required."
        );

        validateRequired(
                request.getCourse(),
                "Course is required."
        );

        validateRequired(
                request.getSection(),
                "Section is required."
        );

        validateRequired(
                request.getSubject(),
                "Subject is required."
        );

        validateRequired(
                request.getFaculty(),
                "Faculty is required."
        );

        validateRequired(
                request.getRoom(),
                "Room is required."
        );

        validateRequired(
                request.getDayOfWeek(),
                "Day of week is required."
        );

        validateRequired(
                request.getStartTime(),
                "Start time is required."
        );

        validateRequired(
                request.getEndTime(),
                "End time is required."
        );

        validateRequired(
                request.getPeriod(),
                "Period is required."
        );

        validateRequired(
                request.getStatus(),
                "Status is required."
        );

        validateRequired(
                request.getDescription(),
                "Description is required."
        );

        validateDayOfWeek(
                request.getDayOfWeek()
        );

        validateTime(
                request.getStartTime(),
                "Start time"
        );

        validateTime(
                request.getEndTime(),
                "End time"
        );

        validatePeriod(
                request.getPeriod()
        );
    }

    // =====================================================
    // REQUIRED FIELD
    // =====================================================

    private void validateRequired(
            String value,
            String message) {

        if (isBlank(value)) {

            throw new IllegalArgumentException(
                    message
            );
        }
    }

    // =====================================================
    // DAY VALIDATION
    // =====================================================

    private void validateDayOfWeek(
            String value) {

        String day =
                normalizeUpper(value);

        switch (day) {

            case "MONDAY":
            case "TUESDAY":
            case "WEDNESDAY":
            case "THURSDAY":
            case "FRIDAY":
            case "SATURDAY":
            case "SUNDAY":
                return;

            default:
                throw new IllegalArgumentException(
                        "Invalid day of week. "
                                + "Allowed values: "
                                + "MONDAY, TUESDAY, WEDNESDAY, "
                                + "THURSDAY, FRIDAY, SATURDAY, SUNDAY."
                );
        }
    }

    // =====================================================
    // TIME VALIDATION
    // =====================================================

    private void validateTime(
            String value,
            String fieldName) {

        String time =
                value.trim();

        try {

            java.time.LocalTime.parse(
                    time,
                    java.time.format.DateTimeFormatter.ofPattern(
                            "HH:mm"
                    )
            );

        } catch (Exception ex) {

            throw new IllegalArgumentException(
                    fieldName
                            + " must be in HH:mm format."
            );
        }
    }

    // =====================================================
    // PERIOD VALIDATION
    // =====================================================

    private void validatePeriod(
            String value) {

        try {

            int period =
                    Integer.parseInt(
                            value.trim()
                    );

            if (period <= 0) {

                throw new IllegalArgumentException(
                        "Period must be greater than zero."
                );
            }

        } catch (NumberFormatException ex) {

            throw new IllegalArgumentException(
                    "Period must be a valid positive number."
            );
        }
    }

    // =====================================================
    // CREATE STATUS
    // =====================================================

    private RecordStatus parseCreateStatus(
            String value) {

        try {

            RecordStatus status =
                    RecordStatus.valueOf(
                            value.trim()
                                    .toUpperCase()
                    );

            if (status == RecordStatus.DELETED) {

                throw new IllegalArgumentException(
                        "New timetable cannot be created "
                                + "with DELETED status."
                );
            }

            return status;

        } catch (IllegalArgumentException ex) {

            if (ex.getMessage() != null
                    && ex.getMessage()
                    .startsWith(
                            "New timetable"
                    )) {

                throw ex;
            }

            throw new IllegalArgumentException(
                    "Invalid timetable status. "
                            + "Allowed values: ACTIVE, INACTIVE."
            );
        }
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private RecordStatus parseUpdateStatus(
            String value) {

        try {

            RecordStatus status =
                    RecordStatus.valueOf(
                            value.trim()
                                    .toUpperCase()
                    );

            if (status == RecordStatus.DELETED) {

                throw new IllegalArgumentException(
                        "Use delete operation to delete "
                                + "a timetable."
                );
            }

            return status;

        } catch (IllegalArgumentException ex) {

            if (ex.getMessage() != null
                    && ex.getMessage()
                    .startsWith(
                            "Use delete"
                    )) {

                throw ex;
            }

            throw new IllegalArgumentException(
                    "Invalid timetable status. "
                            + "Allowed values: ACTIVE, INACTIVE."
            );
        }
    }

    // =====================================================
    // NORMALIZE ENTITY
    // =====================================================

    private void normalizeEntity(
            Timetable timetable) {

        timetable.setAcademicYear(
                normalize(
                        timetable.getAcademicYear()
                )
        );

        timetable.setSemester(
                normalize(
                        timetable.getSemester()
                )
        );

        timetable.setCourse(
                normalize(
                        timetable.getCourse()
                )
        );

        timetable.setSection(
                normalize(
                        timetable.getSection()
                )
        );

        timetable.setSubject(
                normalize(
                        timetable.getSubject()
                )
        );

        timetable.setFaculty(
                normalize(
                        timetable.getFaculty()
                )
        );

        timetable.setRoom(
                normalize(
                        timetable.getRoom()
                )
        );

        timetable.setDayOfWeek(
                normalizeUpper(
                        timetable.getDayOfWeek()
                )
        );

        timetable.setStartTime(
                normalize(
                        timetable.getStartTime()
                )
        );

        timetable.setEndTime(
                normalize(
                        timetable.getEndTime()
                )
        );

        timetable.setPeriod(
                normalize(
                        timetable.getPeriod()
                )
        );

        timetable.setDescription(
                normalize(
                        timetable.getDescription()
                )
        );
    }

    // =====================================================
    // NORMALIZE
    // =====================================================

    private String normalize(
            String value) {

        return value == null
                ? null
                : value.trim();
    }

    private String normalizeUpper(
            String value) {

        return value == null
                ? null
                : value.trim()
                        .toUpperCase();
    }

    // =====================================================
    // BLANK
    // =====================================================

    private boolean isBlank(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }
}
