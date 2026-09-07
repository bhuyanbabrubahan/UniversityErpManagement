package com.sms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.attendance.AttendanceSessionRequestDto;
import com.sms.dto.attendance.AttendanceSessionResponseDto;
import com.sms.dto.attendance.AttendanceSessionDetailsResponseDto;
import com.sms.payload.ApiResponseDto;
import com.sms.service.AttendanceSessionService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance-sessions")
@RequiredArgsConstructor
@Tag(
        name = "Attendance Sessions",
        description = "Session lifecycle APIs for ERP attendance."
)
public class AttendanceSessionController {

    private final AttendanceSessionService service;

    @PostMapping("/open")
    @Operation(summary = "Open attendance session")
    @AuditLog(
            action = AuditAction.CREATE,
            module = "ATTENDANCE_SESSION",
            description = "Open attendance session"
    )
    public ResponseEntity<ApiResponseDto<AttendanceSessionResponseDto>> openSession(
            @Valid @RequestBody AttendanceSessionRequestDto request) {

        return ResponseBuilder.success(
                service.openSession(request),
                "Attendance session opened successfully",
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id:\\d+}/details")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','FACULTY')")
    @Operation(summary = "Get attendance session details for management")
    public ResponseEntity<ApiResponseDto<AttendanceSessionDetailsResponseDto>> getDetails(@PathVariable Long id) {
        return ResponseBuilder.success(
                service.getSessionDetailsForManagement(id),
                "Attendance session details fetched successfully",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Get attendance session")
    public ResponseEntity<ApiResponseDto<AttendanceSessionResponseDto>> getSession(
            @PathVariable Long id) {

        return ResponseBuilder.success(
                service.getSessionById(id),
                "Attendance session fetched successfully",
                HttpStatus.OK
        );
    }

    @GetMapping("/mapping/{mappingId:\\d+}/date")
    @Operation(summary = "Get session by mapping and date")
    public ResponseEntity<ApiResponseDto<AttendanceSessionResponseDto>> getByMappingAndDate(
            @PathVariable Long mappingId,
            @RequestParam LocalDate date) {

        return ResponseBuilder.success(
                service.getSessionByMappingAndDate(mappingId, date),
                "Attendance session fetched successfully",
                HttpStatus.OK
        );
    }


    @GetMapping("/mapping/{mappingId:\\d+}/history")
    @Operation(summary = "Get attendance session history")
    public ResponseEntity<ApiResponseDto<List<AttendanceSessionResponseDto>>> history(
            @PathVariable Long mappingId) {

        return ResponseBuilder.success(
                service.getSessionHistory(mappingId),
                "Attendance session history fetched successfully",
                HttpStatus.OK
        );
    }

    @PostMapping("/{id:\\d+}/complete")
    @Operation(summary = "Complete attendance session")
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "ATTENDANCE_SESSION",
            description = "Complete attendance session"
    )
    public ResponseEntity<ApiResponseDto<AttendanceSessionResponseDto>> complete(
            @PathVariable Long id) {

        return ResponseBuilder.success(
                service.completeSession(id),
                "Attendance session completed successfully",
                HttpStatus.OK
        );
    }

    @PostMapping("/{id:\\d+}/cancel")
    @Operation(summary = "Cancel attendance session")
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "ATTENDANCE_SESSION",
            description = "Cancel attendance session"
    )
    public ResponseEntity<ApiResponseDto<AttendanceSessionResponseDto>> cancel(
            @PathVariable Long id) {

        return ResponseBuilder.success(
                service.cancelSession(id),
                "Attendance session cancelled successfully",
                HttpStatus.OK
        );
    }
}

