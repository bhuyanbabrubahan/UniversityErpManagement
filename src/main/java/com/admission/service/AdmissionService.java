package com.admission.service;

import com.admission.dto.AdmissionRequestDto;
import com.admission.dto.AdmissionResponseDto;
import com.admission.dto.AdmissionSearchRequest;
import com.sms.dto.PageResponse;

public interface AdmissionService {

	AdmissionResponseDto createAdmission(AdmissionRequestDto request);

	AdmissionResponseDto getAdmissionById(Long id);

	AdmissionResponseDto updateAdmission(Long id, AdmissionRequestDto request);

	void deleteAdmission(Long id);

	PageResponse<AdmissionResponseDto> getAllAdmissions(int page, int size, String sortBy, String direction);

	PageResponse<AdmissionResponseDto> searchAdmissions(AdmissionSearchRequest request, int page, int size,
			String sortBy, String direction);

}