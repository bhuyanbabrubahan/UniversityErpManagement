package com.sms.erp.hod.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.erp.hod.dto.HodRequestDto;
import com.sms.erp.hod.dto.HodResponseDto;
import com.sms.erp.hod.dto.HodSearchDto;
import com.sms.erp.hod.service.HodService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/erp/hod")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','VC','REGISTRAR','DEAN','HOD')")
public class HodController {

    private final HodService service;

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<
            ApiResponseDto<Page<HodResponseDto>>> all(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<HodResponseDto> response =
                service.findAll(
                        page,
                        size
                );

        return ResponseBuilder.success(
                response,
                "HODs fetched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @PostMapping("/search")
    public ResponseEntity<
            ApiResponseDto<Page<HodResponseDto>>> search(

            @RequestBody(
                    required = false
            )
            HodSearchDto search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<HodResponseDto> response =
                service.search(
                        search,
                        page,
                        size
                );

        return ResponseBuilder.success(
                response,
                "HODs searched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<HodResponseDto>> get(
            @PathVariable Long id) {

        HodResponseDto response =
                service.findById(id);

        return ResponseBuilder.success(
                response,
                "HOD fetched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<
            ApiResponseDto<HodResponseDto>> create(
            @RequestBody HodRequestDto request) {

        HodResponseDto response =
                service.create(request);

        return ResponseBuilder.success(
                response,
                "HOD created successfully",
                HttpStatus.CREATED
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<HodResponseDto>> update(

            @PathVariable Long id,

            @RequestBody HodRequestDto request) {

        HodResponseDto response =
                service.update(
                        id,
                        request
                );

        return ResponseBuilder.success(
                response,
                "HOD updated successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // SOFT DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<Void>> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseBuilder.success(
                null,
                "HOD deleted successfully",
                HttpStatus.OK
        );
    }
}
