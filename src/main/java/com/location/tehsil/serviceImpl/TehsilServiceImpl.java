package com.location.tehsil.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.common.service.LocationValidationService;
import com.location.district.entity.District;
import com.location.district.repository.DistrictRepository;
import com.location.enums.MasterStatus;
import com.location.tehsil.dto.TehsilRequestDto;
import com.location.tehsil.dto.TehsilResponseDto;
import com.location.tehsil.dto.TehsilSearchDto;
import com.location.tehsil.entity.Tehsil;
import com.location.tehsil.mapper.TehsilMapper;
import com.location.tehsil.repository.TehsilRepository;
import com.location.tehsil.service.TehsilService;
import com.location.tehsil.specification.TehsilSpecification;
import com.sms.exception.DuplicateResourceException;
import com.sms.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TehsilServiceImpl implements TehsilService {
	private final DistrictRepository districtRepository;

	private final TehsilRepository tehsilRepository;

	private final TehsilMapper tehsilMapper;

	private final LocationValidationService validationService;

	@Override
	public TehsilResponseDto createTehsil(TehsilRequestDto dto) {

		District district = validationService.validateDistrict(dto.getDistrictId());

		if (tehsilRepository.existsByDistrictIdAndTehsilNameIgnoreCase(district.getId(), dto.getTehsilName().trim())) {

			throw new DuplicateResourceException("Tehsil already exists.");
		}

		Tehsil tehsil = tehsilMapper.toEntity(dto);

		return tehsilMapper.toDto(tehsilRepository.save(tehsil));
	}

	@Override
	public TehsilResponseDto updateTehsil(Long id, TehsilRequestDto dto) {

		Tehsil tehsil = validationService.validateTehsil(id);

		District district = validationService.validateDistrict(dto.getDistrictId());

		if (tehsilRepository.existsByDistrictIdAndTehsilNameIgnoreCaseAndIdNot(district.getId(),
				dto.getTehsilName().trim(), id)) {

			throw new DuplicateResourceException("Tehsil already exists.");
		}

		tehsilMapper.updateEntity(tehsil, dto);

		return tehsilMapper.toDto(tehsilRepository.save(tehsil));
	}

	@Override
	@Transactional(readOnly = true)
	public TehsilResponseDto getTehsilById(Long id) {

		return tehsilMapper.toDto(validationService.validateTehsil(id));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TehsilResponseDto> getAllTehsils(Pageable pageable) {

		return tehsilRepository.findAll(TehsilSpecification.search(new TehsilSearchDto()), pageable)
				.map(tehsilMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TehsilResponseDto> searchTehsils(TehsilSearchDto dto, Pageable pageable) {

		return tehsilRepository.findAll(TehsilSpecification.search(dto), pageable).map(tehsilMapper::toDto);
	}

	@Override
	public void deleteTehsil(Long id) {

		Tehsil tehsil = validationService.validateTehsil(id);

		tehsil.setStatus(MasterStatus.DELETED);

		tehsilRepository.save(tehsil);

	}

	@Override
	@Transactional(readOnly = true)
	public List<TehsilResponseDto> getTehsilsByDistrict(Long districtId) {

	    districtRepository.findByIdAndStatus(districtId, MasterStatus.ACTIVE)
	            .orElseThrow(() -> new ResourceNotFoundException("District not found."));

	    List<Tehsil> tehsils = tehsilRepository
	            .findByDistrictIdAndStatusOrderByTehsilNameAsc(
	                    districtId,
	                    MasterStatus.ACTIVE
	            );

	    return tehsils.stream()
	            .map(tehsilMapper::toDto)
	            .toList();
	}

}
