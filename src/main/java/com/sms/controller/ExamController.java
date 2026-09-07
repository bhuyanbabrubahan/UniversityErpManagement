package com.sms.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;

import com.sms.dto.ExamRequestDto;
import com.sms.dto.ExamResponseDto;
import com.sms.dto.ExamSearchRequest;
import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.service.ExamService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Tag(
        name = "Exam Management",
        description = "APIs for managing examinations including create, retrieve, update, delete and dynamic search operations."
)
public class ExamController {



    private final ExamService service;



	 // ==========================
	 // CREATE
	 // ==========================
	
	 @Operation(
	         summary = "Create Exam",
	         description = """
	                 Creates a new examination.
	
	                 Business Validations:
	                 - Semester must exist and be ACTIVE
	                 - Subject must exist and belong to the selected semester
	                 - Exam date must be within semester duration
	                 - End time must be after start time
	                 - Passing marks cannot exceed maximum marks
	                 - Duplicate exam should not exist
	                 """
	 )
	 @ApiResponses({
	         @ApiResponse(
	                 responseCode = "201",
	                 description = "Exam created successfully"
	         ),
	         @ApiResponse(
	                 responseCode = "400",
	                 description = "Invalid request data or business validation failed"
	         ),
	         @ApiResponse(
	                 responseCode = "404",
	                 description = "Semester or Subject not found"
	         ),
	         @ApiResponse(
	                 responseCode = "409",
	                 description = "Duplicate examination already exists"
	         )
	 })
	 @AuditLog(
		        action = AuditAction.CREATE,
		        module = "EXAM",
		        description = "Create exam"
		)
		@PostMapping
	 public ResponseEntity<ApiResponseDto<ExamResponseDto>> createExam(
	
	         @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                 description = "Exam information",
	                 required = true
	         )
	         @Valid
	         @RequestBody ExamRequestDto dto
	
	 ) {
	
	     ExamResponseDto response =
	             service.createExam(dto);
	
	     return ResponseBuilder.success(
	             response,
	             "Exam created successfully",
	             HttpStatus.CREATED
	     );
	
	 }





	// ==========================
	// GET BY ID
	// ==========================

	@Operation(
	        summary = "Get Exam By ID",
	        description = "Retrieves an active examination using the unique exam ID."
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Exam fetched successfully"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Exam not found"
	        )
	})
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<ExamResponseDto>> getById(

	        @Parameter(
	                description = "Unique Exam ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id

	) {

	    return ResponseBuilder.success(
	            service.getExamById(id),
	            "Exam fetched successfully",
	            HttpStatus.OK
	    );

	}




	// ==========================
	// GET ALL
	// ==========================

	@Operation(
	        summary = "Get All Exams",
	        description = """
	                Retrieves all active examinations.

	                Features:
	                - Pagination
	                - Sorting
	                - Default ACTIVE records only
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Examinations fetched successfully"
	        )
	})
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponse<ExamResponseDto>>> getAll(

	        @Parameter(
	                description = "Page number",
	                example = "0"
	        )
	        @RequestParam(defaultValue = "0")
	        int page,

	        @Parameter(
	                description = "Number of records per page",
	                example = "10"
	        )
	        @RequestParam(defaultValue = "10")
	        int size,

	        @Parameter(
	                description = "Field used for sorting",
	                example = "id"
	        )
	        @RequestParam(defaultValue = "id")
	        String sortBy,

	        @Parameter(
	                description = "Sorting direction",
	                example = "asc"
	        )
	        @RequestParam(defaultValue = "asc")
	        String direction

	) {

	    return ResponseBuilder.success(

	            service.getAllExams(
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            ),

	            "Examinations fetched successfully",

	            HttpStatus.OK
	    );

	}





	// ==========================
	// UPDATE
	// ==========================

	@Operation(
	        summary = "Update Exam",
	        description = """
	                Updates an existing examination.

	                Business Validations:
	                - Exam must exist
	                - Semester must exist and be ACTIVE
	                - Subject must exist and belong to the selected semester
	                - Exam date must be within semester duration
	                - End time must be after start time
	                - Passing marks cannot exceed maximum marks
	                - Duplicate examination should not exist
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Exam updated successfully"
	        ),
	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid request data or business validation failed"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Exam, Semester or Subject not found"
	        ),
	        @ApiResponse(
	                responseCode = "409",
	                description = "Duplicate examination already exists"
	        )
	})
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "EXAM",
	        description = "Update exam"
	)
	@PutMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<ExamResponseDto>> update(

	        @Parameter(
	                description = "Unique Exam ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id,

	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Updated examination information",
	                required = true
	        )
	        @Valid
	        @RequestBody ExamRequestDto dto

	) {

	    return ResponseBuilder.success(

	            service.updateExam(
	                    id,
	                    dto
	            ),

	            "Exam updated successfully",

	            HttpStatus.OK
	    );

	}





	// ==========================
	// DELETE
	// ==========================

	@Operation(
	        summary = "Delete Exam",
	        description = """
	                Soft deletes an examination.

	                Business Validations:
	                - Exam must exist
	                - Exam must not already be deleted
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Exam deleted successfully"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Exam not found"
	        )
	})
	@AuditLog(
	        action = AuditAction.DELETE,
	        module = "EXAM",
	        description = "Delete exam"
	)
	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<Void>> delete(

	        @Parameter(
	                description = "Unique Exam ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id

	) {

	    service.deleteExam(id);

	    return ResponseBuilder.success(
	            null,
	            "Exam deleted successfully",
	            HttpStatus.OK
	    );

	}




	// ==========================
	// SEARCH
	// ==========================

	@Operation(
	        summary = "Search Exams",
	        description = """
	                Performs dynamic examination search using optional filters.

	                Supported Filters:
	                - Semester ID
	                - Subject ID
	                - Exam Type
	                - Exam Date
	                - Academic Year
	                - Record Status

	                Features:
	                - Dynamic Specification
	                - Pagination
	                - Sorting
	                - Case-insensitive search where applicable
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Examination search completed successfully"
	        )
	})
	@PostMapping("/search")
	public ResponseEntity<ApiResponseDto<PageResponse<ExamResponseDto>>> search(

	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Examination search criteria",
	                required = true
	        )
	        @Valid
	        @RequestBody ExamSearchRequest request,

	        @Parameter(
	                description = "Page number",
	                example = "0"
	        )
	        @RequestParam(defaultValue = "0")
	        int page,

	        @Parameter(
	                description = "Number of records per page",
	                example = "10"
	        )
	        @RequestParam(defaultValue = "10")
	        int size,

	        @Parameter(
	                description = "Field used for sorting",
	                example = "id"
	        )
	        @RequestParam(defaultValue = "id")
	        String sortBy,

	        @Parameter(
	                description = "Sorting direction",
	                example = "asc"
	        )
	        @RequestParam(defaultValue = "asc")
	        String direction

	) {

	    return ResponseBuilder.success(

	            service.searchExams(
	                    request,
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            ),

	            "Examinations searched successfully",

	            HttpStatus.OK
	    );

	}


}