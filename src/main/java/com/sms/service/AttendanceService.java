package com.sms.service;

import java.util.List;

import com.sms.dto.PageResponse;
import com.sms.dto.attendance.AttendanceClassRequestDto;
import com.sms.dto.attendance.AttendanceClassResponseDto;
import com.sms.dto.attendance.AttendanceRequestDto;
import com.sms.dto.attendance.AttendanceResponseDto;
import com.sms.dto.attendance.AttendanceSearchRequest;
import com.sms.dto.attendance.BulkAttendanceRequestDto;

public interface AttendanceService {

    AttendanceResponseDto createAttendance(AttendanceRequestDto dto);

    AttendanceResponseDto getAttendanceById(Long id);

    PageResponse<AttendanceResponseDto> getAllAttendance(
            int page, int size, String sortBy, String direction);

    AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto dto);

    void deleteAttendance(Long id);

    PageResponse<AttendanceResponseDto> searchAttendance(
            AttendanceSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction);

    AttendanceClassResponseDto getClassAttendance(AttendanceClassRequestDto request);

    List<AttendanceResponseDto> saveBulkAttendance(BulkAttendanceRequestDto request);
}
