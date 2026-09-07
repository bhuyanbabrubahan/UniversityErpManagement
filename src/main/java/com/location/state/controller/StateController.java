package com.location.state.controller;

import java.util.List;

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

import com.location.state.dto.StateRequestDto;
import com.location.state.dto.StateResponseDto;
import com.location.state.dto.StateSearchDto;
import com.location.state.service.StateService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;
    
    @PostMapping
    public ResponseEntity<ApiResponseDto<StateResponseDto>> createState(
            @Valid @RequestBody StateRequestDto dto) {
    	
    	StateResponseDto response = stateService.createState(dto);

        return ResponseBuilder.success(response, "State created successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<StateResponseDto>> getState(
            @PathVariable Long id) {

    	StateResponseDto response = stateService.getStateById(id);
        
        return ResponseBuilder.success(response, "State fetched successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<StateResponseDto>>> getAllStates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "stateName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StateResponseDto> response = stateService.getAllStates(pageable);
                
        return ResponseBuilder.success(
        		response, 
        		"States fetched successfully.", 
        		HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<StateResponseDto>>> searchStates(
            @RequestBody StateSearchDto dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "stateName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<StateResponseDto> response = stateService.searchStates(dto, pageable);

        return ResponseBuilder.success(
        		response, 
        		"States fetched successfully.", 
        		HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<StateResponseDto>> updateState(
            @PathVariable Long id,
            @Valid @RequestBody StateRequestDto dto) {

    	StateResponseDto response = stateService.updateState(id, dto);
    	
    	return ResponseBuilder.success(
        		response, 
        		"States updated successfully.", 
        		HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteState(
            @PathVariable Long id) {

        stateService.deleteState(id);
        
        return ResponseBuilder.success(
        		null, 
        		"States deleted successfully.", 
        		HttpStatus.OK);
    }
    
    @GetMapping("/country/{countryId}")
    public ResponseEntity<ApiResponseDto<List<StateResponseDto>>> getStatesByCountry(
            @PathVariable Long countryId) {

        List<StateResponseDto> response =
                stateService.getStatesByCountry(countryId);

        return ResponseBuilder.success(
                response,
                "States fetched successfully.",
                HttpStatus.OK);
    }

    
}
