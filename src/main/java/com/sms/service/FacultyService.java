package com.sms.service;

import com.sms.dto.FacultyRequestDto;
import com.sms.dto.FacultyResponseDto;
import com.sms.dto.FacultySearchRequest;
import com.sms.dto.PageResponse;

public interface FacultyService {
	FacultyResponseDto createFaculty(FacultyRequestDto request);

	FacultyResponseDto getFacultyById(Long id);

	PageResponse<FacultyResponseDto> getAllFaculties(int page, int size, String sortBy, String direction);

	PageResponse<FacultyResponseDto> searchFaculties(FacultySearchRequest request, int page, int size, String sortBy,
			String direction);

	FacultyResponseDto updateFaculty(Long id, FacultyRequestDto request);

	void deleteFaculty(Long id);
}
