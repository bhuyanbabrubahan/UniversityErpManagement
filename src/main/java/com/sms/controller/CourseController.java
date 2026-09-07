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

import com.sms.dto.CourseRequestDto;
import com.sms.dto.CourseResponseDto;
import com.sms.dto.CourseSearchRequest;
import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.service.CourseService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(
	    name = "Course Management",
	    description = "APIs for managing university courses"
	)
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    
    
    @Operation(
            summary = "Create Course",
            description = "Creates a new course under a department."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "409", description = "Course code already exists")
    })
    @AuditLog(
            action = AuditAction.CREATE,
            module = "COURSE",
            description = "Create course"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> createCourse(
            @Valid @RequestBody CourseRequestDto dto) {

        CourseResponseDto response = courseService.createCourse(dto);

        return ResponseBuilder.success(
                response,
                "Course created successfully",
                HttpStatus.CREATED
        );
    }
    
    
    
    
    
    
    @Operation(
            summary = "Get Course By Id",
            description = "Fetch course details using course id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> getCourseById(
            @PathVariable Long id) {

        CourseResponseDto response = courseService.getCourseById(id);

        return ResponseBuilder.success(
                response,
                "Course fetched successfully",
                HttpStatus.OK
        );
    }
    
    
    
    
    
    @Operation(
            summary = "Update Course",
            description = "Updates an existing course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Course code already exists")
    })
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "COURSE",
            description = "Update course"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseResponseDto>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDto dto) {

        CourseResponseDto response = courseService.updateCourse(id, dto);

        return ResponseBuilder.success(
                response,
                "Course updated successfully",
                HttpStatus.OK
        );
    }
    
    
    
    
    
    
    @Operation(
            summary = "Delete Course",
            description = "Soft deletes a course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "400", description = "Course cannot be deleted")
    })
    @AuditLog(
            action = AuditAction.DELETE,
            module = "COURSE",
            description = "Delete course"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseBuilder.success(
                null,
                "Course deleted successfully",
                HttpStatus.OK
        );
    }
    
    
    
    
    
    @Operation(
            summary = "Get All Courses",
            description = "Returns paginated list of courses."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses fetched successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<PageResponse<CourseResponseDto>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        PageResponse<CourseResponseDto> response =
                courseService.getAllCourses(page, size, sortBy, direction);

        return ResponseBuilder.success(
                response,
                "Courses fetched successfully",
                HttpStatus.OK
        );
    }
    
    
    
    
    
    @Operation(
            summary = "Search Courses",
            description = "Search courses using dynamic filters."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses searched successfully")
    })
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponse<CourseResponseDto>>> searchCourses(
            @RequestBody CourseSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        PageResponse<CourseResponseDto> response =
                courseService.searchCourses(request, page, size, sortBy, direction);

        return ResponseBuilder.success(
                response,
                "Courses fetched successfully",
                HttpStatus.OK
        );
    }
    
    
}