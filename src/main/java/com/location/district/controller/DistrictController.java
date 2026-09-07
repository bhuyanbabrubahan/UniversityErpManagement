package com.location.district.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.location.district.dto.DistrictRequestDto;
import com.location.district.dto.DistrictResponseDto;
import com.location.district.dto.DistrictSearchDto;
import com.location.district.service.DistrictService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<DistrictResponseDto>> createDistrict(
            @Valid @RequestBody DistrictRequestDto dto) {

        DistrictResponseDto response = districtService.createDistrict(dto);

        return ResponseBuilder.success(
                response,
                "District created successfully.",
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DistrictResponseDto>> getDistrictById(
            @PathVariable Long id) {

        DistrictResponseDto response = districtService.getDistrictById(id);

        return ResponseBuilder.success(
                response,
                "District fetched successfully.",
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<DistrictResponseDto>>> getAllDistricts(
            Pageable pageable) {

        Page<DistrictResponseDto> response =
                districtService.getAllDistricts(pageable);

        return ResponseBuilder.success(
                response,
                "District list fetched successfully.",
                HttpStatus.OK
        );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<DistrictResponseDto>>> searchDistricts(
            @RequestBody DistrictSearchDto dto,
            Pageable pageable) {

        Page<DistrictResponseDto> response =
                districtService.searchDistricts(dto, pageable);

        return ResponseBuilder.success(
                response,
                "District search completed successfully.",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DistrictResponseDto>> updateDistrict(
            @PathVariable Long id,
            @Valid @RequestBody DistrictRequestDto dto) {

        DistrictResponseDto response =
                districtService.updateDistrict(id, dto);

        return ResponseBuilder.success(
                response,
                "District updated successfully.",
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Object>> deleteDistrict(
            @PathVariable Long id) {

        districtService.deleteDistrict(id);

        return ResponseBuilder.success(
                null,
                "District deleted successfully.",
                HttpStatus.OK
        );
    }
    
    @GetMapping("/state/{stateId}")
    public ResponseEntity<ApiResponseDto<List<DistrictResponseDto>>> getDistrictsByState(
            @PathVariable Long stateId) {

        List<DistrictResponseDto> response =
                districtService.getDistrictsByState(stateId);

        return ResponseBuilder.success(
                response,
                "Districts fetched successfully.",
                HttpStatus.OK);
    }

}