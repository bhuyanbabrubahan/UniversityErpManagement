package com.sms.service;


import com.sms.dto.ExamRequestDto;
import com.sms.dto.ExamResponseDto;
import com.sms.dto.ExamSearchRequest;
import com.sms.dto.PageResponse;


public interface ExamService {


    // CREATE

    ExamResponseDto createExam(
            ExamRequestDto dto
    );



    // GET BY ID

    ExamResponseDto getExamById(
            Long id
    );



    // GET ALL

    PageResponse<ExamResponseDto> getAllExams(
            int page,
            int size,
            String sortBy,
            String direction
    );



    // UPDATE

    ExamResponseDto updateExam(
            Long id,
            ExamRequestDto dto
    );



    // DELETE

    void deleteExam(
            Long id
    );



    // SEARCH

    PageResponse<ExamResponseDto> searchExams(
            ExamSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );


}