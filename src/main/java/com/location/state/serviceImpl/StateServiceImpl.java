package com.location.state.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.common.service.LocationValidationService;
import com.location.country.entity.Country;
import com.location.country.repository.CountryRepository;
import com.location.enums.MasterStatus;
import com.location.state.dto.StateRequestDto;
import com.location.state.dto.StateResponseDto;
import com.location.state.dto.StateSearchDto;
import com.location.state.entity.State;
import com.location.state.mapper.StateMapper;
import com.location.state.repository.StateRepository;
import com.location.state.service.StateService;
import com.location.state.specification.StateSpecification;
import com.sms.exception.DuplicateResourceException;
import com.sms.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StateServiceImpl implements StateService {

	private final StateRepository stateRepository;
	private final CountryRepository countryRepository;

	private final StateMapper stateMapper;

	private final LocationValidationService locationValidationService;

	@Override
	public StateResponseDto createState(StateRequestDto dto) {

		Country country = locationValidationService.validateCountry(dto.getCountryId());

		if (stateRepository.existsByCountryIdAndStateNameIgnoreCase(country.getId(), dto.getStateName().trim())) {

			throw new DuplicateResourceException("State already exists.");
		}

		if (stateRepository.existsByStateCodeIgnoreCase(dto.getStateCode().trim())) {

			throw new DuplicateResourceException("State code already exists.");
		}

		State state = stateMapper.toEntity(dto);

		State saved = stateRepository.save(state);

		return stateMapper.toDto(saved);
	}

	@Override
	public StateResponseDto updateState(Long id, StateRequestDto dto) {

		State state = locationValidationService.validateState(id); // state not found validation

		Country country = locationValidationService.validateCountry(dto.getCountryId()); // country not found validation

		if (stateRepository.existsByCountryIdAndStateNameIgnoreCaseAndIdNot(country.getId(), dto.getStateName().trim(),
				id)) {

			throw new DuplicateResourceException("State already exists.");
		}

		if (stateRepository.existsByStateCodeIgnoreCaseAndIdNot(dto.getStateCode().trim(), id)) {

			throw new DuplicateResourceException("State code already exists.");
		}

		stateMapper.updateEntity(state, dto);

		State updated = stateRepository.save(state);

		return stateMapper.toDto(updated);
	}

	@Override
	@Transactional(readOnly = true)
	public StateResponseDto getStateById(Long id) {

		State state = locationValidationService.validateState(id); // state not found validation

		return stateMapper.toDto(state);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StateResponseDto> getAllStates(Pageable pageable) {

		return stateRepository.findAll(StateSpecification.search(new StateSearchDto()), pageable)
				.map(stateMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StateResponseDto> searchStates(StateSearchDto dto, Pageable pageable) {

		return stateRepository.findAll(StateSpecification.search(dto), pageable).map(stateMapper::toDto);
	}

	@Override
	public void deleteState(Long id) {

		State state = locationValidationService.validateState(id); // state not found validation

		state.setStatus(MasterStatus.DELETED);

		stateRepository.save(state);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StateResponseDto> getStatesByCountry(Long countryId) {

		countryRepository.findByIdAndStatus(countryId, MasterStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Country not found."));

		List<State> states = stateRepository.findByCountryIdAndStatusOrderByStateNameAsc(countryId,
				MasterStatus.ACTIVE);

		return states.stream().map(stateMapper::toDto).toList();
	}

}