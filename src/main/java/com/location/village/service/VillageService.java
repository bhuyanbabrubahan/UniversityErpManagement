package com.location.village.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.location.village.dto.VillageRequestDto;
import com.location.village.dto.VillageResponseDto;
import com.location.village.dto.VillageSearchDto;

public interface VillageService {

    VillageResponseDto createVillage(VillageRequestDto dto);

    VillageResponseDto updateVillage(Long id,
                                     VillageRequestDto dto);

    VillageResponseDto getVillageById(Long id);

    Page<VillageResponseDto> getAllVillages(Pageable pageable);

    Page<VillageResponseDto> searchVillages(VillageSearchDto dto,
                                            Pageable pageable);

    List<VillageResponseDto> getVillagesByTehsil(Long tehsilId);

    List<VillageResponseDto> getVillagesByPincode(String pincode);

    void deleteVillage(Long id);

}