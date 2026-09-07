package com.sms.controller;

import java.util.List;

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

import com.sms.dto.FacultySubjectRequestDto;
import com.sms.dto.FacultySubjectResponseDto;
import com.sms.dto.FacultySubjectSearchRequest;
import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.service.FacultySubjectService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Tag(
	    name = "Faculty Subject Management",
	    description = "APIs for assigning subjects to faculty members, updating assignments, retrieving records, deleting assignments and performing dynamic searches."
	)
@RestController
@RequestMapping("/api/faculty-subjects")
@RequiredArgsConstructor
public class FacultySubjectController {

    private final FacultySubjectService service;

    // ==========================
    // ASSIGN SUBJECT
    // ==========================
    @Operation(
    	    summary = "Assign Subject to Faculty",
    	    description = "Assigns a subject to a faculty member for a specific academic year."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "201", description = "Subject assigned successfully"),
    	    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    	    @ApiResponse(responseCode = "404", description = "Faculty or Subject not found"),
    	    @ApiResponse(responseCode = "409", description = "Faculty subject assignment already exists")
    	})
	    @AuditLog(
	            action = AuditAction.CREATE,
	            module = "FACULTY_SUBJECT",
	            description = "Create faculty subject mapping"
	    )
    	@PostMapping
    	public ResponseEntity<ApiResponseDto<FacultySubjectResponseDto>> assignSubject(

    	        @Valid
    	        @RequestBody
    	        FacultySubjectRequestDto dto) {

        FacultySubjectResponseDto response =
                service.assignSubject(dto);

        return ResponseBuilder.success(
                response,
                "Subject assigned successfully.",
                HttpStatus.CREATED
        );
    }

    // ==========================
    // GET BY ID
    // ==========================
    @Operation(
    	    summary = "Get Faculty Subject Mapping By ID",
    	    description = "Fetches a faculty-subject assignment using its unique ID."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty subject fetched successfully"),
    	    @ApiResponse(responseCode = "404", description = "Faculty subject mapping not found")
    	})
    	@GetMapping("/{id}")
    	public ResponseEntity<ApiResponseDto<FacultySubjectResponseDto>> getById(

    	        @Parameter(
    	                description = "Faculty Subject Mapping ID",
    	                example = "1",
    	                required = true
    	        )
    	        @PathVariable Long id) {

        FacultySubjectResponseDto response =
                service.getById(id);

        return ResponseBuilder.success(
                response,
                "Faculty subject fetched successfully.",
                HttpStatus.OK
        );
    }

    // ==========================
    // GET ALL
    // ==========================
    @Operation(
    	    summary = "Get All Faculty Subject Assignments",
    	    description = "Returns all faculty-subject assignments with pagination and sorting."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty subject list fetched successfully")
    	})
    	@GetMapping
    	public ResponseEntity<ApiResponseDto<PageResponse<FacultySubjectResponseDto>>> getAll(

    	        @Parameter(description = "Page Number", example = "0")
    	        @RequestParam(defaultValue = "0")
    	        int page,

    	        @Parameter(description = "Page Size", example = "10")
    	        @RequestParam(defaultValue = "10")
    	        int size,

    	        @Parameter(description = "Sort Field", example = "id")
    	        @RequestParam(defaultValue = "id")
    	        String sortBy,

    	        @Parameter(description = "Sort Direction", example = "asc")
    	        @RequestParam(defaultValue = "asc")
    	        String direction) {

        return ResponseBuilder.success(
                service.getAll(page, size, sortBy, direction),
                "Faculty subject list fetched successfully.",
                HttpStatus.OK
        );
    }

    // ==========================
    // UPDATE
    // ==========================
    @Operation(
    	    summary = "Update Faculty Subject Assignment",
    	    description = "Updates an existing faculty-subject assignment."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty subject updated successfully"),
    	    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    	    @ApiResponse(responseCode = "404", description = "Faculty subject mapping not found"),
    	    @ApiResponse(responseCode = "409", description = "Duplicate faculty subject assignment")
    	})
	    @AuditLog(
	            action = AuditAction.UPDATE,
	            module = "FACULTY_SUBJECT",
	            description = "Update faculty subject mapping"
	    )
    	@PutMapping("/{id}")
    	public ResponseEntity<ApiResponseDto<FacultySubjectResponseDto>> update(

    	        @Parameter(
    	                description = "Faculty Subject Mapping ID",
    	                example = "1",
    	                required = true
    	        )
    	        @PathVariable Long id,

    	        @Valid
    	        @RequestBody FacultySubjectRequestDto dto) {

        return ResponseBuilder.success(
                service.update(id, dto),
                "Faculty subject updated successfully.",
                HttpStatus.OK
        );
    }

    // ==========================
    // DELETE (SOFT DELETE)
    // ==========================
    @Operation(
    	    summary = "Delete Faculty Subject Assignment",
    	    description = "Soft deletes a faculty-subject assignment."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty subject deleted successfully"),
    	    @ApiResponse(responseCode = "404", description = "Faculty subject mapping not found")
    	})
	    @AuditLog(
	            action = AuditAction.DELETE,
	            module = "FACULTY_SUBJECT",
	            description = "Delete faculty subject mapping"
	    )    	
    	@DeleteMapping("/{id}")
    	public ResponseEntity<ApiResponseDto<Void>> delete(

    	        @Parameter(
    	                description = "Faculty Subject Mapping ID",
    	                example = "1",
    	                required = true
    	        )
    	        @PathVariable Long id) {

        service.delete(id);

        return ResponseBuilder.success(
                null,
                "Faculty subject deleted successfully.",
                HttpStatus.OK
        );
    }

    // ==========================
    // SEARCH
    // ==========================
    @Operation(
    	    summary = "Search Faculty Subject Assignments",
    	    description = """
    	            Performs dynamic search using optional filters.

    	            Supported Filters:
    	            - Faculty ID
    	            - Subject ID
    	            - Academic Year
    	            - Record Status

    	            Supports Pagination and Sorting.
    	            """
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty subject search completed successfully")
    	})
    	@PostMapping("/search")
    	public ResponseEntity<ApiResponseDto<PageResponse<FacultySubjectResponseDto>>> search(

    	        @RequestBody FacultySubjectSearchRequest request,

    	        @Parameter(description = "Page Number", example = "0")
    	        @RequestParam(defaultValue = "0")
    	        int page,

    	        @Parameter(description = "Page Size", example = "10")
    	        @RequestParam(defaultValue = "10")
    	        int size,

    	        @Parameter(description = "Sort Field", example = "id")
    	        @RequestParam(defaultValue = "id")
    	        String sortBy,

    	        @Parameter(description = "Sort Direction", example = "asc")
    	        @RequestParam(defaultValue = "asc")
    	        String direction) {

        return ResponseBuilder.success(
                service.search(request, page, size, sortBy, direction),
                "Faculty subject search completed successfully.",
                HttpStatus.OK
        );
    }
    
    
    @GetMapping("/my-subjects")
    @Operation(
            summary = "Get My Assigned Subjects",
            description = "Returns only active faculty-subject mappings assigned to the currently authenticated faculty."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Assigned subjects fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Academic year is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Faculty profile or assigned subjects not found"
            )
    })
    public ResponseEntity<ApiResponseDto<List<FacultySubjectResponseDto>>> getMySubjects(

            @Parameter(
                    description = "Academic Year",
                    example = "2026-2027",
                    required = true
            )
            @RequestParam String academicYear) {

        return ResponseBuilder.success(
                service.getMySubjects(academicYear),
                "My assigned subjects fetched successfully.",
                HttpStatus.OK
        );
    }


    
    @GetMapping("/faculty/{facultyId}/subjects")
    public ResponseEntity<ApiResponseDto<List<FacultySubjectResponseDto>>> getSubjectsByFacultyAndAcademicYear(

            @Parameter(
                    description = "Faculty ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long facultyId,

            @Parameter(
                    description = "Academic Year",
                    example = "2026-2027",
                    required = true
            )
            @RequestParam String academicYear) {

        return ResponseBuilder.success(
                service.getSubjectsByFacultyAndAcademicYear(
                        facultyId,
                        academicYear
                ),
                "Faculty subjects fetched successfully.",
                HttpStatus.OK
        );
    }

    
    
    

}