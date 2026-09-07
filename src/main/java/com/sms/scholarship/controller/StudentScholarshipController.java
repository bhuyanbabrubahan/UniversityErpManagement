package com.sms.scholarship.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.scholarship.dto.ScholarshipRejectRequestDto;
import com.sms.scholarship.dto.StudentScholarshipRequestDto;
import com.sms.scholarship.dto.StudentScholarshipResponseDto;
import com.sms.scholarship.dto.StudentScholarshipSearchRequest;
import com.sms.scholarship.service.StudentScholarshipService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/scholarships")
@RequiredArgsConstructor
@Tag(
        name = "Student Scholarship",
        description = "Student Scholarship Management APIs"
)
public class StudentScholarshipController {


    private final StudentScholarshipService scholarshipService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    @Operation(
            summary = "Create Scholarship",
            description = "Create a new student scholarship record"
    )
    public ResponseEntity<ApiResponseDto<StudentScholarshipResponseDto>> create(

            @Valid
            @RequestBody
            StudentScholarshipRequestDto dto) {


        return ResponseBuilder.success(
                scholarshipService.createScholarship(dto),
                "Scholarship created successfully",
                HttpStatus.CREATED
        );
    }



    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Scholarship By Id",
            description = "Fetch scholarship details using scholarship id"
    )
    public ResponseEntity<ApiResponseDto<StudentScholarshipResponseDto>> getById(

            @Parameter(
                    description = "Scholarship Id",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {


        return ResponseBuilder.success(
                scholarshipService.getScholarshipById(id),
                "Scholarship fetched successfully",
                HttpStatus.OK
        );
    }



    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Scholarship",
            description = "Update existing scholarship details"
    )
    public ResponseEntity<ApiResponseDto<StudentScholarshipResponseDto>> update(

            @Parameter(
                    description = "Scholarship Id",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,


            @Valid
            @RequestBody
            StudentScholarshipRequestDto dto) {


        return ResponseBuilder.success(
                scholarshipService.updateScholarship(id, dto),
                "Scholarship updated successfully",
                HttpStatus.OK
        );
    }



    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Scholarship",
            description = "Soft delete scholarship using scholarship id"
    )
    public ResponseEntity<ApiResponseDto<Void>> delete(

            @Parameter(
                    description = "Scholarship Id",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {


        scholarshipService.deleteScholarship(id);


        return ResponseBuilder.success(
                null,
                "Scholarship deleted successfully",
                HttpStatus.OK
        );
    }



    // =====================================================
    // GET ALL PAGINATION
    // =====================================================

    @GetMapping
    @Operation(
            summary = "Get All Scholarships",
            description = "Fetch paginated scholarship list with sorting"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentScholarshipResponseDto>>> getAll(

            @Parameter(
                    description = "Page number (zero based index)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,


            @Parameter(
                    description = "Records per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,


            @Parameter(
                    description = "Sorting field",
                    example = "id"
            )
            @RequestParam(defaultValue = "id")
            String sortBy,


            @Parameter(
                    description = "Sorting direction (asc or desc)",
                    example = "desc"
            )
            @RequestParam(defaultValue = "desc")
            String direction) {


        return ResponseBuilder.success(
                scholarshipService.getAllScholarships(
                        page,
                        size,
                        sortBy,
                        direction
                ),
                "Scholarship list fetched successfully",
                HttpStatus.OK
        );
    }



    // =====================================================
    // SEARCH
    // =====================================================

    @PostMapping("/search")
    @Operation(
            summary = "Search Scholarships",
            description = "Search scholarships dynamically with pagination and sorting"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentScholarshipResponseDto>>> search(

            @RequestBody
            StudentScholarshipSearchRequest request,


            @Parameter(
                    description = "Page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,


            @Parameter(
                    description = "Records per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,


            @Parameter(
                    description = "Sorting field",
                    example = "id"
            )
            @RequestParam(defaultValue = "id")
            String sortBy,


            @Parameter(
                    description = "Sorting direction",
                    example = "desc"
            )
            @RequestParam(defaultValue = "desc")
            String direction) {


        return ResponseBuilder.success(
                scholarshipService.searchScholarships(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                ),
                "Scholarship search completed successfully",
                HttpStatus.OK
        );
    }
    
    
 // =====================================================
 // APPROVE SCHOLARSHIP
 // =====================================================

 @PatchMapping("/{id}/approve")
 @Operation(
         summary = "Approve Scholarship",
         description = "Approve pending scholarship"
 )
 public ResponseEntity<ApiResponseDto<StudentScholarshipResponseDto>> approve(

         @Parameter(
                 description = "Scholarship Id",
                 example = "1",
                 required = true
         )
         @PathVariable Long id) {

     return ResponseBuilder.success(
             scholarshipService.approveScholarship(id),
             "Scholarship approved successfully",
             HttpStatus.OK
     );
 }
 
 
 
 
 
 
//=====================================================
//REJECT SCHOLARSHIP
//=====================================================

@PatchMapping("/{id}/reject")
@Operation(
      summary = "Reject Scholarship",
      description = "Reject pending scholarship"
)
public ResponseEntity<ApiResponseDto<StudentScholarshipResponseDto>> reject(

      @Parameter(
              description = "Scholarship Id",
              example = "1",
              required = true
      )
      @PathVariable Long id,

      @Valid
      @RequestBody
      ScholarshipRejectRequestDto dto) {

  return ResponseBuilder.success(
          scholarshipService.rejectScholarship(
                  id,
                  dto.getReason()
          ),
          "Scholarship rejected successfully",
          HttpStatus.OK
  );
}

}