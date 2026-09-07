package com.sms.service;

import com.sms.dto.PageResponse;
import com.sms.dto.SubjectRequestDto;
import com.sms.dto.SubjectResponseDto;
import com.sms.dto.SubjectSearchRequest;

public interface SubjectService {

	SubjectResponseDto createSubject(SubjectRequestDto dto);

	SubjectResponseDto getSubjectById(Long id);

	PageResponse<SubjectResponseDto> getAllSubjects(int page, int size, String sortBy, String direction);

	SubjectResponseDto updateSubject(Long id, SubjectRequestDto dto);

	void deleteSubject(Long id);

	PageResponse<SubjectResponseDto> searchSubjects(SubjectSearchRequest request, int page, int size, String sortBy,
			String direction);

}