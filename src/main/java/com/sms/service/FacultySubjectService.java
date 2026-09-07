package com.sms.service;

import java.util.List;

import com.sms.dto.FacultySubjectRequestDto;
import com.sms.dto.FacultySubjectResponseDto;
import com.sms.dto.FacultySubjectSearchRequest;
import com.sms.dto.PageResponse;

public interface FacultySubjectService {

	FacultySubjectResponseDto assignSubject(FacultySubjectRequestDto dto);

	FacultySubjectResponseDto getById(Long id);

	PageResponse<FacultySubjectResponseDto> getAll(int page, int size, String sortBy, String direction);

	FacultySubjectResponseDto update(Long id, FacultySubjectRequestDto dto);

	void delete(Long id);

	PageResponse<FacultySubjectResponseDto> search(FacultySubjectSearchRequest request, int page, int size,
			String sortBy, String direction);
	
	List<FacultySubjectResponseDto> getMySubjects(String academicYear);
	
	List<FacultySubjectResponseDto> getSubjectsByFacultyAndAcademicYear(
	        Long facultyId,
	        String academicYear
	);


	
}