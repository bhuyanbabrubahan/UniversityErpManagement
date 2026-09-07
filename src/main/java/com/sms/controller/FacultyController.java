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

import com.sms.dto.FacultyRequestDto;
import com.sms.dto.FacultyResponseDto;
import com.sms.dto.FacultySearchRequest;
import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.service.FacultyService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
	    name = "Faculty Management",
	    description = "APIs for managing faculty members including create, update, retrieve, delete and search operations."
	)
@RestController
@RequestMapping("/api/faculties")
public class FacultyController {


    private final FacultyService facultyService;


    public FacultyController(
            FacultyService facultyService
    ) {

        this.facultyService = facultyService;

    }



    // ===============================
    // CREATE FACULTY
    // ===============================
    @Operation(
    	    summary = "Create Faculty",
    	    description = "Creates a new faculty member along with user account and address details."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "201", description = "Faculty created successfully"),
    	    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    	    @ApiResponse(responseCode = "404", description = "Department or Address reference not found"),
    	    @ApiResponse(responseCode = "409", description = "Email, Phone Number or Employee Code already exists")
    	})
    @AuditLog(
            action = AuditAction.CREATE,
            module = "FACULTY",
            description = "Create faculty"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<FacultyResponseDto>> createFaculty(
            @Valid @RequestBody FacultyRequestDto dto
    ) {


        FacultyResponseDto response =
                facultyService.createFaculty(dto);


        return ResponseBuilder.success(
                response,
                "Faculty Created Successfully",
                HttpStatus.CREATED
        );

    }





    // ===============================
    // GET FACULTY BY ID
    // Only Numeric ID Allowed
    // ===============================

    @Operation(
    	    summary = "Get Faculty By ID",
    	    description = "Fetches a faculty record using its unique ID."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty fetched successfully"),
    	    @ApiResponse(responseCode = "404", description = "Faculty not found")
    	})
    	@GetMapping("/{id:\\d+}")
    	public ResponseEntity<ApiResponseDto<FacultyResponseDto>> getFacultyById(

    	        @Parameter(
    	                description = "Unique Faculty ID",
    	                example = "1",
    	                required = true
    	        )
    	        @PathVariable Long id
    	) {


        FacultyResponseDto response =
                facultyService.getFacultyById(id);


        return ResponseBuilder.success(
                response,
                "Faculty fetched successfully",
                HttpStatus.OK
        );

    }





    // ===============================
    // GET ALL FACULTIES
    // Pagination + Sorting
    // ===============================

    @Operation(
    	    summary = "Get All Faculties",
    	    description = "Returns all faculty records with pagination and sorting."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty list fetched successfully")
    	})
    @GetMapping
    public ResponseEntity<ApiResponseDto<PageResponse<FacultyResponseDto>>> getAllFaculties(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction

    ) {

        PageResponse<FacultyResponseDto> response =
            facultyService.getAllFaculties(
                page,
                size,
                sortBy,
                direction
            );

        return ResponseBuilder.success(
            response,
            "Faculties fetched successfully",
            HttpStatus.OK
        );
    }







    // ===============================
    // UPDATE FACULTY
    // ===============================


    @Operation(
    	    summary = "Update Faculty",
    	    description = "Updates an existing faculty record using the faculty ID."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty updated successfully"),
    	    @ApiResponse(responseCode = "400", description = "Invalid request"),
    	    @ApiResponse(responseCode = "404", description = "Faculty not found"),
    	    @ApiResponse(responseCode = "409", description = "Duplicate email or phone number")
    	})
	    @AuditLog(
	            action = AuditAction.UPDATE,
	            module = "FACULTY",
	            description = "Update faculty"
	    )
    	@PutMapping("/{id:\\d+}")
    	public ResponseEntity<ApiResponseDto<FacultyResponseDto>> updateFaculty(

    	        @Parameter(
    	                description = "Faculty ID",
    	                example = "1",
    	                required = true
    	        )
    	        @PathVariable Long id,

    	        @Valid
    	        @RequestBody FacultyRequestDto dto

    	) {



        FacultyResponseDto response =

                facultyService.updateFaculty(
                        id,
                        dto
                );



        return ResponseBuilder.success(
                response,
                "Faculty updated successfully",
                HttpStatus.OK
        );

    }







    // ===============================
    // DELETE FACULTY
    // Soft Delete
    // ===============================


    @Operation(
    	    summary = "Delete Faculty",
    	    description = "Soft deletes a faculty record."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty deleted successfully"),
    	    @ApiResponse(responseCode = "404", description = "Faculty not found")
    	})
	    @AuditLog(
	            action = AuditAction.DELETE,
	            module = "FACULTY",
	            description = "Delete faculty"
	    )
    	@DeleteMapping("/{id:\\d+}")
    	public ResponseEntity<ApiResponseDto<Void>> deleteFaculty(

    	        @Parameter(
    	                description = "Faculty ID",
    	                example = "1",
    	                required = true
    	        )
    	        @PathVariable Long id

    	) {


        facultyService.deleteFaculty(id);



        return ResponseBuilder.success(
                null,
                "Faculty deleted successfully",
                HttpStatus.OK
        );

    }







    // ===============================
    // SEARCH FACULTY
    // Dynamic Search
    // ===============================


    @Operation(
    	    summary = "Search Faculties",
    	    description = """
    	            Performs dynamic faculty search using optional filters.
    	            
    	            Supports:
    	            - Faculty ID
    	            - Employee Code
    	            - First Name
    	            - Last Name
    	            - Department
    	            - Designation
    	            - Qualification
    	            - Record Status
    	            
    	            Also supports Pagination and Sorting.
    	            """
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Faculty search completed successfully")
    	})
    	@PostMapping("/search")
    	public ResponseEntity<ApiResponseDto<PageResponse<FacultyResponseDto>>> searchFaculties(

    	        @RequestBody FacultySearchRequest request,

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
    	        String direction

    	) {



        PageResponse<FacultyResponseDto> response =

                facultyService.searchFaculties(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );



        return ResponseBuilder.success(
                response,
                "Faculty search result",
                HttpStatus.OK
        );

    }


}