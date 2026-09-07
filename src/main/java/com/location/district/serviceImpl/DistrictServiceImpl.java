package com.location.district.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.common.service.LocationValidationService;
import com.location.district.dto.DistrictRequestDto;
import com.location.district.dto.DistrictResponseDto;
import com.location.district.dto.DistrictSearchDto;
import com.location.district.entity.District;
import com.location.district.mapper.DistrictMapper;
import com.location.district.repository.DistrictRepository;
import com.location.district.service.DistrictService;
import com.location.district.specification.DistrictSpecification;
import com.location.enums.MasterStatus;
import com.location.state.entity.State;
import com.location.state.repository.StateRepository;
import com.sms.exception.DuplicateResourceException;
import com.sms.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DistrictServiceImpl implements DistrictService {
	private final StateRepository stateRepository;

    private final DistrictRepository districtRepository;

    private final DistrictMapper districtMapper;

    private final LocationValidationService validationService;

    @Override
    public DistrictResponseDto createDistrict(DistrictRequestDto dto) {

        State state =
                validationService.validateState(dto.getStateId());

        if (districtRepository.existsByStateIdAndDistrictNameIgnoreCase(
                state.getId(),
                dto.getDistrictName().trim())) {

            throw new DuplicateResourceException(
                    "District already exists.");
        }

        if (districtRepository.existsByDistrictCodeIgnoreCase(
                dto.getDistrictCode().trim())) {

            throw new DuplicateResourceException(
                    "District code already exists.");
        }

        District district =
                districtMapper.toEntity(dto);

        return districtMapper.toDto(
                districtRepository.save(district));
    }

    @Override
    public DistrictResponseDto updateDistrict(
            Long id,
            DistrictRequestDto dto) {

        District district =
                validationService.validateDistrict(id);

        State state =
                validationService.validateState(dto.getStateId());

        if (districtRepository
                .existsByStateIdAndDistrictNameIgnoreCaseAndIdNot(
                        state.getId(),
                        dto.getDistrictName().trim(),
                        id)) {

            throw new DuplicateResourceException(
                    "District already exists.");
        }

        if (districtRepository
                .existsByDistrictCodeIgnoreCaseAndIdNot(
                        dto.getDistrictCode().trim(),
                        id)) {

            throw new DuplicateResourceException(
                    "District code already exists.");
        }

        districtMapper.updateEntity(
                district,
                dto);

        return districtMapper.toDto(
                districtRepository.save(district));
    }

    @Override
    @Transactional(readOnly = true)
    public DistrictResponseDto getDistrictById(Long id) {

        return districtMapper.toDto(
                validationService.validateDistrict(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DistrictResponseDto> getAllDistricts(
            Pageable pageable) {

        return districtRepository
                .findAll(
                        DistrictSpecification.search(
                                new DistrictSearchDto()),
                        pageable)
                .map(districtMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DistrictResponseDto> searchDistricts(
            DistrictSearchDto dto,
            Pageable pageable) {

        return districtRepository
                .findAll(
                        DistrictSpecification.search(dto),
                        pageable)
                .map(districtMapper::toDto);
    }

    @Override
    public void deleteDistrict(Long id) {

        District district =
                validationService.validateDistrict(id);

        district.setStatus(MasterStatus.DELETED);

        districtRepository.save(district);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistrictResponseDto> getDistrictsByState(Long stateId) {

        stateRepository.findByIdAndStatus(stateId, MasterStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("State not found."));

        List<District> districts = districtRepository
                .findByStateIdAndStatusOrderByDistrictNameAsc(stateId, MasterStatus.ACTIVE);

        return districts.stream()
                .map(districtMapper::toDto)
                .toList();
    }

}