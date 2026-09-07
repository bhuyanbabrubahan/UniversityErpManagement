package com.sms.service;

import com.sms.dto.PageResponse;
import com.sms.dto.ResultRequestDto;
import com.sms.dto.ResultResponseDto;
import com.sms.dto.ResultSearchRequest;

public interface ResultService {

	ResultResponseDto createResult(ResultRequestDto request);

	ResultResponseDto updateResult(Long id, ResultRequestDto request);

	ResultResponseDto getResultById(Long id);


	PageResponse<ResultResponseDto> getAllResults(int page, int size, String sortBy, String direction);

	PageResponse<ResultResponseDto> searchResults(ResultSearchRequest request, int page, int size, String sortBy,
			String direction);

	void deleteResult(Long id);

}