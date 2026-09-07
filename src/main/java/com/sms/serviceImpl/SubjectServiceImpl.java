package com.sms.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.dto.SubjectRequestDto;
import com.sms.dto.SubjectResponseDto;
import com.sms.dto.SubjectSearchRequest;
import com.sms.entity.Semester;
import com.sms.entity.Subject;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.SubjectMapper;
import com.sms.repository.SemesterRepository;
import com.sms.repository.SubjectRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.SubjectService;
import com.sms.specification.SubjectSpecification;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository repository;
    private final SubjectMapper mapper;
    private final SemesterRepository semesterRepository;

    public SubjectServiceImpl(
            SubjectRepository repository,
            SubjectMapper mapper,
            SemesterRepository semesterRepository) {

        this.repository = repository;
        this.mapper = mapper;
        this.semesterRepository = semesterRepository;
    }

    // ==========================
    // CREATE
    // ==========================

    @Override
    @AuditLog(
            action = AuditAction.CREATE,
            module = "SUBJECT",
            description = "Create Subject"
    )
    public SubjectResponseDto createSubject(SubjectRequestDto dto) {

        if (repository.existsBySubjectNameAndSemesterId(
                dto.getSubjectName(),
                dto.getSemesterId())) {

            throw new BusinessException(
                    "Subject already exists in this semester.");
        }

        Semester semester = semesterRepository
                .findByIdAndStatusNot(
                        dto.getSemesterId(),
                        RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Semester not found with id : "
                                        + dto.getSemesterId()));

        Subject subject = mapper.toEntity(dto);

        subject.setSubjectCode(generateSubjectCode());
        subject.setSemester(semester);
        subject.setStatus(RecordStatus.ACTIVE);
        
	    // Create info
        subject.setCreatedByUserId(
	 		        SecurityUtils.getCurrentUserId()
	 		);

        subject.setCreatedBy(
	 		        SecurityUtils.getCurrentFullName()
	 		);

        subject.setCreatedAt(
	 		        LocalDateTime.now()
	 		);

        Subject saved = repository.save(subject);

        return mapper.toDto(saved);
    }

    // ==========================
    // GET BY ID
    // ==========================

    @Override
    public SubjectResponseDto getSubjectById(Long id) {

        Subject subject = repository
                .findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id : " + id));

        return mapper.toDto(subject);
    }

    // ==========================
    // UPDATE
    // ==========================

    @Override
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "SUBJECT",
            description = "Update Subject"
    )
    public SubjectResponseDto updateSubject(
            Long id,
            SubjectRequestDto dto) {

        Subject subject = repository
                .findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id : " + id));

        if (repository.existsBySubjectNameAndSemesterIdAndIdNot(
                dto.getSubjectName(),
                dto.getSemesterId(),
                id)) {

            throw new BusinessException(
                    "Subject already exists in this semester.");
        }

        Semester semester = semesterRepository
                .findByIdAndStatusNot(
                        dto.getSemesterId(),
                        RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Semester not found with id : "
                                        + dto.getSemesterId()));

        mapper.updateEntity(subject, dto);

        subject.setSemester(semester);
        
	    // Update info
        subject.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        subject.setUpdatedBy(SecurityUtils.getCurrentFullName());
        subject.setUpdatedAt(LocalDateTime.now());

        Subject updated = repository.save(subject); // repository.save  जरूरत ही नहीं।

        return mapper.toDto(updated);
    }

    // ==========================
    // DELETE
    // ==========================

    @Override
    @AuditLog(
            action = AuditAction.DELETE,
            module = "SUBJECT",
            description = "Delete Subject"
    )
    public void deleteSubject(Long id) {

        Subject subject = repository
                .findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subject not found with id : " + id));

        subject.setStatus(RecordStatus.DELETED);

        repository.save(subject);
    }

    // ==========================
    // GET ALL
    // ==========================

    @Override
    public PageResponse<SubjectResponseDto> getAllSubjects(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Page<Subject> subjectPage =
                repository.findByStatusNot(
                		RecordStatus.DELETED,
                        pageable);

        List<SubjectResponseDto> content =
                subjectPage.getContent()
                        .stream()
                        .map(mapper::toDto)
                        .toList();

        PageResponse<SubjectResponseDto> response =
                new PageResponse<>();

        response.setContent(content);
        response.setPageNumber(subjectPage.getNumber());
        response.setPageSize(subjectPage.getSize());
        response.setTotalElements(subjectPage.getTotalElements());
        response.setTotalPages(subjectPage.getTotalPages());
        response.setLast(subjectPage.isLast());

        return response;
    }

    // ==========================
    // SEARCH
    // ==========================

    @Override
    public PageResponse<SubjectResponseDto> searchSubjects(
            SubjectSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction) {

        Specification<Subject> spec =
                Specification.where(null);

        // =====================================================
        // STATUS
        // =====================================================

        if (request.getStatus() != null) {

            spec = spec.and(
                    SubjectSpecification.hasStatus(
                            request.getStatus()));

        } else {

            spec = spec.and(
                    SubjectSpecification.hasStatus(
                            RecordStatus.ACTIVE));
        }


        // =====================================================
        // SUBJECT NAME
        // =====================================================

        if (request.getSubjectName() != null
                && !request.getSubjectName().isBlank()) {

            spec = spec.and(
                    SubjectSpecification.hasSubjectName(
                            request.getSubjectName()));
        }


        // =====================================================
        // SUBJECT CODE
        // =====================================================

        if (request.getSubjectCode() != null
                && !request.getSubjectCode().isBlank()) {

            spec = spec.and(
                    SubjectSpecification.hasSubjectCode(
                            request.getSubjectCode()));
        }


        // =====================================================
        // SEMESTER NUMBER
        // =====================================================

        if (request.getSemesterNumber() != null) {

            spec = spec.and(
                    SubjectSpecification.hasSemesterNumber(
                            request.getSemesterNumber()));
        }


        // =====================================================
        // CREDITS
        // =====================================================

        if (request.getCredits() != null) {

            spec = spec.and(
                    SubjectSpecification.hasCredits(
                            request.getCredits()));
        }


        // =====================================================
        // SORTING
        // =====================================================

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        // =====================================================
        // PAGINATION
        // =====================================================

        Pageable pageable =
                PageRequest.of(page, size, sort);


        // =====================================================
        // DATABASE SEARCH
        // =====================================================

        Page<Subject> subjectPage =
                repository.findAll(spec, pageable);


        // =====================================================
        // MAP RESPONSE
        // =====================================================

        List<SubjectResponseDto> content =
                subjectPage.getContent()
                        .stream()
                        .map(mapper::toDto)
                        .toList();


        // =====================================================
        // PAGE RESPONSE
        // =====================================================

        PageResponse<SubjectResponseDto> response =
                new PageResponse<>();

        response.setContent(content);
        response.setPageNumber(
                subjectPage.getNumber());

        response.setPageSize(
                subjectPage.getSize());

        response.setTotalElements(
                subjectPage.getTotalElements());

        response.setTotalPages(
                subjectPage.getTotalPages());

        response.setLast(
                subjectPage.isLast());

        return response;
    }

    // ==========================
    // SUBJECT CODE GENERATOR
    // ==========================

    private String generateSubjectCode() {

        Optional<Subject> optionalSubject =
                repository.findTopByOrderByIdDesc();

        if (optionalSubject.isEmpty()) {
            return "SUB001";
        }

        String lastCode =
                optionalSubject.get().getSubjectCode();

        int number =
                Integer.parseInt(lastCode.substring(3));

        return String.format("SUB%03d", number + 1);
    }

}