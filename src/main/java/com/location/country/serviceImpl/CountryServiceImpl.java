package com.location.country.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.location.common.service.LocationValidationService;
import com.location.country.dto.CountryRequestDto;
import com.location.country.dto.CountryResponseDto;
import com.location.country.dto.CountrySearchDto;
import com.location.country.entity.Country;
import com.location.country.mapper.CountryMapper;
import com.location.country.repository.CountryRepository;
import com.location.country.service.CountryService;
import com.location.country.specification.CountrySpecification;
import com.location.enums.MasterStatus;
import com.sms.exception.DuplicateResourceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;
    private final LocationValidationService locationValidationService;

    @Override
    public CountryResponseDto createCountry(CountryRequestDto dto) {

        if (countryRepository.existsByCountryNameIgnoreCase(dto.getCountryName())) {
            throw new DuplicateResourceException("Country name already exists.");
        }

        if (countryRepository.existsByCountryCodeIgnoreCase(dto.getCountryCode())) {
            throw new DuplicateResourceException("Country code already exists.");
        }

        Country country = countryMapper.toEntity(dto);

        country.setStatus(MasterStatus.ACTIVE);

        Country saved = countryRepository.save(country);

        return countryMapper.toDto(saved);
    }

    @Override
    public CountryResponseDto updateCountry(Long id,
                                            CountryRequestDto dto) {

    	Country country =
    	        locationValidationService.validateCountry(id);

        if (countryRepository.existsByCountryNameIgnoreCaseAndIdNot(
                dto.getCountryName(), id)) {

            throw new DuplicateResourceException("Country name already exists.");
        }

        if (countryRepository.existsByCountryCodeIgnoreCaseAndIdNot(
                dto.getCountryCode(), id)) {

            throw new DuplicateResourceException("Country code already exists.");
        }

        countryMapper.updateEntity(country, dto);

        @SuppressWarnings("null")
		Country updated = countryRepository.save(country);

        return countryMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryResponseDto getCountryById(Long id) {

    	Country country =
    	        locationValidationService.validateCountry(id);

        return countryMapper.toDto(country);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CountryResponseDto> getAllCountries(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("countryName").ascending());

        return countryRepository
                .findAll(
                        CountrySpecification.search(
                                new CountrySearchDto()),
                        sortedPageable)
                .map(countryMapper::toDto);
    }

    @SuppressWarnings("null")
	@Override
    @Transactional(readOnly = true)
    public Page<CountryResponseDto> searchCountries(
            CountrySearchDto dto,
            Pageable pageable) {

        return countryRepository
                .findAll(
                        CountrySpecification.search(dto),
                        pageable)
                .map(countryMapper::toDto);
    }

    @Override
    public void deleteCountry(Long id) {

    	Country country =
    	        locationValidationService.validateCountry(id);

        country.setStatus(MasterStatus.DELETED);

        countryRepository.save(country);
    }

}