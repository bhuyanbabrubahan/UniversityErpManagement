package com.sms.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.security.dto.AdminRequestDto;
import com.sms.security.dto.AdminResponseDto;
import com.sms.security.dto.AdminSearchRequestDto;
import com.sms.security.service.AdminService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
@Validated
@Tag(
        name = "Admin Management",
        description = "APIs for managing administrators"
)
public class AdminController {

    private final AdminService adminService;

    // =====================================================
    // Create Admin
    // =====================================================

    @PostMapping
    @Operation(
            summary = "Create Admin",
            description = "Create a new administrator account"
    )
    public ResponseEntity<ApiResponseDto<AdminResponseDto>> createAdmin(

            @Valid
            @RequestBody
            AdminRequestDto request

    ) {

        return ResponseBuilder.success(

                adminService.createAdmin(request),

                "Admin created successfully",

                HttpStatus.CREATED
        );

    }

    // =====================================================
    // Get Admin By Id
    // =====================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Admin By Id",
            description = "Fetch administrator details using id"
    )
    public ResponseEntity<ApiResponseDto<AdminResponseDto>> getAdminById(

            @Parameter(
                    description = "Admin Id",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id

    ) {

        return ResponseBuilder.success(

                adminService.getAdminById(id),

                "Admin fetched successfully",

                HttpStatus.OK
        );

    }

    // =====================================================
    // Get All Admins
    // =====================================================

    @GetMapping
    @Operation(
            summary = "Get All Admins",
            description = "Fetch paginated list of administrators"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<AdminResponseDto>>> getAllAdmins(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction

    ) {

        return ResponseBuilder.success(

                adminService.getAllAdmins(
                        page,
                        size,
                        sortBy,
                        direction
                ),

                "Admin list fetched successfully",

                HttpStatus.OK
        );

    }

    // =====================================================
    // Search Admins
    // =====================================================

    @PostMapping("/search")
    @Operation(
            summary = "Search Admins",
            description = "Search administrators with filters"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<AdminResponseDto>>> searchAdmins(

            @RequestBody
            AdminSearchRequestDto request,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction

    ) {

        return ResponseBuilder.success(

                adminService.searchAdmins(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                ),

                "Admin search completed successfully",

                HttpStatus.OK
        );

    }

    // =====================================================
    // Update Admin
    // =====================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Admin",
            description = "Update administrator details"
    )
    public ResponseEntity<ApiResponseDto<AdminResponseDto>> updateAdmin(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            AdminRequestDto request

    ) {

        return ResponseBuilder.success(

                adminService.updateAdmin(
                        id,
                        request
                ),

                "Admin updated successfully",

                HttpStatus.OK
        );

    }

    // =====================================================
    // Delete Admin
    // =====================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Admin",
            description = "Soft delete administrator"
    )
    public ResponseEntity<ApiResponseDto<Void>> deleteAdmin(

            @PathVariable
            Long id

    ) {

        adminService.deleteAdmin(id);

        return ResponseBuilder.success(

                null,

                "Admin deleted successfully",

                HttpStatus.OK
        );

    }

}