package com.sms.fee.serviceImpl;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.entity.Course;
import com.sms.entity.Semester;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.fee.dto.FeeStructureRequestDto;
import com.sms.fee.dto.FeeStructureResponseDto;
import com.sms.fee.dto.FeeStructureSearchRequest;
import com.sms.fee.entity.FeeStructure;
import com.sms.fee.mapper.FeeStructureMapper;
import com.sms.fee.repository.FeeStructureRepository;
import com.sms.fee.service.FeeStructureService;
import com.sms.fee.specification.FeeStructureSpecification;
import com.sms.repository.CourseRepository;
import com.sms.repository.SemesterRepository;

import lombok.RequiredArgsConstructor;



@Service
@Transactional
@RequiredArgsConstructor
public class FeeStructureServiceImpl implements FeeStructureService {

    // ==========================================================
    // Repositories
    // ==========================================================

    private final FeeStructureRepository repository;

    private final CourseRepository courseRepository;

    private final SemesterRepository semesterRepository;

    // ==========================================================
    // Mapper
    // ==========================================================

    private final FeeStructureMapper mapper;

    @Override
    public FeeStructureResponseDto createFeeStructure(
            FeeStructureRequestDto dto) {

        // ==========================================================
        // 1. Duplicate Validation
        // ==========================================================

        boolean exists =
                repository
                .existsByCourseIdAndSemesterIdAndFeeTypeAndAcademicYearAndStatusNot(
                        dto.getCourseId(),
                        dto.getSemesterId(),
                        dto.getFeeType(),
                        dto.getAcademicYear(),
                        RecordStatus.DELETED
                );

        if (exists) {

            throw new BusinessException(
                    "Fee structure already exists for this course, semester and fee type."
            );
        }

        // ==========================================================
        // 2. Course Validation
        // ==========================================================

        Course course =
                courseRepository
                .findByIdAndStatusNot(
                        dto.getCourseId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found"
                        )
                );

        // ==========================================================
        // 3. Semester Validation
        // ==========================================================

        Semester semester =
                semesterRepository
                .findByIdAndStatusNot(
                        dto.getSemesterId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Semester not found"
                        )
                );

        // ==========================================================
        // 4. Business Validation
        // ==========================================================

        validateFeeStructure(
                dto,
                course,
                semester
        );

        // ==========================================================
        // 5. DTO -> Entity
        // ==========================================================

        FeeStructure entity =
                mapper.toEntity(dto);

        entity.setCourse(course);

        entity.setSemester(semester);

        entity.setStatus(
                RecordStatus.ACTIVE
        );

        // ==========================================================
        // 6. Save
        // ==========================================================

        FeeStructure saved =
                repository.save(entity);

        // ==========================================================
        // 7. Response
        // ==========================================================

        return mapper.toDto(saved);

    }
    
    private void validateFeeStructure(

            FeeStructureRequestDto dto,

            Course course,

            Semester semester

    ) {

        // ==========================================================
        // Course - Semester Validation
        // ==========================================================

        if (!semester.getCourse().getId().equals(course.getId())) {

            throw new BusinessException(
                    "Selected semester does not belong to the selected course."
            );

        }

        // ==========================================================
        // Amount Validation
        // ==========================================================

        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new BusinessException(
                    "Fee amount must be greater than zero."
            );

        }

        // ==========================================================
        // Academic Year Validation
        // ==========================================================

        if (!dto.getAcademicYear().matches("\\d{4}-\\d{4}")) {

            throw new BusinessException(
                    "Academic year must be in YYYY-YYYY format."
            );

        }

    }

    @Override
    @Transactional(readOnly = true)
    public FeeStructureResponseDto getFeeStructureById(
            Long id) {

        // ==========================================================
        // 1. Find Fee Structure
        // ==========================================================

        FeeStructure feeStructure =
                repository
                        .findByIdAndStatusNot(
                                id,
                                RecordStatus.DELETED
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Fee structure not found with id : "
                                                + id
                                )
                        );

        // ==========================================================
        // 2. Entity -> DTO
        // ==========================================================

        return mapper.toDto(
                feeStructure
        );

    }

	@Override
	public FeeStructureResponseDto updateFeeStructure(
	        Long id,
	        FeeStructureRequestDto dto) {

	    // ==========================================================
	    // 1. Find Existing Fee Structure
	    // ==========================================================

	    FeeStructure feeStructure =
	            repository
	                    .findByIdAndStatusNot(
	                            id,
	                            RecordStatus.DELETED
	                    )
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Fee structure not found with id : " + id
	                            )
	                    );

	    // ==========================================================
	    // 2. Duplicate Validation
	    // ==========================================================

	    boolean exists =
	            repository
	                    .existsByCourseIdAndSemesterIdAndFeeTypeAndAcademicYearAndStatusNotAndIdNot(
	                            dto.getCourseId(),
	                            dto.getSemesterId(),
	                            dto.getFeeType(),
	                            dto.getAcademicYear(),
	                            RecordStatus.DELETED,
	                            id
	                    );

	    if (exists) {

	        throw new BusinessException(
	                "Fee structure already exists for this course, semester and fee type."
	        );

	    }

	    // ==========================================================
	    // 3. Course Validation
	    // ==========================================================

	    Course course =
	            courseRepository
	                    .findByIdAndStatusNot(
	                            dto.getCourseId(),
	                            RecordStatus.DELETED
	                    )
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Course not found with id : "
	                                            + dto.getCourseId()
	                            )
	                    );

	    // ==========================================================
	    // 4. Semester Validation
	    // ==========================================================

	    Semester semester =
	            semesterRepository
	                    .findByIdAndStatusNot(
	                            dto.getSemesterId(),
	                            RecordStatus.DELETED
	                    )
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Semester not found with id : "
	                                            + dto.getSemesterId()
	                            )
	                    );

	    // ==========================================================
	    // 5. Business Validation
	    // ==========================================================

	    validateFeeStructure(
	            dto,
	            course,
	            semester
	    );

	    // ==========================================================
	    // 6. Update Entity
	    // ==========================================================

	    mapper.updateEntity(
	            feeStructure,
	            dto
	    );

	    feeStructure.setCourse(course);

	    feeStructure.setSemester(semester);

	    // ==========================================================
	    // 7. Save
	    // ==========================================================

	    FeeStructure updated =
	            repository.save(feeStructure);

	    // ==========================================================
	    // 8. Response
	    // ==========================================================

	    return mapper.toDto(updated);

	}

	@Override
	public void deleteFeeStructure(Long id) {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<FeeStructureResponseDto> getAllFeeStructures(
	        int page,
	        int size,
	        String sortBy,
	        String direction) {

	    // ==========================================================
	    // 1. Sorting
	    // ==========================================================

	    Sort sort = direction.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    // ==========================================================
	    // 2. Pageable
	    // ==========================================================

	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            sort
	    );

	    // ==========================================================
	    // 3. Fetch Data
	    // ==========================================================

	    Page<FeeStructure> result =
	            repository.findByStatusNot(
	                    RecordStatus.DELETED,
	                    pageable
	            );

	    // ==========================================================
	    // 4. Entity -> DTO
	    // ==========================================================

	    List<FeeStructureResponseDto> content =
	            result.getContent()
	                    .stream()
	                    .map(mapper::toDto)
	                    .toList();

	    // ==========================================================
	    // 5. Prepare Response
	    // ==========================================================

	    PageResponse<FeeStructureResponseDto> response =
	            new PageResponse<>();

	    response.setContent(content);
	    response.setPageNumber(result.getNumber());
	    response.setPageSize(result.getSize());
	    response.setTotalElements(result.getTotalElements());
	    response.setTotalPages(result.getTotalPages());
	    response.setLast(result.isLast());

	    return response;
	}
	

	@Override
	@Transactional(readOnly = true)
	public PageResponse<FeeStructureResponseDto> searchFeeStructures(
	        FeeStructureSearchRequest request,
	        int page,
	        int size,
	        String sortBy,
	        String direction) {

	    // ==========================================================
	    // 1. Sorting
	    // ==========================================================

	    Sort sort = direction.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    // ==========================================================
	    // 2. Pageable
	    // ==========================================================

	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            sort
	    );

	    // ==========================================================
	    // 3. Specification
	    // ==========================================================

	    Specification<FeeStructure> specification =
	            FeeStructureSpecification.search(request);

	    // ==========================================================
	    // 4. Search
	    // ==========================================================

	    Page<FeeStructure> result =
	            repository.findAll(
	                    specification,
	                    pageable
	            );

	    // ==========================================================
	    // 5. Entity -> DTO
	    // ==========================================================

	    List<FeeStructureResponseDto> content =
	            result.getContent()
	                    .stream()
	                    .map(mapper::toDto)
	                    .toList();

	    // ==========================================================
	    // 6. Prepare Response
	    // ==========================================================

	    PageResponse<FeeStructureResponseDto> response =
	            new PageResponse<>();

	    response.setContent(content);
	    response.setPageNumber(result.getNumber());
	    response.setPageSize(result.getSize());
	    response.setTotalElements(result.getTotalElements());
	    response.setTotalPages(result.getTotalPages());
	    response.setLast(result.isLast());

	    return response;
	}

	

}