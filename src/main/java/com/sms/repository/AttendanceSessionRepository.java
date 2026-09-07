package com.sms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.entity.AttendanceSession;
import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.RecordStatus;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    Optional<AttendanceSession> findByIdAndStatusNot(Long id, RecordStatus status);

    Optional<AttendanceSession> findByFacultySubjectMappingIdAndAttendanceDateAndStatusNot(
            Long facultySubjectMappingId,
            LocalDate attendanceDate,
            RecordStatus status);

    boolean existsByFacultySubjectMappingIdAndAttendanceDateAndStatusNot(
            Long facultySubjectMappingId,
            LocalDate attendanceDate,
            RecordStatus status);

    List<AttendanceSession> findByFacultySubjectMappingIdAndStatusNotOrderByAttendanceDateDesc(
            Long facultySubjectMappingId,
            RecordStatus status);

    List<AttendanceSession> findBySessionStatusAndStatusNot(
            AttendanceSessionStatus sessionStatus,
            RecordStatus status);
}
