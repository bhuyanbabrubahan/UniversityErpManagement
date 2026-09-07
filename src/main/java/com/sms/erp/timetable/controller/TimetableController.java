package com.sms.erp.timetable.controller;

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

import com.sms.erp.timetable.dto.TimetableRequestDto;
import com.sms.erp.timetable.dto.TimetableResponseDto;
import com.sms.erp.timetable.dto.TimetableSearchDto;
import com.sms.erp.timetable.service.TimetableService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/erp/timetable")
@RequiredArgsConstructor
@PreAuthorize(
        "hasAnyRole("
                + "'SUPER_ADMIN',"
                + "'ADMIN',"
                + "'REGISTRAR',"
                + "'DEAN',"
                + "'HOD'"
                + ")"
)
public class TimetableController {

    private final TimetableService service;

    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<
            ApiResponseDto<
                    Page<TimetableResponseDto>>> all(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<TimetableResponseDto> response =
                service.findAll(
                        page,
                        size
                );

        return ResponseBuilder.success(
                response,
                "Timetables fetched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // SEARCH
    // =====================================================

    @PostMapping("/search")
    public ResponseEntity<
            ApiResponseDto<
                    Page<TimetableResponseDto>>> search(

            @RequestBody(required = false)
            TimetableSearchDto search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<TimetableResponseDto> response =
                service.search(
                        search,
                        page,
                        size
                );

        return ResponseBuilder.success(
                response,
                "Timetables searched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<TimetableResponseDto>> get(

            @PathVariable Long id) {

        TimetableResponseDto response =
                service.findById(id);

        return ResponseBuilder.success(
                response,
                "Timetable fetched successfully",
                HttpStatus.OK
        );
    }

    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<
            ApiResponseDto<TimetableResponseDto>> create(

            @RequestBody
            TimetableRequestDto request) {

        TimetableResponseDto response =
                service.create(request);

        return ResponseBuilder.success(
                response,
                "Timetable created successfully",
                HttpStatus.CREATED
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<
            ApiResponseDto<TimetableResponseDto>> update(

            @PathVariable Long id,

            @RequestBody
            TimetableRequestDto request) {

        TimetableResponseDto response =
                service.update(
                        id,
                        request
                );

        return ResponseBuilder.success(
                response,
                "Timetable updated successfully",
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
                "Timetable deleted successfully",
                HttpStatus.OK
        );
    }
}