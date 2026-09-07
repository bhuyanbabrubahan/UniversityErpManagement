package com.sms.service;

import java.time.LocalDate;
import java.util.List;

import com.sms.dto.attendance.AttendanceSessionRequestDto;
import com.sms.dto.attendance.AttendanceSessionResponseDto;
import com.sms.dto.attendance.AttendanceSessionDetailsResponseDto;

public interface AttendanceSessionService {

    AttendanceSessionResponseDto openSession(AttendanceSessionRequestDto request);

    AttendanceSessionResponseDto getSessionById(Long id);

    AttendanceSessionDetailsResponseDto getSessionDetailsForManagement(Long id);

    AttendanceSessionResponseDto getSessionByMappingAndDate(Long mappingId, LocalDate date);

    List<AttendanceSessionResponseDto> getSessionHistory(Long mappingId);

    AttendanceSessionResponseDto completeSession(Long id);

    AttendanceSessionResponseDto cancelSession(Long id);
}
