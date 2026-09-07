package com.sms.service;

import com.sms.dto.PageResponse;
import com.sms.dto.SemesterRequestDto;
import com.sms.dto.SemesterResponseDto;
import com.sms.dto.SemesterSearchRequest;

public interface SemesterService {

	SemesterResponseDto createSemester(SemesterRequestDto dto);

	SemesterResponseDto getSemesterById(Long id);

	PageResponse<SemesterResponseDto> getAllSemesters(int page, int size, String sortBy, String direction);

	SemesterResponseDto updateSemester(Long id, SemesterRequestDto dto);

	void deleteSemester(Long id);

	PageResponse<SemesterResponseDto> searchSemesters(SemesterSearchRequest request, int page, int size, String sortBy,
			String direction);

}