package com.sms.service;

import com.sms.dto.PageResponse;
import com.sms.dto.StudentRequestDto;
import com.sms.dto.StudentResponseDto;
import com.sms.dto.StudentSearchRequest;

public interface StudentService {

    // ==========================================================
    // CREATE
    // ==========================================================

    StudentResponseDto saveStudent(
            StudentRequestDto requestDto
    );

    // ==========================================================
    // GET BY ID
    // ==========================================================

    StudentResponseDto getStudentById(
            Long id
    );

    // ==========================================================
    // UPDATE
    // ==========================================================

    StudentResponseDto updateStudent(
            Long id,
            StudentRequestDto requestDto
    );

    // ==========================================================
    // DELETE (Soft Delete)
    // ==========================================================

    void deleteStudent(
            Long id
    );

    // ==========================================================
    // GET ALL
    // ==========================================================

    PageResponse<StudentResponseDto> getAllStudents(
            int page,
            int size,
            String sortBy,
            String direction
    );

    // ==========================================================
    // SEARCH
    // ==========================================================

    PageResponse<StudentResponseDto> searchStudents(
            StudentSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );

}