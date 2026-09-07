package com.sms.serviceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import com.sms.dto.PageResponse;
import com.sms.dto.SemesterRequestDto;
import com.sms.dto.SemesterResponseDto;
import com.sms.dto.SemesterSearchRequest;
import com.sms.entity.Course;
import com.sms.entity.Semester;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.SemesterMapper;
import com.sms.repository.CourseRepository;
import com.sms.repository.SemesterRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.SemesterService;
import com.sms.specification.SemesterSpecification;

@Service
@Transactional
public class SemesterServiceImpl implements SemesterService {


    private final SemesterRepository repository;
    private final SemesterMapper mapper;
    private final CourseRepository courseRepository;

    public SemesterServiceImpl(
            SemesterRepository repository,
            SemesterMapper mapper,
            CourseRepository courseRepository
    ) {

        this.repository = repository;
        this.mapper = mapper;
        this.courseRepository = courseRepository;
    }


   
    // ================= CREATE =================

    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.CREATE,
            module = "SEMESTER",
            description = "Create semester"
    )
    public SemesterResponseDto createSemester(
            SemesterRequestDto dto
    ) {


        // 1. Common Validation

		validateSemester(dto, null, false);

        

        // 2. Fetch ACTIVE Course

        Course course =
                courseRepository
                .findByIdAndStatus(
                        dto.getCourseId(),
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Course not found with id: "
                                + dto.getCourseId()
                        )
                );

     // 3. DTO -> Entity

        Semester semester = mapper.toEntity(dto);

        // 4. Relationship

        semester.setCourse(course);

        // 5. Default Status

        semester.setStatus(RecordStatus.ACTIVE);

        // 6. Auto Calculate Total Working Days

        int totalWorkingDays =

                (int) ChronoUnit.DAYS.between(
                        semester.getSemesterStartDate(),
                        semester.getSemesterEndDate()
                ) + 1;

        semester.setTotalWorkingDays(totalWorkingDays);

        // 7. Save
        
	    // 8. Create info
        semester.setCreatedByUserId(
	 		        SecurityUtils.getCurrentUserId()
	 		);

        semester.setCreatedBy(
	 		        SecurityUtils.getCurrentFullName()
	 		);

        semester.setCreatedAt(
	 		        LocalDateTime.now()
	 		);

        Semester saved = repository.save(semester);

        // 9. Response

        return mapper.toDto(saved);

    }





    // ================= GET BY ID =================


    @Override
    public SemesterResponseDto getSemesterById(
            Long id
    ) {


        Semester semester =
                repository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                       "Semester not found with id: "
                       + id
                    )

                );



        return mapper.toDto(semester);

    }


    // ================= UPDATE =================

    @AuditLog(
            action = AuditAction.UPDATE,
            module = "SEMESTER",
            description = "Update semester"
    )
    @Override
    @Transactional
    public SemesterResponseDto updateSemester(
            Long id,
            SemesterRequestDto dto
    ) {

        // ==========================
        // 1. Fetch Existing Semester
        // ==========================

        Semester semester =
                repository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Semester not found with id: " + id
                        )
                );


        // ==========================
        // 2. Common Validation
        // ==========================

        validateSemester(
                dto,
                id,
                true
        );


        // ==========================
        // 3. Fetch ACTIVE Course
        // ==========================

        Course course =
                courseRepository
                .findByIdAndStatus(
                        dto.getCourseId(),
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Course not found with id: "
                                        + dto.getCourseId()
                        )
                );


        // ==========================
        // 4. Update Entity
        // ==========================

        mapper.updateEntity(semester, dto);


        // ==========================
        // 5. Update Relationship
        // ==========================

        semester.setCourse(course);


        // ==========================
        // 6. Auto Calculate Working Days
        // ==========================

        semester.setTotalWorkingDays(

                calculateTotalWorkingDays(
                        semester.getSemesterStartDate(),
                        semester.getSemesterEndDate()
                )

        );

	    // 7. Update info
        semester.setCreatedByUserId(
	 		        SecurityUtils.getCurrentUserId()
	 		);

        semester.setCreatedBy(
	 		        SecurityUtils.getCurrentFullName()
	 		);

        semester.setCreatedAt(
	 		        LocalDateTime.now()
	 		);

        // ==========================
        // 8. Save
        // ==========================

        Semester updated =
                repository.save(semester);


        // ==========================
        // 9. Response
        // ==========================

        return mapper.toDto(updated);

    }






    // ================= DELETE =================

    @AuditLog(
            action = AuditAction.DELETE,
            module = "SEMESTER",
            description = "Delete semester"
    )
    @Override
    public void deleteSemester(Long id) {



        Semester semester =
                repository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                      "Semester not found with id: "
                      + id
                    )
                );



        semester.setStatus(
        		RecordStatus.DELETED
        );



        repository.save(semester);

    }








    // ================= GET ALL =================


    @Override
    public PageResponse<SemesterResponseDto> getAllSemesters(
            int page,
            int size,
            String sortBy,
            String direction
    ) {



        Sort sort =
                direction.equalsIgnoreCase("asc")
                ?
                Sort.by(sortBy).ascending()
                :
                Sort.by(sortBy).descending();



        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );



        Page<Semester> semesterPage =
                repository.findByStatusNot(
                		RecordStatus.DELETED,
                        pageable
                );



        List<SemesterResponseDto> content =
                semesterPage.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();




        PageResponse<SemesterResponseDto> response =
                new PageResponse<>();


        response.setContent(content);

        response.setPageNumber(
                semesterPage.getNumber()
        );

        response.setPageSize(
                semesterPage.getSize()
        );

        response.setTotalElements(
                semesterPage.getTotalElements()
        );

        response.setTotalPages(
                semesterPage.getTotalPages()
        );

        response.setLast(
                semesterPage.isLast()
        );



        return response;

    }






 // ==================================================
 // SEARCH
 // ==================================================

 @Override
 @Transactional(readOnly = true)
 public PageResponse<SemesterResponseDto> searchSemesters(

         SemesterSearchRequest request,

         int page,

         int size,

         String sortBy,

         String direction

 ) {

     Specification<Semester> spec =
             Specification.where(null);



     // ==================================================
     // Default ACTIVE Records
     // ==================================================

     if (request.getStatus() != null) {

         spec = spec.and(

                 SemesterSpecification.hasStatus(
                         request.getStatus()
                 )

         );

     } else {

         spec = spec.and(

                 SemesterSpecification.hasStatus(
                         RecordStatus.ACTIVE
                 )

         );

     }



     // ==================================================
     // Semester Id
     // ==================================================

     if (request.getSemesterId() != null) {

         spec = spec.and(

                 SemesterSpecification.hasSemesterId(
                         request.getSemesterId()
                 )

         );

     }



     // ==================================================
     // Semester Name
     // ==================================================

     if (request.getSemesterName() != null
             && !request.getSemesterName().isBlank()) {

         spec = spec.and(

                 SemesterSpecification.hasSemesterName(
                         request.getSemesterName()
                 )

         );

     }



     // ==================================================
     // Semester Number
     // ==================================================

     if (request.getSemesterNumber() != null) {

         spec = spec.and(

                 SemesterSpecification.hasSemesterNumber(
                         request.getSemesterNumber()
                 )

         );

     }



     // ==================================================
     // Course Id
     // ==================================================

     if (request.getCourseId() != null) {

         spec = spec.and(

                 SemesterSpecification.hasCourse(
                         request.getCourseId()
                 )

         );

     }



     // ==================================================
     // Course Code
     // ==================================================

     if (request.getCourseCode() != null
             && !request.getCourseCode().isBlank()) {

         spec = spec.and(

                 SemesterSpecification.hasCourseCode(
                         request.getCourseCode()
                 )

         );

     }



     // ==================================================
     // Course Name
     // ==================================================

     if (request.getCourseName() != null
             && !request.getCourseName().isBlank()) {

         spec = spec.and(

                 SemesterSpecification.hasCourseName(
                         request.getCourseName()
                 )

         );

     }



     // ==================================================
     // Minimum Working Days
     // ==================================================

     if (request.getMinWorkingDays() != null) {

         spec = spec.and(

                 SemesterSpecification.hasMinWorkingDays(
                         request.getMinWorkingDays()
                 )

         );

     }



     // ==================================================
     // Maximum Working Days
     // ==================================================

     if (request.getMaxWorkingDays() != null) {

         spec = spec.and(

                 SemesterSpecification.hasMaxWorkingDays(
                         request.getMaxWorkingDays()
                 )

         );

     }



     // ==================================================
     // Semester Start Date From
     // ==================================================

     if (request.getStartDateFrom() != null) {

         spec = spec.and(

                 SemesterSpecification.hasStartDateFrom(
                         request.getStartDateFrom()
                 )

         );

     }



     // ==================================================
     // Semester Start Date To
     // ==================================================

     if (request.getStartDateTo() != null) {

         spec = spec.and(

                 SemesterSpecification.hasStartDateTo(
                         request.getStartDateTo()
                 )

         );

     }



     // ==================================================
     // Semester End Date From
     // ==================================================

     if (request.getEndDateFrom() != null) {

         spec = spec.and(

                 SemesterSpecification.hasEndDateFrom(
                         request.getEndDateFrom()
                 )

         );

     }



     // ==================================================
     // Semester End Date To
     // ==================================================

     if (request.getEndDateTo() != null) {

         spec = spec.and(

                 SemesterSpecification.hasEndDateTo(
                         request.getEndDateTo()
                 )

         );

     }



     // ==================================================
     // Sorting
     // ==================================================

     Sort sort =
             direction.equalsIgnoreCase("asc")

                     ? Sort.by(sortBy).ascending()

                     : Sort.by(sortBy).descending();



     // ==================================================
     // Pageable
     // ==================================================

     Pageable pageable =
             PageRequest.of(
                     page,
                     size,
                     sort
             );



     // ==================================================
     // Fetch Data
     // ==================================================

     Page<Semester> pageResult =
             repository.findAll(
                     spec,
                     pageable
             );



     // ==================================================
     // Entity -> DTO
     // ==================================================

     List<SemesterResponseDto> content =
             pageResult
                     .getContent()
                     .stream()
                     .map(mapper::toDto)
                     .toList();



     // ==================================================
     // Response
     // ==================================================

     PageResponse<SemesterResponseDto> response =
             new PageResponse<>();

     response.setContent(content);

     response.setPageNumber(pageResult.getNumber());

     response.setPageSize(pageResult.getSize());

     response.setTotalElements(pageResult.getTotalElements());

     response.setTotalPages(pageResult.getTotalPages());

     response.setLast(pageResult.isLast());

     return response;
 }

    
 private void validateSemester(
	        SemesterRequestDto dto,
	        Long id,
	        boolean update
	) {

	    // ==========================
	    // 1. Date Validation
	    // ==========================

	    if (dto.getSemesterStartDate()
	            .isAfter(dto.getSemesterEndDate())) {

	        throw new BusinessException(
	                "Semester start date cannot be after end date."
	        );
	    }

	    if (dto.getSemesterStartDate()
	            .isEqual(dto.getSemesterEndDate())) {

	        throw new BusinessException(
	                "Semester start date and end date cannot be same."
	        );
	    }

	    // ==========================
	    // 2. Duplicate Semester Number
	    // ==========================

	    boolean duplicateSemester;

	    if (update) {

	        duplicateSemester =
	                repository.existsByCourseIdAndSemesterNumberAndIdNot(
	                        dto.getCourseId(),
	                        dto.getSemesterNumber(),
	                        id
	                );

	    } else {

	        duplicateSemester =
	                repository.existsByCourseIdAndSemesterNumber(
	                        dto.getCourseId(),
	                        dto.getSemesterNumber()
	                );
	    }

	    if (duplicateSemester) {

	        throw new BusinessException(
	                "Semester number already exists for this course."
	        );
	    }

	    // ==========================
	    // 3. Date Overlap Validation
	    // ==========================

	    boolean overlap;

	    if (update) {

	        overlap =
	                repository.existsSemesterOverlapForUpdate(
	                        id,
	                        dto.getCourseId(),
	                        dto.getSemesterStartDate(),
	                        dto.getSemesterEndDate()
	                );

	    } else {

	        overlap =
	                repository.existsSemesterOverlap(
	                        dto.getCourseId(),
	                        dto.getSemesterStartDate(),
	                        dto.getSemesterEndDate()
	                );
	    }

	    if (overlap) {

	        throw new BusinessException(
	                "Semester date range overlaps with existing semester."
	        );
	    }
	    
	    

	}
 
	 private Integer calculateTotalWorkingDays(
		        LocalDate semesterStartDate,
		        LocalDate semesterEndDate
		) {
	
		    return (int) ChronoUnit.DAYS.between(
		            semesterStartDate,
		            semesterEndDate
		    ) + 1;
	
		}

}