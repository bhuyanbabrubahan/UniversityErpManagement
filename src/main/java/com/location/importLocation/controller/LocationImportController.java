package com.location.importLocation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.location.importLocation.dto.ImportResultDto;
import com.location.importLocation.service.LocationImportService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location-import")
@RequiredArgsConstructor
public class LocationImportController {

    private final LocationImportService locationImportService;

    @PostMapping("/import")
    public ResponseEntity<ApiResponseDto<ImportResultDto>> importLocation() {

        ImportResultDto response =
                locationImportService.importLocationMaster();

        return ResponseBuilder.success(
                response,
                "Location Master Imported Successfully",
                HttpStatus.OK
        );
    }

}