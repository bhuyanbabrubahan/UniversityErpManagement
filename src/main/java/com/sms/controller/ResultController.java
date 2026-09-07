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

import com.sms.dto.PageResponse;
import com.sms.dto.ResultRequestDto;
import com.sms.dto.ResultResponseDto;
import com.sms.dto.ResultSearchRequest;
import com.sms.payload.ApiResponseDto;
import com.sms.service.ResultService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@Tag(
        name = "Result Management",
        description = "APIs for managing student examination results including create, retrieve, update, delete and dynamic search operations."
)
public class ResultController {



    private final ResultService resultService;



	 // ==========================
	 // CREATE RESULT
	 // ==========================
	
	 @Operation(
	         summary = "Create Result",
	         description = """
	                 Creates a new student examination result.
	
	                 Business Validations:
	                 - Student must exist and be ACTIVE
	                 - Exam must exist and be ACTIVE
	                 - Student must belong to the exam semester
	                 - Obtained marks cannot exceed maximum marks
	                 - Duplicate result is not allowed
	                 - Percentage, Grade and Result Status are calculated automatically
	                 """
	 )
	 @ApiResponses({
	         @ApiResponse(
	                 responseCode = "201",
	                 description = "Result created successfully"
	         ),
	         @ApiResponse(
	                 responseCode = "400",
	                 description = "Business validation failed"
	         ),
	         @ApiResponse(
	                 responseCode = "404",
	                 description = "Student or Exam not found"
	         ),
	         @ApiResponse(
	                 responseCode = "409",
	                 description = "Duplicate result already exists"
	         )
	 })
	 @AuditLog(
		        action = AuditAction.CREATE,
		        module = "RESULT",
		        description = "Create result"
		)
	 @PostMapping
	 public ResponseEntity<ApiResponseDto<ResultResponseDto>> createResult(
	
	         @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                 description = "Student examination result information",
	                 required = true
	         )
	         @Valid
	         @RequestBody ResultRequestDto request
	
	 ) {
	
	     ResultResponseDto response =
	             resultService.createResult(request);
	
	     return ResponseBuilder.success(
	             response,
	             "Result created successfully",
	             HttpStatus.CREATED
	     );
	
	 }



	// ==========================
	// UPDATE RESULT
	// ==========================

	@Operation(
	        summary = "Update Result",
	        description = """
	                Updates an existing student examination result.

	                Business Validations:
	                - Result must exist
	                - Student must exist and be ACTIVE
	                - Exam must exist and be ACTIVE
	                - Student must belong to the exam semester
	                - Obtained marks cannot exceed maximum marks
	                - Percentage, Grade and Result Status are recalculated automatically
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Result updated successfully"
	        ),
	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid request data or business validation failed"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Result, Student or Exam not found"
	        ),
	        @ApiResponse(
	                responseCode = "409",
	                description = "Duplicate result already exists"
	        )
	})
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "RESULT",
	        description = "Update result"
	)
	@PutMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<ResultResponseDto>> updateResult(

	        @Parameter(
	                description = "Unique Result ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id,

	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Updated student examination result information",
	                required = true
	        )
	        @Valid
	        @RequestBody ResultRequestDto request

	) {

	    ResultResponseDto response =
	            resultService.updateResult(id, request);

	    return ResponseBuilder.success(
	            response,
	            "Result updated successfully",
	            HttpStatus.OK
	    );

	}





	// ==========================
	// GET RESULT BY ID
	// ==========================

	@Operation(
	        summary = "Get Result By ID",
	        description = "Retrieves an active student examination result using the unique Result ID."
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Result fetched successfully"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Result not found"
	        )
	})
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<ResultResponseDto>> getById(

	        @Parameter(
	                description = "Unique Result ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id

	) {

	    ResultResponseDto response =
	            resultService.getResultById(id);

	    return ResponseBuilder.success(
	            response,
	            "Result fetched successfully",
	            HttpStatus.OK
	    );

	}





	// ==========================
	// GET ALL RESULTS
	// ==========================

	@Operation(
	        summary = "Get All Results",
	        description = """
	                Retrieves all student examination results.

	                Features:
	                - Pagination
	                - Sorting
	                - Default ACTIVE records only
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Results fetched successfully"
	        )
	})
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponse<ResultResponseDto>>> getAll(

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

	    PageResponse<ResultResponseDto> response =
	            resultService.getAllResults(
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            );

	    return ResponseBuilder.success(
	            response,
	            "Results fetched successfully",
	            HttpStatus.OK
	    );

	}





	// ==========================
	// SEARCH RESULTS
	// ==========================

	@Operation(
	        summary = "Search Results",
	        description = """
	                Performs dynamic student result search.

	                Supported Filters:
	                - Student ID
	                - Roll Number
	                - Student Name
	                - Semester
	                - Subject
	                - Exam
	                - Result Status
	                - Grade
	                - Percentage Range
	                - Obtained Marks Range
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
	                description = "Results searched successfully"
	        )
	})
	@PostMapping("/search")
	public ResponseEntity<ApiResponseDto<PageResponse<ResultResponseDto>>> search(

	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Student result search criteria",
	                required = true
	        )
	        @Valid
	        @RequestBody ResultSearchRequest request,

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

	    PageResponse<ResultResponseDto> response =
	            resultService.searchResults(
	                    request,
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            );

	    return ResponseBuilder.success(
	            response,
	            "Results searched successfully",
	            HttpStatus.OK
	    );

	}





	// ==========================
	// DELETE RESULT
	// ==========================

	@Operation(
	        summary = "Delete Result",
	        description = """
	                Soft deletes a student examination result.

	                Business Validations:
	                - Result must exist
	                - Result must not already be deleted
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Result deleted successfully"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Result not found"
	        )
	})
	@AuditLog(
	        action = AuditAction.DELETE,
	        module = "RESULT",
	        description = "Delete result"
	)
	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<Void>> delete(

	        @Parameter(
	                description = "Unique Result ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id

	) {

	    resultService.deleteResult(id);

	    return ResponseBuilder.success(
	            null,
	            "Result deleted successfully",
	            HttpStatus.OK
	    );

	}


}