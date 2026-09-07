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
import com.sms.dto.ExamRequestDto;
import com.sms.dto.ExamResponseDto;
import com.sms.dto.ExamSearchRequest;
import com.sms.dto.PageResponse;
import com.sms.entity.Exam;
import com.sms.entity.Semester;
import com.sms.entity.Subject;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.ExamMapper;
import com.sms.repository.ExamRepository;
import com.sms.repository.SemesterRepository;
import com.sms.repository.SubjectRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.ExamService;
import com.sms.specification.ExamSpecification;

import lombok.RequiredArgsConstructor;



@Service
@Transactional
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {


    private final ExamRepository repository;
    private final SubjectRepository subjectRepository;
    private final ExamMapper mapper;
    private final SemesterRepository semesterRepository;

    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.CREATE,
            module = "EXAM",
            description = "Create new exam"
    )
    public ExamResponseDto createExam(
            ExamRequestDto dto
    ) {


        // ==========================
        // 1. Duplicate Check
        // ==========================


        boolean exists =
                repository
                .existsBySubjectIdAndExamDateAndExamTypeAndStatusNot(
                        dto.getSubjectId(),
                        dto.getExamDate(),
                        dto.getExamType(),
                        RecordStatus.DELETED
                );


        if(exists){

            throw new BusinessException(
                    "Exam already exists for this subject and date."
            );
        }




        // ==========================
        // 2. Subject Validation
        // ==========================


        Subject subject =
                subjectRepository
                .findByIdAndStatusNot(
                        dto.getSubjectId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Subject not found with id : "
                                + dto.getSubjectId()
                        )
                );





        // ==========================
        // 3. Semester Validation
        // ==========================


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





        // ==========================
        // 4. Business Validation
        // ==========================


        validateExam(
                dto,
                semester
        );





        // ==========================
        // 5. DTO -> ENTITY
        // ==========================


        Exam exam =
                mapper.toEntity(dto);





        // ==========================
        // 6. Set Relationships
        // ==========================


        exam.setSubject(
                subject
        );


        exam.setSemester(
                semester
        );





        // ==========================
        // 7. Default Status
        // ==========================


        if(exam.getStatus() == null){

            exam.setStatus(
                    RecordStatus.ACTIVE
            );
        }





        // ==========================
        // 8. Creation Audit Information
        // ==========================


        exam.setCreatedByUserId(
                SecurityUtils.getCurrentUserId()
        );


        exam.setCreatedBy(
                SecurityUtils.getCurrentFullName()
        );


        exam.setCreatedDate(
                LocalDateTime.now()
        );





        // ==========================
        // 9. Save Entity
        // ==========================


        Exam saved =
                repository.save(exam);





        // ==========================
        // 10. Fetch Response Data
        // ==========================


        Exam responseExam =
                repository
                .findByIdAndStatusNot(
                        saved.getId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Exam not found"
                        )
                );





        // ==========================
        // 11. Entity -> DTO
        // ==========================


        return mapper.toDto(
                responseExam
        );

    }
    
    
    

	
    @Override
    @Transactional(readOnly = true)
    public ExamResponseDto getExamById(Long id) {


        // ==========================
        // 1. Find Active Exam
        // ==========================

        Exam exam =
                repository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Exam not found with id : "
                                + id
                        )
                );


        // ==========================
        // 2. Entity -> DTO
        // ==========================

        return mapper.toDto(exam);

    }
    
    
    

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ExamResponseDto> getAllExams(
            int page,
            int size,
            String sortBy,
            String direction) {


        // ==========================
        // 1. Sorting
        // ==========================


        Sort sort =
                direction.equalsIgnoreCase("asc")

                ?
                Sort.by(sortBy).ascending()

                :
                Sort.by(sortBy).descending();



        // ==========================
        // 2. Pageable
        // ==========================


        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );



        // ==========================
        // 3. Fetch Non Deleted Data
        // ==========================


        Page<Exam> result =
                repository.findByStatusNot(
                		RecordStatus.DELETED,
                        pageable
                );



        // ==========================
        // 4. Convert Entity -> DTO
        // ==========================


        List<ExamResponseDto> content =
                result
                .getContent()
                .stream()
                .map(mapper::toDto)
                .toList();



        // ==========================
        // 5. Prepare Response
        // ==========================


        PageResponse<ExamResponseDto> response =
                new PageResponse<>();

        response.setContent(content);

        response.setPageNumber(
                result.getNumber()
        );

        response.setPageSize(
                result.getSize()
        );

        response.setTotalElements(
                result.getTotalElements()
        );

        response.setTotalPages(
                result.getTotalPages()
        );

        response.setLast(
                result.isLast()
        );

        return response;

    }
    
    

    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "EXAM",
            description = "Update exam details"
    )
    public ExamResponseDto updateExam(
            Long id,
            ExamRequestDto dto
    ) {


        // ==========================
        // 1. Find Existing Exam
        // ==========================

        Exam exam =
                repository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                        "Exam not found with id : "
                        + id
                    )
                );




        // ==========================
        // 2. Duplicate Validation
        // ==========================


        boolean exists =
                repository
                .existsBySubjectIdAndExamDateAndExamTypeAndStatusNotAndIdNot(
                        dto.getSubjectId(),
                        dto.getExamDate(),
                        dto.getExamType(),
                        RecordStatus.DELETED,
                        id
                );


        if(exists){

            throw new BusinessException(
                    "Exam already exists for this subject and date."
            );
        }




        // ==========================
        // 3. Semester Validation
        // ==========================


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




        // ==========================
        // 4. Business Validation
        // ==========================


        validateExam(
                dto,
                semester
        );




        // ==========================
        // 5. Subject Validation
        // ==========================


        Subject subject =
                subjectRepository
                .findByIdAndStatusNot(
                        dto.getSubjectId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                        "Subject not found with id : "
                        + dto.getSubjectId()
                    )
                );




        // ==========================
        // 6. Update Simple Fields
        // ==========================

        mapper.updateEntity(
                exam,
                dto
        );




        // ==========================
        // 7. Update Relationships
        // ==========================

        exam.setSubject(subject);

        exam.setSemester(semester);

        // Update info
        exam.setCreatedByUserId(
                SecurityUtils.getCurrentUserId()
        );

        exam.setCreatedBy(
                SecurityUtils.getCurrentFullName()
        );

        exam.setCreatedDate(
                LocalDateTime.now()
        );


        // ==========================
        // 8. Save
        // ==========================

        Exam updated =
                repository.save(exam);




        // ==========================
        // 9. Fetch Updated Data
        // ==========================

        Exam responseExam =
                repository
                .findByIdAndStatusNot(
                        updated.getId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                        "Exam not found"
                    )
                );




        return mapper.toDto(
                responseExam
        );

    }
    

    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.DELETE,
            module = "EXAM",
            description = "Delete exam"
    )
    public void deleteExam(Long id) {


        // ==========================
        // 1. Find Existing Exam
        // ==========================


        Exam exam =
                repository
                .findByIdAndStatusNot(
                        id,
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Exam not found with id : "
                                + id
                        )
                );



        // ==========================
        // 2. Soft Delete
        // ==========================


        exam.setStatus(
        		RecordStatus.DELETED
        );


        // ==========================
        // 3. Save
        // ==========================
        repository.save(exam);

    }

	
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ExamResponseDto> searchExams(
            ExamSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction) {



        // ==========================
        // 1. Create Specification
        // ==========================


        Specification<Exam> spec =
                Specification.where(null);



        // ==========================
        // Status Filter
        // ==========================


        if(request.getStatus()!=null){


            spec =
            spec.and(
                    ExamSpecification.hasStatus(
                            request.getStatus()
                    )
            );


        }
        else{


            spec =
            spec.and(
                    ExamSpecification.statusNot(
                    		RecordStatus.DELETED
                    )
            );

        }




        // ==========================
        // Subject Filter
        // ==========================


        if(request.getSubjectId()!=null){


            spec =
            spec.and(
                    ExamSpecification.hasSubject(
                            request.getSubjectId()
                    )
            );

        }




        // ==========================
        // Semester Filter
        // ==========================


        if(request.getSemesterId()!=null){


            spec =
            spec.and(
                    ExamSpecification.hasSemester(
                            request.getSemesterId()
                    )
            );

        }





        // ==========================
        // Exam Type Filter
        // ==========================


        if(request.getExamType()!=null){


            spec =
            spec.and(
                    ExamSpecification.hasExamType(
                            request.getExamType()
                    )
            );

        }





        // ==========================
        // Exam Date Filter
        // ==========================


        if(request.getExamDate()!=null){


            spec =
            spec.and(
                    ExamSpecification.hasExamDate(
                            request.getExamDate()
                    )
            );

        }




        // ==========================
        // Sorting
        // ==========================


        Sort sort =
                direction.equalsIgnoreCase("asc")

                ?
                Sort.by(sortBy).ascending()

                :
                Sort.by(sortBy).descending();





        // ==========================
        // Pagination
        // ==========================


        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );





        // ==========================
        // Database Search
        // ==========================


        Page<Exam> result =
                repository.findAll(
                        spec,
                        pageable
                );





        // ==========================
        // Entity -> DTO
        // ==========================


        List<ExamResponseDto> content =
                result.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();





        // ==========================
        // Response
        // ==========================


        PageResponse<ExamResponseDto> response =
                new PageResponse<>();


        response.setContent(content);

        response.setPageNumber(
                result.getNumber()
        );

        response.setPageSize(
                result.getSize()
        );

        response.setTotalElements(
                result.getTotalElements()
        );

        response.setTotalPages(
                result.getTotalPages()
        );

        response.setLast(
                result.isLast()
        );


        return response;

    }
    
	
    private void validateExam(
            ExamRequestDto dto,
            Semester semester
    ) {

		// ==========================
	    // Time Validation
	    // ==========================

	    if(dto.getStartTime()
	            .isAfter(dto.getEndTime())) {


	        throw new BusinessException(
	                "Exam start time cannot be after end time."
	        );

	    }

	    // ==========================
	    // Exam Date Validation
	    // ==========================

	    if(dto.getExamDate() == null){

	        throw new BusinessException(
	                "Exam date is required."
	        );

	    }


	    if(dto.getExamDate()
	            .isBefore(
	              semester.getSemesterStartDate()
	            )){


	        throw new BusinessException(
	            "Exam date cannot be before semester start date."
	        );

	    }


	    if(dto.getExamDate()
	            .isAfter(
	              semester.getSemesterEndDate()
	            )){


	        throw new BusinessException(
	            "Exam date cannot be after semester end date."
	        );

	    }



	    // ==========================
	    // Marks Validation
	    // ==========================


	    if(dto.getMaximumMarks() == null ||
	       dto.getPassingMarks() == null){


	        throw new BusinessException(
	                "Total marks and passing marks are required."
	        );

	    }



	    if(dto.getMaximumMarks() <= 0){

	        throw new BusinessException(
	                "Total marks must be greater than zero."
	        );

	    }



	    if(dto.getPassingMarks() < 0){

	        throw new BusinessException(
	                "Passing marks cannot be negative."
	        );

	    }



	    if(dto.getPassingMarks()
	            > dto.getMaximumMarks()){


	        throw new BusinessException(
	                "Passing marks cannot be greater than total marks."
	        );

	    }

	}

}
