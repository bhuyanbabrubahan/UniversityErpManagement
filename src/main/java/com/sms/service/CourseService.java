package com.sms.service;

import com.sms.dto.CourseRequestDto;
import com.sms.dto.CourseResponseDto;
import com.sms.dto.CourseSearchRequest;
import com.sms.dto.PageResponse;

public interface CourseService {

    CourseResponseDto createCourse(CourseRequestDto dto);

    CourseResponseDto getCourseById(Long id);

    CourseResponseDto updateCourse(Long id, CourseRequestDto dto);

    void deleteCourse(Long id);

    PageResponse<CourseResponseDto> getAllCourses(int page, int size, String sortBy, String direction);

    PageResponse<CourseResponseDto> searchCourses(CourseSearchRequest request,
                                                  int page,
                                                  int size,
                                                  String sortBy,
                                                  String direction);
}
