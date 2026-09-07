package com.location.country.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.location.country.dto.CountryRequestDto;
import com.location.country.dto.CountryResponseDto;
import com.location.country.dto.CountrySearchDto;

public interface CountryService {

	CountryResponseDto createCountry(CountryRequestDto dto);

	CountryResponseDto updateCountry(Long id, CountryRequestDto dto);

	CountryResponseDto getCountryById(Long id);

	Page<CountryResponseDto> getAllCountries(Pageable pageable);

	Page<CountryResponseDto> searchCountries(CountrySearchDto dto, Pageable pageable);

	void deleteCountry(Long id);

}