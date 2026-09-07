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

import com.sms.dto.DepartmentRequestDto;
import com.sms.dto.DepartmentResponseDto;
import com.sms.dto.DepartmentSearchRequest;
import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.service.DepartmentService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
	    name = "Department Management",
	    description = "APIs for managing departments"
	)
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	
	@Operation(
	        summary = "Create Department",
	        description = "Creates a new department."
	)
	@ApiResponses({
	        @ApiResponse(responseCode = "201", description = "Department created successfully"),
	        @ApiResponse(responseCode = "400", description = "Validation failed"),
	        @ApiResponse(responseCode = "409", description = "Department already exists")
	})
	@AuditLog(
	        action = AuditAction.CREATE,
	        module = "DEPARTMENT",
	        description = "Create department"
	)
	@PostMapping
	public ResponseEntity<ApiResponseDto<DepartmentResponseDto>> createDepartment(
	        @Valid @RequestBody DepartmentRequestDto dto) {

	    DepartmentResponseDto response =
	            departmentService.createDepartment(dto);

	    return ResponseBuilder.success(
	            response,
	            "Department Created Successfully",
	            HttpStatus.CREATED
	    );
	}
	
	
	
	@Operation(
	        summary = "Get Department By Id",
	        description = "Fetch department details using department id."
	)
	@ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Department fetched successfully"),
	        @ApiResponse(responseCode = "404", description = "Department not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<DepartmentResponseDto>> getDepartmentById(
	        @PathVariable Long id) {

	    DepartmentResponseDto response =
	            departmentService.getDepartmentById(id);

	    return ResponseBuilder.success(
	            response,
	            "Department fetched successfully",
	            HttpStatus.OK
	    );
	}

	
	@Operation(
	        summary = "Get All Departments",
	        description = "Returns paginated department list."
	)
	@ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Departments fetched successfully")
	})
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponse<DepartmentResponseDto>>> getAllDepartments(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    PageResponse<DepartmentResponseDto> response =
	            departmentService.getAllDepartments(page, size, sortBy, direction);

	    return ResponseBuilder.success(
	            response,
	            "Departments fetched successfully",
	            HttpStatus.OK
	    );
	}
	
	
	@Operation(
	        summary = "Update Department",
	        description = "Updates an existing department."
	)
	@ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Department updated successfully"),
	        @ApiResponse(responseCode = "404", description = "Department not found"),
	        @ApiResponse(responseCode = "409", description = "Department already exists")
	})
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "DEPARTMENT",
	        description = "Update department"
	)
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDto<DepartmentResponseDto>> updateDepartment(
	        @PathVariable Long id,
	        @Valid @RequestBody DepartmentRequestDto dto) {

	    DepartmentResponseDto response =
	            departmentService.updateDepartment(id, dto);

	    return ResponseBuilder.success(
	            response,
	            "Department updated successfully",
	            HttpStatus.OK
	    );
	}
	
	
	@Operation(
	        summary = "Delete Department",
	        description = "Soft deletes a department."
	)
	@ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Department deleted successfully"),
	        @ApiResponse(responseCode = "404", description = "Department not found"),
	        @ApiResponse(responseCode = "400", description = "Department cannot be deleted")
	})
	@AuditLog(
	        action = AuditAction.DELETE,
	        module = "DEPARTMENT",
	        description = "Delete department"
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto<String>> deleteDepartment(@PathVariable Long id) {

	    departmentService.deleteDepartment(id);

	    return ResponseBuilder.success(null,
	            "Department deleted successfully",
	            HttpStatus.OK
	    );
	}
	
	
	@Operation(
	        summary = "Search Departments",
	        description = "Search departments using dynamic filters."
	)
	@ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Departments searched successfully")
	})
	@PostMapping("/search")
	public ResponseEntity<ApiResponseDto<PageResponse<DepartmentResponseDto>>> searchDepartments(
			@RequestBody DepartmentSearchRequest request, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		PageResponse<DepartmentResponseDto> response = 
				departmentService.searchDepartments(request, page, size, 
						sortBy, direction);

		return ResponseBuilder.success(response, "Departments search result", HttpStatus.OK);
	}
	
	
	
	
}