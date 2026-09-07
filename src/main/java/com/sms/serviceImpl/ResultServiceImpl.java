package com.sms.serviceImpl;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
import com.sms.dto.PageResponse;
import com.sms.dto.ResultRequestDto;
import com.sms.dto.ResultResponseDto;
import com.sms.dto.ResultSearchRequest;
import com.sms.entity.Exam;
import com.sms.entity.Result;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.enums.ResultStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.ResultMapper;
import com.sms.repository.ExamRepository;
import com.sms.repository.ResultRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.ResultService;
import com.sms.specification.ResultSpecification;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class ResultServiceImpl implements ResultService {


    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final ResultMapper resultMapper;



    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.CREATE,
            module = "RESULT",
            description = "Create student result"
    )
    public ResultResponseDto createResult(
            ResultRequestDto request
    ) {


        // ==================================================
        // 1. Fetch ACTIVE Student
        // ==================================================

        Student student =
                studentRepository
                .findByIdAndStatus(
                        request.getStudentId(),
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                        "Student not found with id : "
                        + request.getStudentId()
                    )
                );



        // ==================================================
        // 2. Fetch Exam
        // ==================================================

        Exam exam =
                examRepository
                .findByIdAndStatusNot(
                        request.getExamId(),
                        RecordStatus.DELETED
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                        "Exam not found with id : "
                        + request.getExamId()
                    )
                );



     // ==================================================
     // 3. Duplicate Result Validation
     // ==================================================

        if(resultRepository
                .existsByStudentIdAndExamIdAndStatus(
                        student.getId(),
                        exam.getId(),
                        RecordStatus.ACTIVE
                )) {


            throw new BusinessException(
                    "Result already exists for this student and exam."
            );

        }




        // ==================================================
        // 4. Business Validation
        // ==================================================

        validateResultCreation(
                request,
                student,
                exam
        );




        // ==================================================
        // 5. DTO -> Entity
        // ==================================================

        Result result =

                resultMapper
                .toEntity(request);



        result.setStudent(student);

        result.setExam(exam);




        // ==================================================
        // 6. Calculate Percentage
        // ==================================================

        BigDecimal  percentage =

                calculatePercentage(
                        request.getObtainedMarks(),
                        exam.getMaximumMarks()
                );



        result.setPercentage(
                percentage
        );




        // ==================================================
        // 7. Calculate Grade
        // ==================================================

        result.setGrade(
                calculateGrade(
                        percentage
                )
        );




        // ==================================================
        // 8. Calculate PASS / FAIL
        // ==================================================

        result.setResultStatus(

                calculateResultStatus(
                        request.getObtainedMarks(),
                        exam.getPassingMarks()
                )

        );




        // ==================================================
        // 9. Default Record Status
        // ==================================================

        result.setStatus(
                RecordStatus.ACTIVE
        );

        // Audit
        result.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        result.setUpdatedBy(SecurityUtils.getCurrentFullName());
        result.setUpdatedDate(LocalDateTime.now());

        result.setRemarks(
                request.getRemarks()
        );




        // ==================================================
        // 10. Save Result
        // ==================================================

        Result savedResult =

                resultRepository
                .save(result);




        // ==================================================
        // 11. Entity -> Response DTO
        // ==================================================

        return resultMapper
                .toResponseDto(savedResult);

    }


    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "RESULT",
            description = "Update student result"
    )
    public ResultResponseDto updateResult(
            Long id,
            ResultRequestDto request
    ) {


        // ==================================================
        // 1. Fetch Existing Active Result
        // ==================================================

        Result result =

                resultRepository
                .findByIdAndStatus(
                        id,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->

                    new ResourceNotFoundException(
                        "Result not found with id : "
                        + id
                    )

                );




        // ==================================================
        // 2. Existing Exam Fetch
        // ==================================================

        Exam exam =
                result.getExam();



        // ==================================================
        // 3. Business Validation
        // ==================================================

        validateResultUpdate(
                request,
                exam
        );




        // ==================================================
        // 4. Update  Fields
        // ==================================================

        resultMapper.updateEntity(result, request);



        // ==================================================
        // 5. Recalculate Percentage
        // ==================================================

        BigDecimal  percentage =

                calculatePercentage(
                        request.getObtainedMarks(),
                        exam.getMaximumMarks()
                );


        result.setPercentage(
                percentage
        );





        // ==================================================
        // 6. Recalculate Grade
        // ==================================================

        result.setGrade(
                calculateGrade(
                        percentage
                )
        );





        // ==================================================
        // 7. Recalculate PASS / FAIL
        // ==================================================

        result.setResultStatus(

                calculateResultStatus(
                        request.getObtainedMarks(),
                        exam.getPassingMarks()
                )

        );


        // Audit
        result.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        result.setUpdatedBy(SecurityUtils.getCurrentFullName());
        result.setUpdatedDate(LocalDateTime.now());


        // ==================================================
        // 8. Save Updated Result
        // ==================================================

        Result updatedResult =

                resultRepository
                .save(result);





        // ==================================================
        // 9. Response DTO
        // ==================================================

        return resultMapper
                .toResponseDto(updatedResult);

    }


    @Override
    @Transactional(readOnly = true)
    public ResultResponseDto getResultById(
            Long id
    ) {


        // ==================================================
        // 1. Fetch ACTIVE Result
        // ==================================================

        Result result =

                resultRepository
                .findByIdAndStatus(
                        id,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->


                    new ResourceNotFoundException(
                            "Result not found with id : "
                            + id
                    )

                );




        // ==================================================
        // 2. Entity -> DTO
        // ==================================================

        return resultMapper
                .toResponseDto(result);

    }




    @Override
    @Transactional
    @AuditLog(
            action = AuditAction.DELETE,
            module = "RESULT",
            description = "Delete student result"
    )
    public void deleteResult(
            Long id
    ) {


        // ==================================================
        // 1. Fetch ACTIVE Result
        // ==================================================

        Result result =

                resultRepository
                .findByIdAndStatus(
                        id,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->


                    new ResourceNotFoundException(
                            "Result not found with id : "
                            + id
                    )

                );




        // ==================================================
        // 2. Soft Delete
        // ==================================================

        result.setStatus(
                RecordStatus.DELETED
        );




        // ==================================================
        // 3. Save
        // ==================================================

        resultRepository.save(result);

    }





    @Override
    @Transactional(readOnly = true)
    public PageResponse<ResultResponseDto> getAllResults(
            int page,
            int size,
            String sortBy,
            String direction
    ) {


        // ==================================================
        // 1. Sorting
        // ==================================================

        Sort sort =

                direction.equalsIgnoreCase("asc")

                ?

                Sort.by(sortBy).ascending()

                :

                Sort.by(sortBy).descending();




        // ==================================================
        // 2. Pagination
        // ==================================================

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        sort
                );





        // ==================================================
        // 3. Fetch ACTIVE Results
        // ==================================================

        Page<Result> resultPage =

                resultRepository
                .findByStatus(
                        RecordStatus.ACTIVE,
                        pageable
                );





        // ==================================================
        // 4. Entity -> DTO List
        // ==================================================

        List<ResultResponseDto> content =

                resultPage
                .getContent()
                .stream()
                .map(resultMapper::toResponseDto)
                .toList();






        // ==================================================
        // 5. Create Page Response
        // ==================================================

        PageResponse<ResultResponseDto> response =

                new PageResponse<>();




        response.setContent(content);



        response.setPageNumber(
                resultPage.getNumber()
        );



        response.setPageSize(
                resultPage.getSize()
        );



        response.setTotalElements(
                resultPage.getTotalElements()
        );



        response.setTotalPages(
                resultPage.getTotalPages()
        );



        response.setLast(
                resultPage.isLast()
        );




        return response;

    }





    @Override
    @Transactional(readOnly = true)
    public PageResponse<ResultResponseDto> searchResults(
            ResultSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    ) {


        // ==================================================
        // 1. Search Request Validation
        // ==================================================

        validateSearchRequest(request);



        // ==================================================
        // 2. Sorting
        // ==================================================

        Sort sort =

                direction.equalsIgnoreCase("asc")

                ?

                Sort.by(sortBy).ascending()

                :

                Sort.by(sortBy).descending();




        // ==================================================
        // 3. Pagination
        // ==================================================

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        sort
                );




        // ==================================================
        // 4. Create Specification
        // ==================================================

        Specification<Result> specification =

                ResultSpecification
                .filter(request);




        // ==================================================
        // 5. Search Database
        // ==================================================

        Page<Result> resultPage =

                resultRepository
                .findAll(
                        specification,
                        pageable
                );




        // ==================================================
        // 6. Entity -> DTO
        // ==================================================

        List<ResultResponseDto> content =

                resultPage
                .getContent()
                .stream()
                .map(resultMapper::toResponseDto)
                .toList();




        // ==================================================
        // 7. Page Response
        // ==================================================

        PageResponse<ResultResponseDto> response =

                new PageResponse<>();


        response.setContent(content);

        response.setPageNumber(
                resultPage.getNumber()
        );

        response.setPageSize(
                resultPage.getSize()
        );

        response.setTotalElements(
                resultPage.getTotalElements()
        );

        response.setTotalPages(
                resultPage.getTotalPages()
        );

        response.setLast(
                resultPage.isLast()
        );


        return response;

    }

    
	// ==================================================
	// Result Creation Business Validation
	// ==================================================

  
    private void validateResultCreation(
            ResultRequestDto request,
            Student student,
            Exam exam) {


        // ==================================================
        // 1. Student Admission Date Validation
        // ==================================================

        if (student.getAdmissionDate() == null) {

            throw new BusinessException(
                    "Student admission date is missing."
            );

        }


        // ==================================================
        // 2. Exam Date Validation
        // ==================================================

        if (exam.getExamDate() == null) {

            throw new BusinessException(
                    "Exam date is required."
            );

        }


        // ==================================================
        // 3. Exam Date Future Check
        // ==================================================

        if (exam.getExamDate().isAfter(LocalDate.now())) {

            throw new BusinessException(
                    "Cannot create result before exam date."
            );

        }


        // ==================================================
        // 4. Exam Date Before Student Admission Check
        // ==================================================

        if (exam.getExamDate()
                .isBefore(student.getAdmissionDate())) {


            throw new BusinessException(
                    "Exam date cannot be before student admission date."
            );

        }


        // ==================================================
        // 5. Maximum Marks Validation
        // ==================================================

        if (exam.getMaximumMarks() == null
                || exam.getMaximumMarks() <= 0) {


            throw new BusinessException(
                    "Maximum marks must be greater than zero."
            );

        }


        // ==================================================
        // 6. Passing Marks Validation
        // ==================================================

        if (exam.getPassingMarks() == null
                || exam.getPassingMarks() < 0) {


            throw new BusinessException(
                    "Passing marks cannot be negative."
            );

        }


        // ==================================================
        // 7. Passing Marks Cannot Exceed Maximum Marks
        // ==================================================

        if (exam.getPassingMarks()
                > exam.getMaximumMarks()) {


            throw new BusinessException(
                    "Passing marks cannot be greater than maximum marks."
            );

        }


        // ==================================================
        // 8. Obtained Marks Required
        // ==================================================

        if (request.getObtainedMarks() == null) {

            throw new BusinessException(
                    "Obtained marks is required."
            );

        }


        // ==================================================
        // 9. Obtained Marks Negative Check
        // ==================================================

        if (request.getObtainedMarks() < 0) {

            throw new BusinessException(
                    "Obtained marks cannot be negative."
            );

        }


        // ==================================================
        // 10. Obtained Marks Greater Than Maximum Marks
        // ==================================================

        if (request.getObtainedMarks()
                > exam.getMaximumMarks()) {


            throw new BusinessException(
                    "Obtained marks cannot be greater than maximum marks."
            );

        }

    }
    

	
	
	
	// ==================================================
	// Result Update Validation
	// ==================================================

	private void validateResultUpdate(
	        ResultRequestDto request,
	        Exam exam
	) {


		// ==================================================
		// 1. Obtained Marks Null Check
		// ==================================================

		if (request.getObtainedMarks() == null) {

		    throw new BusinessException(
		            "Obtained marks is required."
		    );

		}

	    // ==================================================
	    // 2. Negative Marks Check
	    // ==================================================

	    if(request.getObtainedMarks() < 0) {


	        throw new BusinessException(
	                "Obtained marks cannot be negative."
	        );

	    }




	    // ==================================================
	    // 3. Maximum Marks Check
	    // ==================================================

	    if(request.getObtainedMarks()
	            > exam.getMaximumMarks()) {


	        throw new BusinessException(
	                "Obtained marks cannot be greater than maximum marks."
	        );

	    }



	    // ==================================================
	    // 4. Exam Marks Configuration Check
	    // ==================================================

	    if(exam.getPassingMarks()
	            > exam.getMaximumMarks()) {


	        throw new BusinessException(
	                "Passing marks cannot be greater than maximum marks."
	        );

	    }


	}
	
	
	// ==================================================
	// Search Request Validation
	// ==================================================

	private void validateSearchRequest(
	        ResultSearchRequest request
	) {


	    // ==================================================
	    // Percentage Validation
	    // ==================================================

	    if (request.getMinPercentage() != null
	            && request.getMinPercentage()
	            .compareTo(BigDecimal.ZERO) < 0) {


	        throw new BusinessException(
	                "Minimum percentage cannot be negative."
	        );

	    }


	    if (request.getMaxPercentage() != null
	            && request.getMaxPercentage()
	            .compareTo(BigDecimal.valueOf(100)) > 0) {


	        throw new BusinessException(
	                "Maximum percentage cannot be greater than 100."
	        );

	    }


	    if (request.getMinPercentage() != null
	            && request.getMaxPercentage() != null
	            && request.getMinPercentage()
	            .compareTo(request.getMaxPercentage()) > 0) {


	        throw new BusinessException(
	                "Minimum percentage cannot be greater than maximum percentage."
	        );

	    }



	    // ==================================================
	    // Obtained Marks Validation
	    // ==================================================

	    if (request.getMinObtainedMarks() != null
	            && request.getMinObtainedMarks() < 0) {


	        throw new BusinessException(
	                "Minimum obtained marks cannot be negative."
	        );

	    }



	    if (request.getMaxObtainedMarks() != null
	            && request.getMaxObtainedMarks() < 0) {


	        throw new BusinessException(
	                "Maximum obtained marks cannot be negative."
	        );

	    }



	    // ==================================================
	    // Marks Range Validation
	    // ==================================================

	    if (request.getMinObtainedMarks() != null
	            && request.getMaxObtainedMarks() != null
	            && request.getMinObtainedMarks()
	            > request.getMaxObtainedMarks()) {


	        throw new BusinessException(
	                "Minimum obtained marks cannot be greater than maximum obtained marks."
	        );

	    }

	}
	

	// ==================================================
	// Calculate Percentage
	// ==================================================

	private BigDecimal calculatePercentage(
	        Integer obtainedMarks,
	        Integer maximumMarks
	) {

	    return BigDecimal
	            .valueOf(obtainedMarks)
	            .divide(
	                    BigDecimal.valueOf(maximumMarks),
	                    4,
	                    RoundingMode.HALF_UP
	            )
	            .multiply(BigDecimal.valueOf(100))
	            .setScale(2
	            		, RoundingMode.HALF_UP);

	}

	// ==================================================
	// Calculate Grade
	// ==================================================

	private String calculateGrade(BigDecimal percentage) {

	    if (percentage.compareTo(BigDecimal.valueOf(90)) >= 0) {
	        return "A+";
	    }

	    if (percentage.compareTo(BigDecimal.valueOf(80)) >= 0) {
	        return "A";
	    }

	    if (percentage.compareTo(BigDecimal.valueOf(70)) >= 0) {
	        return "B";
	    }

	    return "C";
	}

	// ==================================================
	// Calculate Result Status
	// ==================================================

	private ResultStatus calculateResultStatus(Integer obtainedMarks, Integer passingMarks) {

		if (obtainedMarks >= passingMarks) {

			return ResultStatus.PASS;

		}

		return ResultStatus.FAIL;

	}

}