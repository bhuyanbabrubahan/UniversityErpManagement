package com.sms.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.CourseRequestDto;
import com.sms.dto.CourseResponseDto;
import com.sms.dto.CourseSearchRequest;
import com.sms.dto.PageResponse;
import com.sms.entity.Course;
import com.sms.entity.Department;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.CourseMapper;
import com.sms.repository.CourseRepository;
import com.sms.repository.DepartmentRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.CourseService;
import com.sms.specification.CourseSpecification;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final DepartmentRepository departmentRepository;

    public CourseServiceImpl(
            CourseRepository repository,
            CourseMapper mapper,
            DepartmentRepository departmentRepository) {

        this.repository = repository;
        this.mapper = mapper;
        this.departmentRepository = departmentRepository;
    }

    
    
 // ---------------- CREATE ----------------

    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.CREATE,
            module = "COURSE",
            description = "Create new course"
    )
    public CourseResponseDto createCourse(
            CourseRequestDto dto
    ) {


        // ==========================================================
        // 1. Check Duplicate Course Name
        // ==========================================================

        if (repository.existsByCourseName(dto.getCourseName())) {

            throw new BusinessException(
                    "Course name already exists."
            );
        }



        // ==========================================================
        // 2. Check Duplicate Course Code
        // ==========================================================

        if (repository.existsByCourseCode(dto.getCourseCode())) {

            throw new BusinessException(
                    "Course code already exists."
            );
        }



        // ==========================================================
        // 3. Fetch ACTIVE Department
        // ==========================================================

        Department department = departmentRepository
                .findByIdAndStatus(
                        dto.getDepartmentId(),
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: "
                                + dto.getDepartmentId()
                        )
                );



        // ==========================================================
        // 4. Convert DTO -> Entity
        // ==========================================================

        Course course = mapper.toEntity(dto);



        // ==========================================================
        // 5. Set Department Relationship
        // ==========================================================

        course.setDepartment(department);

        // 3. Default status set (VERY IMPORTANT in production)
        course.setStatus(RecordStatus.ACTIVE);

        // ==========================================================
        // 6. Set Creation Information
        // ==========================================================

        course.setCreatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        course.setCreatedBy(
                SecurityUtils.getCurrentFullName()
        );

        course.setCreatedAt(
                LocalDateTime.now()
        );



        // ==========================================================
        // 7. Save Entity
        // ==========================================================

        Course saved = repository.save(course);



        // ==========================================================
        // 8. Return Response DTO
        // ==========================================================

        return mapper.toDto(saved);
    }
    

    // ---------------- GET BY ID (FIXED) ----------------
    @Override
    public CourseResponseDto getCourseById(Long id) {

        Course course = repository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id: " + id));

        return mapper.toDto(course);
    }

    
    // ---------------- UPDATE ----------------
    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "COURSE",
            description = "Update course details"
    )
    public CourseResponseDto updateCourse(
            Long id,
            CourseRequestDto dto
    ) {

        // ==========================================================
        // 1. Fetch Existing ACTIVE Course
        // ==========================================================

        Course course = repository
                .findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: " + id
                        )
                );


        // ==========================================================
        // 2. Check Duplicate Course Name
        // ==========================================================

        if (repository.existsByCourseNameAndIdNot(
                dto.getCourseName(),
                id
        )) {

            throw new BusinessException(
                    "Course name already exists."
            );
        }



        // ==========================================================
        // 3. Check Duplicate Course Code
        // ==========================================================

        if (repository.existsByCourseCodeAndIdNot(
                dto.getCourseCode(),
                id
        )) {

            throw new BusinessException(
                    "Course code already exists."
            );
        }



        // ==========================================================
        // 4. Update Department (if changed)
        // ==========================================================

        if (dto.getDepartmentId() != null &&
            (
                course.getDepartment() == null ||
                !dto.getDepartmentId()
                 .equals(course.getDepartment().getId())
            )) {


            Department department = departmentRepository
                    .findByIdAndStatus(
                            dto.getDepartmentId(),
                            RecordStatus.ACTIVE
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Department not found with id: "
                                    + dto.getDepartmentId()
                            )
                    );


            course.setDepartment(department);
        }



        // ==========================================================
        // 5. Update Simple Fields
        // ==========================================================

        mapper.updateEntity(
                course,
                dto
        );



        // ==========================================================
        // 6. Update Audit Information
        // ==========================================================

        course.setUpdatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        course.setUpdatedBy(
                SecurityUtils.getCurrentFullName()
        );

        course.setUpdatedAt(
                LocalDateTime.now()
        );



        // ==========================================================
        // 7. Save Updated Course
        // ==========================================================

        Course updated = repository.save(course);



        // ==========================================================
        // 8. Return Response
        // ==========================================================

        return mapper.toDto(updated);
    }
    

    // ---------------- DELETE (SOFT DELETE) ----------------
    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.DELETE,
            module = "COURSE",
            description = "Delete course"
    )
    public void deleteCourse(Long id) {

        Course course = repository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id: " + id));

        course.setStatus(RecordStatus.DELETED);

        repository.save(course);
    }

    // ---------------- GET ALL (FIXED FILTER) ----------------
    @Override
    public PageResponse<CourseResponseDto> getAllCourses(int page,
                                                          int size,
                                                          String sortBy,
                                                          String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // 🔥 FIX: exclude deleted
        Page<Course> coursePage = repository.findByStatusNot(RecordStatus.DELETED, pageable);

        List<CourseResponseDto> content = coursePage.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        PageResponse<CourseResponseDto> response = new PageResponse<>();

        response.setContent(content);
        response.setPageNumber(coursePage.getNumber());
        response.setPageSize(coursePage.getSize());
        response.setTotalElements(coursePage.getTotalElements());
        response.setTotalPages(coursePage.getTotalPages());
        response.setLast(coursePage.isLast());

        return response;
    }

    // ---------------- SEARCH (PRODUCTION OPTIMIZED) ----------------
    @Override
    public PageResponse<CourseResponseDto> searchCourses(
            CourseSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction) {

        Specification<Course> spec = Specification.where(null);

        // Status
        if (request.getStatus() != null) {
            spec = spec.and(CourseSpecification.hasStatus(request.getStatus()));
        } else {
            spec = spec.and(CourseSpecification.hasStatus(RecordStatus.ACTIVE));
        }

        // Course Name
        if (request.getCourseName() != null && !request.getCourseName().isBlank()) {
            spec = spec.and(
                    CourseSpecification.hasCourseName(request.getCourseName()));
        }

        // Course Code
        if (request.getCourseCode() != null && !request.getCourseCode().isBlank()) {
            spec = spec.and(
                    CourseSpecification.hasCourseCode(request.getCourseCode()));
        }

        // Department
        if (request.getDepartmentId() != null) {
            spec = spec.and(
                    CourseSpecification.hasDepartment(request.getDepartmentId()));
        }


        // Minimum Duration
        if (request.getMinDuration() != null) {
            spec = spec.and(
                    CourseSpecification.hasMinDuration(request.getMinDuration()));
        }

        // Maximum Duration
        if (request.getMaxDuration() != null) {
            spec = spec.and(
                    CourseSpecification.hasMaxDuration(request.getMaxDuration()));
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Course> coursePage = repository.findAll(spec, pageable);

        List<CourseResponseDto> content = coursePage.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        PageResponse<CourseResponseDto> response = new PageResponse<>();
        response.setContent(content);
        response.setPageNumber(coursePage.getNumber());
        response.setPageSize(coursePage.getSize());
        response.setTotalElements(coursePage.getTotalElements());
        response.setTotalPages(coursePage.getTotalPages());
        response.setLast(coursePage.isLast());

        return response;
    }
}