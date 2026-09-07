package com.admission.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admission.dto.AdmissionRequestDto;
import com.admission.dto.AdmissionResponseDto;
import com.admission.dto.AdmissionSearchRequest;
import com.admission.entity.Admission;
import com.admission.enums.AdmissionStatus;
import com.admission.mapper.AdmissionMapper;
import com.admission.repository.AdmissionRepository;
import com.admission.service.AdmissionService;
import com.admission.specification.AdmissionSpecification;
import com.sms.dto.PageResponse;
import com.sms.entity.Course;
import com.sms.entity.Department;
import com.sms.entity.Semester;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.CourseRepository;
import com.sms.repository.DepartmentRepository;
import com.sms.repository.SemesterRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.sequence.enums.ModuleType;
import com.sms.sequence.service.CodeGeneratorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final AdmissionMapper admissionMapper;
    private final CodeGeneratorService codeGeneratorService;
    private final SemesterRepository semesterRepository;

    // =====================================================
    // CREATE ADMISSION
    // =====================================================

    @Override
    public AdmissionResponseDto createAdmission(
            AdmissionRequestDto dto) {

        // -------------------------------------------------
        // 1. Student validation
        // -------------------------------------------------

        Student student =
                getActiveStudent(dto.getStudentId());

        // -------------------------------------------------
        // 2. Duplicate active admission validation
        // -------------------------------------------------

        validateDuplicateAdmission(
                dto.getStudentId()
        );

        // -------------------------------------------------
        // 3. Department validation
        // -------------------------------------------------

        Department department =
                getActiveDepartment(dto.getDepartmentId());

        // -------------------------------------------------
        // 4. Course validation
        // -------------------------------------------------

        Course course =
                getActiveCourse(dto.getCourseId());

        // -------------------------------------------------
        // 5. Course belongs to department
        // -------------------------------------------------

        validateCourseDepartment(
                course,
                department
        );

        // -------------------------------------------------
        // 6. Semester validation
        // -------------------------------------------------

        Semester semester =
                getActiveSemester(dto.getSemesterId());

        // -------------------------------------------------
        // 7. DTO -> Entity
        // -------------------------------------------------

        Admission admission =
                admissionMapper.toEntity(dto);

        // -------------------------------------------------
        // 8. Relationships
        // -------------------------------------------------

        admission.setStudent(student);
        admission.setDepartment(department);
        admission.setCourse(course);
        admission.setSemester(semester);

        // -------------------------------------------------
        // 9. Generated admission number
        // -------------------------------------------------

        admission.setAdmissionNumber(
                codeGeneratorService.generateCode(
                        ModuleType.ADMISSION,
                        "ADM"
                )
        );

        // -------------------------------------------------
        // 10. System fields
        // -------------------------------------------------

        admission.setAdmissionStatus(
                AdmissionStatus.ACTIVE
        );

        admission.setStatus(
                RecordStatus.ACTIVE
        );

        // -------------------------------------------------
        // 11. Admission date
        // -------------------------------------------------

        if (admission.getAdmissionDate() == null) {
            admission.setAdmissionDate(
                    LocalDate.now()
            );
        }

        // -------------------------------------------------
        // 12. Audit fields
        // -------------------------------------------------

        admission.setCreatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        admission.setCreatedBy(
                SecurityUtils.getCurrentFullName()
        );

        // -------------------------------------------------
        // 13. Save
        // -------------------------------------------------

        Admission savedAdmission =
                admissionRepository.save(admission);

        // -------------------------------------------------
        // 14. Response
        // -------------------------------------------------

        return admissionMapper.toDto(savedAdmission);
    }

    // =====================================================
    // GET ADMISSION BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public AdmissionResponseDto getAdmissionById(Long id) {

        Admission admission =
                admissionRepository
                        .findByIdAndAdmissionStatusNot(
                                id,
                                AdmissionStatus.CANCELLED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admission not found with id : "
                                                + id
                                )
                        );

        return admissionMapper.toDto(admission);
    }

    // =====================================================
    // GET ALL ADMISSIONS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AdmissionResponseDto> getAllAdmissions(
            int page,
            int size,
            String sortBy,
            String direction) {

        /*
         * Objects.requireNonNull() guarantees that the Sort
         * passed to PageRequest.of() is never null.
         *
         * No business logic changed.
         */
        Sort sort =
                Objects.requireNonNull(
                        createSort(
                                sortBy,
                                direction
                        )
                );

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        Page<Admission> admissionPage =
                admissionRepository
                        .findByAdmissionStatusNotAndStatus(
                                AdmissionStatus.CANCELLED,
                                RecordStatus.ACTIVE,
                                pageable
                        );

        List<AdmissionResponseDto> content =
                admissionPage
                        .getContent()
                        .stream()
                        .map(admissionMapper::toDto)
                        .toList();

        return createPageResponse(
                admissionPage,
                content
        );
    }

    // =====================================================
    // UPDATE ADMISSION
    // =====================================================

    @Override
    public AdmissionResponseDto updateAdmission(
            Long id,
            AdmissionRequestDto dto) {

        // -------------------------------------------------
        // 1. Existing admission fetch
        // -------------------------------------------------

        Admission admission =
                admissionRepository
                        .findByIdAndAdmissionStatusNot(
                                id,
                                AdmissionStatus.CANCELLED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admission not found with id : "
                                                + id
                                )
                        );

        // -------------------------------------------------
        // 2. Existing admission must be ACTIVE record
        // -------------------------------------------------

        if (admission.getStatus() != RecordStatus.ACTIVE) {

            throw new BusinessException(
                    "Inactive or deleted admission cannot be updated"
            );
        }

        // -------------------------------------------------
        // 3. Student validation
        // -------------------------------------------------

        Student student =
                getActiveStudent(
                        dto.getStudentId()
                );

        // -------------------------------------------------
        // 4. Duplicate validation
        //
        // Current admission ID ko exclude karke check.
        // -------------------------------------------------

        validateDuplicateAdmissionForUpdate(
                id,
                dto.getStudentId()
        );

        // -------------------------------------------------
        // 5. Department validation
        // -------------------------------------------------

        Department department =
                getActiveDepartment(
                        dto.getDepartmentId()
                );

        // -------------------------------------------------
        // 6. Course validation
        // -------------------------------------------------

        Course course =
                getActiveCourse(
                        dto.getCourseId()
                );

        // -------------------------------------------------
        // 7. Course -> Department validation
        // -------------------------------------------------

        validateCourseDepartment(
                course,
                department
        );

        // -------------------------------------------------
        // 8. Semester validation
        // -------------------------------------------------

        Semester semester =
                getActiveSemester(
                        dto.getSemesterId()
                );

        // -------------------------------------------------
        // 9. Existing admission date preserve
        // -------------------------------------------------

        LocalDate oldAdmissionDate =
                admission.getAdmissionDate();

        // -------------------------------------------------
        // 10. Update DTO fields
        // -------------------------------------------------

        admissionMapper.updateEntity(
                admission,
                dto
        );

        // -------------------------------------------------
        // 11. Preserve admission date if not provided
        // -------------------------------------------------

        if (dto.getAdmissionDate() == null) {

            admission.setAdmissionDate(
                    oldAdmissionDate
            );
        }

        // -------------------------------------------------
        // 12. Update relationships
        // -------------------------------------------------

        admission.setStudent(student);
        admission.setDepartment(department);
        admission.setCourse(course);
        admission.setSemester(semester);

        // -------------------------------------------------
        // 13. Keep system controlled fields safe
        // -------------------------------------------------

        admission.setAdmissionStatus(
                AdmissionStatus.ACTIVE
        );

        admission.setStatus(
                RecordStatus.ACTIVE
        );

        // -------------------------------------------------
        // 14. Update audit fields
        // -------------------------------------------------

        admission.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        admission.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );

        // -------------------------------------------------
        // 15. Save
        // -------------------------------------------------

        Admission updated =
                admissionRepository.save(admission);

        // -------------------------------------------------
        // 16. Response
        // -------------------------------------------------

        return admissionMapper.toDto(updated);
    }

    // =====================================================
    // DELETE / CANCEL ADMISSION
    // =====================================================

    @Override
    public void deleteAdmission(Long id) {

        Admission admission =
                admissionRepository
                        .findById(
                                java.util.Objects.requireNonNull(id)
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admission not found with id : "
                                                + id
                                )
                        );

        if (admission.getAdmissionStatus()
                == AdmissionStatus.CANCELLED) {

            throw new BusinessException(
                    "Admission already cancelled"
            );
        }

        if (admission.getAdmissionStatus()
                == AdmissionStatus.COMPLETED) {

            throw new BusinessException(
                    "Completed admission cannot be cancelled"
            );
        }

        admission.setAdmissionStatus(
                AdmissionStatus.CANCELLED
        );

        admissionRepository.save(admission);
    }

    // =====================================================
    // SEARCH ADMISSIONS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AdmissionResponseDto> searchAdmissions(
            AdmissionSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction) {

        Specification<Admission> specification =
                (root, query, criteriaBuilder) -> null;

        // -------------------------------------------------
        // Status
        // -------------------------------------------------

        if (request.getAdmissionStatus() != null) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasStatus(
                                    request.getAdmissionStatus()
                            )
                    );

        } else {

            specification =
                    specification.and(
                            AdmissionSpecification.hasStatus(
                                    AdmissionStatus.ACTIVE
                            )
                    );
        }

        // -------------------------------------------------
        // Admission Number
        // -------------------------------------------------

        if (request.getAdmissionNumber() != null
                && !request.getAdmissionNumber().isBlank()) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasAdmissionNumber(
                                    request.getAdmissionNumber()
                            )
                    );
        }

        // -------------------------------------------------
        // Student ID
        // -------------------------------------------------

        if (request.getStudentId() != null) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasStudentId(
                                    request.getStudentId()
                            )
                    );
        }

        // -------------------------------------------------
        // Student Name
        // -------------------------------------------------

        if (request.getStudentName() != null
                && !request.getStudentName().isBlank()) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasStudentName(
                                    request.getStudentName()
                            )
                    );
        }

        // -------------------------------------------------
        // Department
        // -------------------------------------------------

        if (request.getDepartmentId() != null) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasDepartmentId(
                                    request.getDepartmentId()
                            )
                    );
        }

        // -------------------------------------------------
        // Course
        // -------------------------------------------------

        if (request.getCourseId() != null) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasCourseId(
                                    request.getCourseId()
                            )
                    );
        }

        // -------------------------------------------------
        // Academic Year
        // -------------------------------------------------

        if (request.getAcademicYear() != null
                && !request.getAcademicYear().isBlank()) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasAcademicYear(
                                    request.getAcademicYear()
                            )
                    );
        }

        // -------------------------------------------------
        // Semester
        // -------------------------------------------------

        if (request.getSemesterId() != null) {

            specification =
                    specification.and(
                            AdmissionSpecification.hasSemester(
                                    request.getSemesterId()
                            )
                    );
        }

        // -------------------------------------------------
        // Record status
        // -------------------------------------------------

        specification =
                specification.and(
                        AdmissionSpecification.hasRecordStatus(
                                RecordStatus.ACTIVE
                        )
                );

        // -------------------------------------------------
        // Pagination
        // -------------------------------------------------

        /*
         * Null-safe Sort.
         * Logic remains exactly the same.
         */
        Sort sort =
                Objects.requireNonNull(
                        createSort(
                                sortBy,
                                direction
                        )
                );

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        // -------------------------------------------------
        // Query
        // -------------------------------------------------

        Page<Admission> admissionPage =
                admissionRepository.findAll(
                        specification,
                        pageable
                );

        // -------------------------------------------------
        // DTO
        // -------------------------------------------------

        List<AdmissionResponseDto> content =
                admissionPage
                        .getContent()
                        .stream()
                        .map(admissionMapper::toDto)
                        .toList();

        return createPageResponse(
                admissionPage,
                content
        );
    }

    // =====================================================
    // SORT VALIDATION
    // =====================================================

    private Sort createSort(
            String sortBy,
            String direction) {

        List<String> allowedFields =
                List.of(
                        "id",
                        "admissionNumber",
                        "admissionDate",
                        "academicYear"
                );

        if (sortBy == null
                || !allowedFields.contains(sortBy)) {

            sortBy = "id";
        }

        if (direction == null
                || !direction.equalsIgnoreCase("asc")) {

            return Sort.by(sortBy)
                    .descending();
        }

        return Sort.by(sortBy)
                .ascending();
    }

    // =====================================================
    // GET ACTIVE STUDENT
    // =====================================================

    private Student getActiveStudent(Long studentId) {

        return studentRepository
                .findByIdAndStatus(
                        studentId,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : "
                                        + studentId
                        )
                );
    }

    // =====================================================
    // GET ACTIVE DEPARTMENT
    // =====================================================

    private Department getActiveDepartment(
            Long departmentId) {

        return departmentRepository
                .findByIdAndStatus(
                        departmentId,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id : "
                                        + departmentId
                        )
                );
    }

    // =====================================================
    // GET ACTIVE COURSE
    // =====================================================

    private Course getActiveCourse(
            Long courseId) {

        return courseRepository
                .findByIdAndStatus(
                        courseId,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : "
                                        + courseId
                        )
                );
    }

    // =====================================================
    // GET ACTIVE SEMESTER
    // =====================================================

    private Semester getActiveSemester(
            Long semesterId) {

        return semesterRepository
                .findByIdAndStatusNot(
                        semesterId,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Semester not found with id : "
                                        + semesterId
                        )
                );
    }

    // =====================================================
    // COURSE -> DEPARTMENT VALIDATION
    // =====================================================

    private void validateCourseDepartment(
            Course course,
            Department department) {

        if (!course.getDepartment()
                .getId()
                .equals(department.getId())) {

            throw new BusinessException(
                    "Course does not belong to department"
            );
        }
    }

    // =====================================================
    // CREATE DUPLICATE VALIDATION
    // =====================================================

    private void validateDuplicateAdmission(
            Long studentId) {

        boolean exists =
                admissionRepository
                        .existsByStudent_IdAndAdmissionStatusAndStatus(
                                studentId,
                                AdmissionStatus.ACTIVE,
                                RecordStatus.ACTIVE
                        );

        if (exists) {

            throw new BusinessException(
                    "Student already has active admission"
            );
        }
    }

    // =====================================================
    // UPDATE DUPLICATE VALIDATION
    // =====================================================

    private void validateDuplicateAdmissionForUpdate(
            Long admissionId,
            Long studentId) {

        /*
         * Current admission ko exclude karke check.
         *
         * Example:
         *
         * Admission 1 -> Student 10
         *
         * Update Admission 1 -> Student 10
         *
         * Allowed.
         *
         * But:
         *
         * Admission 1 -> Student 10
         * Admission 2 -> Student 10
         *
         * Update Admission 1 -> Student 10
         *
         * Existing duplicate Admission 2 detect hoga.
         */

        boolean duplicateExists =
                admissionRepository
                        .existsByStudent_IdAndAdmissionStatusAndStatusAndIdNot(
                                studentId,
                                AdmissionStatus.ACTIVE,
                                RecordStatus.ACTIVE,
                                admissionId
                        );

        if (duplicateExists) {

            throw new BusinessException(
                    "Student already has another active admission"
            );
        }
    }

    // =====================================================
    // PAGE RESPONSE
    // =====================================================

    private <E, D> PageResponse<D> createPageResponse(
            Page<E> page,
            List<D> content) {

        PageResponse<D> response =
                new PageResponse<>();

        response.setContent(content);

        response.setPageNumber(
                page.getNumber()
        );

        response.setPageSize(
                page.getSize()
        );

        response.setTotalElements(
                page.getTotalElements()
        );

        response.setTotalPages(
                page.getTotalPages()
        );

        response.setLast(
                page.isLast()
        );

        return response;
    }
}
