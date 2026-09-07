package com.location.country.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.location.country.dto.CountryRequestDto;
import com.location.country.dto.CountryResponseDto;
import com.location.country.dto.CountrySearchDto;
import com.location.country.service.CountryService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponseDto<CountryResponseDto>> createCountry(
            @Valid @RequestBody CountryRequestDto requestDto) {

        CountryResponseDto response = countryService.createCountry(requestDto);

        return ResponseBuilder.success(response,
	            "Country deleted successfully",
	            HttpStatus.OK
	    );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CountryResponseDto>> getCountryById(
            @PathVariable Long id) {

        CountryResponseDto response = countryService.getCountryById(id);

        return ResponseBuilder.success(response,
                        "Country fetched successfully.",
                        HttpStatus.OK);
    }

    // GET ALL WITH PAGINATION & SORTING
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<CountryResponseDto>>> getAllCountries(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "countryName") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CountryResponseDto> response =
                countryService.getAllCountries(pageable);

        return ResponseBuilder.success(response,
                "Country fetched successfully.",
                HttpStatus.OK);
    }

    // SEARCH
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<CountryResponseDto>>> searchCountries(

            @RequestBody CountrySearchDto searchDto,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "countryName") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CountryResponseDto> response =
                countryService.searchCountries(searchDto, pageable);

        return ResponseBuilder.success(response,
        		"Countries searched successfully.",
                HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CountryResponseDto>> updateCountry(
            @PathVariable Long id,
            @Valid @RequestBody CountryRequestDto requestDto) {

        CountryResponseDto response =
                countryService.updateCountry(id, requestDto);

        return ResponseBuilder.success(response,
        		"Country updated successfully.",
                HttpStatus.OK);
        
    }

    // SOFT DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteCountry(
            @PathVariable Long id) {

        countryService.deleteCountry(id);

        return ResponseBuilder.success(null,
        		"Country deleted successfully.",
                HttpStatus.OK);
        
        
    }

}