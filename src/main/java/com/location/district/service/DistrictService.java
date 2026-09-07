package com.location.district.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.location.district.dto.DistrictRequestDto;
import com.location.district.dto.DistrictResponseDto;
import com.location.district.dto.DistrictSearchDto;

public interface DistrictService {

    DistrictResponseDto createDistrict(DistrictRequestDto dto);

    DistrictResponseDto updateDistrict(Long id,
                                       DistrictRequestDto dto);

    DistrictResponseDto getDistrictById(Long id);

    Page<DistrictResponseDto> getAllDistricts(Pageable pageable);

    Page<DistrictResponseDto> searchDistricts(DistrictSearchDto dto,
                                              Pageable pageable);

    void deleteDistrict(Long id);
    List<DistrictResponseDto> getDistrictsByState(Long stateId);

}
