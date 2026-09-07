package com.sms.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.security.dto.SuperAdminRequestDto;
import com.sms.security.dto.SuperAdminResponseDto;
import com.sms.security.dto.SuperAdminSearchRequestDto;
import com.sms.security.service.SuperAdminService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/super-admins")
@Validated
@Tag(
        name = "Super Admin Management",
        description = "APIs for managing super administrators"
)
public class SuperAdminController {


    private final SuperAdminService superAdminService;


    // =====================================================
    // Create Super Admin
    // =====================================================

    @PostMapping
    @Operation(
            summary = "Create Super Admin",
            description = "Create a new super administrator account"
    )
    public ResponseEntity<ApiResponseDto<SuperAdminResponseDto>> createSuperAdmin(

            @Valid
            @RequestBody
            SuperAdminRequestDto request

    ) {

        return ResponseBuilder.success(

                superAdminService.createSuperAdmin(request),

                "Super Admin created successfully",

                HttpStatus.CREATED
        );

    }



    // =====================================================
    // Get Super Admin By Id
    // =====================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Super Admin By Id",
            description = "Fetch super administrator details using id"
    )
    public ResponseEntity<ApiResponseDto<SuperAdminResponseDto>> getSuperAdminById(

            @Parameter(
                    description = "Super Admin Id",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id

    ) {

        return ResponseBuilder.success(

                superAdminService.getSuperAdminById(id),

                "Super Admin fetched successfully",

                HttpStatus.OK
        );

    }



    // =====================================================
    // Get All Super Admins
    // =====================================================

    @GetMapping
    @Operation(
            summary = "Get All Super Admins",
            description = "Fetch paginated list of super administrators"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<SuperAdminResponseDto>>> getAllSuperAdmins(

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

                superAdminService.getAllSuperAdmins(
                        page,
                        size,
                        sortBy,
                        direction
                ),

                "Super Admin list fetched successfully",

                HttpStatus.OK
        );

    }



    // =====================================================
    // Search Super Admins
    // =====================================================

    @PostMapping("/search")
    @Operation(
            summary = "Search Super Admins",
            description = "Search super administrators with filters"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<SuperAdminResponseDto>>> searchSuperAdmins(

            @RequestBody
            SuperAdminSearchRequestDto request,

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

                superAdminService.searchSuperAdmins(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                ),

                "Super Admin search completed successfully",

                HttpStatus.OK
        );

    }



    // =====================================================
    // Update Super Admin
    // =====================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Super Admin",
            description = "Update super administrator details"
    )
    public ResponseEntity<ApiResponseDto<SuperAdminResponseDto>> updateSuperAdmin(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            SuperAdminRequestDto request

    ) {

        return ResponseBuilder.success(

                superAdminService.updateSuperAdmin(
                        id,
                        request
                ),

                "Super Admin updated successfully",

                HttpStatus.OK
        );

    }



    // =====================================================
    // Delete Super Admin
    // =====================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Super Admin",
            description = "Soft delete super administrator"
    )
    public ResponseEntity<ApiResponseDto<Void>> deleteSuperAdmin(

            @PathVariable
            Long id

    ) {

        superAdminService.deleteSuperAdmin(id);

        return ResponseBuilder.success(

                null,

                "Super Admin deleted successfully",

                HttpStatus.OK
        );

    }

}