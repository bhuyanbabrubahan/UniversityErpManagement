package com.sms.fee.service;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.FeeStructureRequestDto;
import com.sms.fee.dto.FeeStructureResponseDto;
import com.sms.fee.dto.FeeStructureSearchRequest;

public interface FeeStructureService {

    // ==========================================================
    // Create
    // ==========================================================

    FeeStructureResponseDto createFeeStructure(
            FeeStructureRequestDto dto
    );

    // ==========================================================
    // Get By Id
    // ==========================================================

    FeeStructureResponseDto getFeeStructureById(
            Long id
    );

    // ==========================================================
    // Get All
    // ==========================================================

    PageResponse<FeeStructureResponseDto> getAllFeeStructures(
            int page,
            int size,
            String sortBy,
            String direction
    );

    // ==========================================================
    // Search
    // ==========================================================

    PageResponse<FeeStructureResponseDto> searchFeeStructures(
            FeeStructureSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );

    // ==========================================================
    // Update
    // ==========================================================

    FeeStructureResponseDto updateFeeStructure(
            Long id,
            FeeStructureRequestDto dto
    );

    // ==========================================================
    // Soft Delete
    // ==========================================================

    void deleteFeeStructure(
            Long id
    );

}