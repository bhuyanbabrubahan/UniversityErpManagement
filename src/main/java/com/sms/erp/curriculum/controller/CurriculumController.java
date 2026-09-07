package com.sms.erp.curriculum.controller;

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

import com.sms.erp.curriculum.dto.CurriculumRequestDto;
import com.sms.erp.curriculum.dto.CurriculumResponseDto;
import com.sms.erp.curriculum.dto.CurriculumSearchDto;
import com.sms.erp.curriculum.service.CurriculumService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/erp/curriculum")
@RequiredArgsConstructor
@PreAuthorize(
        "hasAnyRole('SUPER_ADMIN','ADMIN','REGISTRAR','DEAN','HOD')"
)
public class CurriculumController {

    private final CurriculumService service;

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<
            ApiResponseDto<Page<CurriculumResponseDto>>> all(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<CurriculumResponseDto> response =
                service.findAll(
                        page,
                        size
                );

        return ResponseBuilder.success(
                response,
                "Curriculums fetched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @PostMapping("/search")
    public ResponseEntity<
            ApiResponseDto<Page<CurriculumResponseDto>>> search(

            @RequestBody(
                    required = false
            )
            CurriculumSearchDto search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<CurriculumResponseDto> response =
                service.search(
                        search,
                        page,
                        size
                );

        return ResponseBuilder.success(
                response,
                "Curriculums searched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<CurriculumResponseDto>> get(
            @PathVariable Long id) {

        CurriculumResponseDto response =
                service.findById(id);

        return ResponseBuilder.success(
                response,
                "Curriculum fetched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<
            ApiResponseDto<CurriculumResponseDto>> create(
            @RequestBody CurriculumRequestDto request) {

        CurriculumResponseDto response =
                service.create(request);

        return ResponseBuilder.success(
                response,
                "Curriculum created successfully",
                HttpStatus.CREATED
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<CurriculumResponseDto>> update(

            @PathVariable Long id,

            @RequestBody CurriculumRequestDto request) {

        CurriculumResponseDto response =
                service.update(
                        id,
                        request
                );

        return ResponseBuilder.success(
                response,
                "Curriculum updated successfully",
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
                "Curriculum deleted successfully",
                HttpStatus.OK
        );
    }
}
