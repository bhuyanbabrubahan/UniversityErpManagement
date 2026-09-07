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
import com.sms.dto.SubjectRequestDto;
import com.sms.dto.SubjectResponseDto;
import com.sms.dto.SubjectSearchRequest;
import com.sms.payload.ApiResponseDto;
import com.sms.service.SubjectService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
	    name = "Subject Management",
	    description = "APIs for managing university subjects"
	)
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService service;

    // ==========================
    // CREATE SUBJECT
    // ==========================

    @Operation(
    	    summary = "Create Subject",
    	    description = "Creates a new subject."
    	)
    @ApiResponses({

    	@ApiResponse(
    	responseCode="201",
    	description="Subject created successfully"
    	),

    	@ApiResponse(
    	responseCode="400",
    	description="Validation failed"
    	),

    	@ApiResponse(
    	responseCode="404",
    	description="Semester not found"
    	),

    	@ApiResponse(
    	responseCode="409",
    	description="Duplicate subject"
    	)

    	})
    @AuditLog(
            action = AuditAction.CREATE,
            module = "SUBJECT",
            description = "Create subject"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<SubjectResponseDto>> createSubject(
            @Valid @RequestBody SubjectRequestDto dto) {

        SubjectResponseDto response =
                service.createSubject(dto);

        return ResponseBuilder.success(
                response,
                "Subject created successfully",
                HttpStatus.CREATED
        );
    }

    
    
    
    // ==========================
    // GET BY ID
    // ==========================
    @Operation(
            summary = "Get Subject By Id",
            description = "Returns subject details by subject ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subject fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subject not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SubjectResponseDto>> getSubjectById(

            @Parameter(description = "Subject ID", example = "1")
            @PathVariable Long id
    ) {

        SubjectResponseDto response =
                service.getSubjectById(id);

        return ResponseBuilder.success(
                response,
                "Subject fetched successfully",
                HttpStatus.OK
        );
    }

    
    
    
    // ==========================
    // GET ALL
    // ==========================
    @Operation(
            summary = "Get All Subjects",
            description = "Returns a paginated list of all subjects."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subjects fetched successfully"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<PageResponse<SubjectResponseDto>>> getAllSubjects(

            @Parameter(description = "Page number (starts from 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direction

    ) {

        PageResponse<SubjectResponseDto> response =
                service.getAllSubjects(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseBuilder.success(
                response,
                "Subjects fetched successfully",
                HttpStatus.OK
        );
    }
    
    

    // ==========================
    // UPDATE
    // ==========================
    @Operation(
            summary = "Update Subject",
            description = "Updates an existing subject."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subject updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subject not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate subject"
            )
    })
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "SUBJECT",
            description = "Update subject"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SubjectResponseDto>> updateSubject(

            @Parameter(description = "Subject ID", example = "1")
            @PathVariable Long id,

            @Valid
            @RequestBody SubjectRequestDto dto
    ) {

        SubjectResponseDto response =
                service.updateSubject(
                        id,
                        dto
                );

        return ResponseBuilder.success(
                response,
                "Subject updated successfully",
                HttpStatus.OK
        );
    }
    
    
    

    // ==========================
    // DELETE
    // ==========================
    @Operation(
            summary = "Delete Subject",
            description = "Soft deletes a subject."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subject deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subject not found"
            )
    })
    @AuditLog(
            action = AuditAction.DELETE,
            module = "SUBJECT",
            description = "Delete subject"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteSubject(

            @Parameter(description = "Subject ID", example = "1")
            @PathVariable Long id
    ) {

        service.deleteSubject(id);

        return ResponseBuilder.success(
                null,
                "Subject deleted successfully",
                HttpStatus.OK
        );
    }
    
    
    
    

    // ==========================
    // SEARCH
    // ==========================
    @Operation(
            summary = "Search Subjects",
            description = "Search subjects using different filters with pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subjects searched successfully"
            )
    })
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponse<SubjectResponseDto>>> searchSubjects(

            @RequestBody SubjectSearchRequest request,

            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direction

    ) {

        PageResponse<SubjectResponseDto> response =
                service.searchSubjects(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseBuilder.success(
                response,
                "Subjects searched successfully",
                HttpStatus.OK
        );
    }

}