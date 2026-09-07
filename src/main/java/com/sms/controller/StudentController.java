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
import com.sms.dto.StudentRequestDto;
import com.sms.dto.StudentResponseDto;
import com.sms.dto.StudentSearchRequest;
import com.sms.payload.ApiResponseDto;
import com.sms.service.StudentService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
@Tag(
        name = "Student Management",
        description = "APIs for managing student records including create, retrieve, update, delete and dynamic search operations."
)
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {

        this.studentService = studentService;
    }

	 // ==========================
	 // CREATE STUDENT
	 // ==========================
	
	 @Operation(
	         summary = "Create Student",
	         description = """
	                 Creates a new student.
	
	                 Business Validations:
	                 - Department must exist and be ACTIVE
	                 - Village must exist
	                 - Mobile number must be unique
	                 - Admission date must be valid
	                 """
	 )
	 @ApiResponses({
	         @ApiResponse(
	                 responseCode = "201",
	                 description = "Student created successfully"
	         ),
	         @ApiResponse(
	                 responseCode = "400",
	                 description = "Invalid request data or business validation failed"
	         ),
	         @ApiResponse(
	                 responseCode = "404",
	                 description = "Department or Village not found"
	         ),
	         @ApiResponse(
	                 responseCode = "409",
	                 description = "Student with the same mobile number already exists"
	         )
	 })
	 @AuditLog(
			 action = AuditAction.CREATE,
			 module = "STUDENT",
			 description = "Create student"
			)
	 @PostMapping
	 public ResponseEntity<ApiResponseDto<StudentResponseDto>> saveStudent(
	
	         @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                 description = "Student information",
	                 required = true
	         )
	         @Valid
	         @RequestBody StudentRequestDto requestDto) {
	
	     StudentResponseDto responseDto =
	             studentService.saveStudent(requestDto);
	
	     return ResponseBuilder.success(
	             responseDto,
	             "Student Created Successfully",
	             HttpStatus.CREATED
	     );
	
	 }
    
    
    
	    
	//==========================
	//GET STUDENT BY ID
	//==========================
	
	@Operation(
	      summary = "Get Student By ID",
	      description = "Retrieves an active student using the unique student ID."
	)
	@ApiResponses({
	      @ApiResponse(
	              responseCode = "200",
	              description = "Student fetched successfully"
	      ),
	      @ApiResponse(
	              responseCode = "404",
	              description = "Student not found"
	      )
	})
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<StudentResponseDto>> getStudentById(
	
	      @Parameter(
	              description = "Unique Student ID",
	              example = "1",
	              required = true
	      )
	      @PathVariable Long id) {
	
	  StudentResponseDto responseDto =
	          studentService.getStudentById(id);
	
	  return ResponseBuilder.success(
	          responseDto,
	          "Student fetched successfully",
	          HttpStatus.OK
	  );
	
	}
    
    
    
	// ==========================
	// UPDATE STUDENT
	// ==========================

	@Operation(
	        summary = "Update Student",
	        description = """
	                Updates an existing student.

	                Business Validations:
	                - Student must exist
	                - Department must exist and be ACTIVE
	                - Village must exist
	                - Mobile number must remain unique
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Student updated successfully"
	        ),
	        @ApiResponse(
	                responseCode = "400",
	                description = "Invalid request data or business validation failed"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Student, Department or Village not found"
	        ),
	        @ApiResponse(
	                responseCode = "409",
	                description = "Student with the same mobile number already exists"
	        )
	})
	@AuditLog(
			 action = AuditAction.UPDATE,
			 module = "STUDENT",
			 description = "Update student details"
			)
	@PutMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<StudentResponseDto>> updateStudent(

	        @Parameter(
	                description = "Unique Student ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id,

	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Updated student information",
	                required = true
	        )
	        @Valid
	        @RequestBody StudentRequestDto dto) {

	    StudentResponseDto response =
	            studentService.updateStudent(id, dto);

	    return ResponseBuilder.success(
	            response,
	            "Student updated successfully",
	            HttpStatus.OK
	    );

	}
    
    
	
	
	// ==========================
	// DELETE STUDENT
	// ==========================

	@Operation(
	        summary = "Delete Student",
	        description = """
	                Soft deletes a student record.

	                Business Validations:
	                - Student must exist
	                - Student must not be already deleted
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Student deleted successfully"
	        ),
	        @ApiResponse(
	                responseCode = "404",
	                description = "Student not found"
	        )
	})
	@AuditLog(
			 action = AuditAction.DELETE,
			 module = "STUDENT",
			 description = "Delete student"
			)
	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponseDto<Void>> deleteStudent(

	        @Parameter(
	                description = "Unique Student ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id) {

	    studentService.deleteStudent(id);

	    return ResponseBuilder.success(
	            null,
	            "Student deleted successfully",
	            HttpStatus.OK
	    );

	}
	
	
	
    
	// ==========================
	// GET ALL STUDENTS
	// ==========================

	@Operation(
	        summary = "Get All Students",
	        description = """
	                Retrieves all active students.

	                Features:
	                - Pagination
	                - Sorting
	                - Default ACTIVE records only
	                """
	)
	@ApiResponses({
	        @ApiResponse(
	                responseCode = "200",
	                description = "Students fetched successfully"
	        )
	})
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponse<StudentResponseDto>>> getAllStudents(

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
	        String direction) {

	    PageResponse<StudentResponseDto> response =
	            studentService.getAllStudents(
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            );

	    return ResponseBuilder.success(
	            response,
	            "Students fetched successfully",
	            HttpStatus.OK
	    );

	}
    
   
    
	// ==========================
	// SEARCH STUDENTS
	// ==========================

	@Operation(
	        summary = "Search Students",
	        description = """
	                Performs dynamic student search using optional filters.

	                Supported Filters:
	                - Student ID
	                - Roll Number
	                - First Name
	                - Last Name
	                - Mobile Number
	                - Department ID
	                - Gender
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
	                description = "Student search completed successfully"
	        )
	})
	@PostMapping("/search")
	public ResponseEntity<ApiResponseDto<PageResponse<StudentResponseDto>>> searchStudents(

	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Student search criteria",
	                required = true
	        )
	        @RequestBody StudentSearchRequest request,

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
	        String direction) {

	    PageResponse<StudentResponseDto> response =
	            studentService.searchStudents(
	                    request,
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            );

	    return ResponseBuilder.success(
	            response,
	            "Students searched successfully",
	            HttpStatus.OK
	    );

	}
    
    
    

}