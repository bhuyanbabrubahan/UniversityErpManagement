package com.sms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Attendance;
import com.sms.enums.AttendanceStatus;
import com.sms.enums.RecordStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    boolean existsByAttendanceSessionIdAndStudentIdAndStatusNot(
            Long attendanceSessionId,
            Long studentId,
            RecordStatus status);

    boolean existsByAttendanceSessionIdAndStudentIdAndStatusNotAndIdNot(
            Long attendanceSessionId,
            Long studentId,
            RecordStatus status,
            Long id);

    Optional<Attendance> findByIdAndStatusNot(Long id, RecordStatus status);

    Optional<Attendance> findByAttendanceSessionIdAndStudentIdAndStatusNot(
            Long attendanceSessionId,
            Long studentId,
            RecordStatus status);

    Optional<Attendance> findByAttendanceSessionIdAndStudentId(
            Long attendanceSessionId,
            Long studentId);

    @Query("""
        select a
        from Attendance a
        join fetch a.student s
        join fetch a.attendanceSession session
        where session.id = :sessionId
          and a.status <> :status
        order by s.rollNumber asc
        """)
    List<Attendance> findByAttendanceSessionId(
            @Param("sessionId") Long sessionId,
            @Param("status") RecordStatus status);

    @Query("""
        select a
        from Attendance a
        join fetch a.student s
        join fetch a.attendanceSession session
        where session.facultySubjectMapping.id = :mappingId
          and session.attendanceDate = :attendanceDate
          and a.status <> :status
        order by s.rollNumber asc
        """)
    List<Attendance> findByMappingAndDate(
            @Param("mappingId") Long mappingId,
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("status") RecordStatus status);

    Page<Attendance> findByStatusNot(RecordStatus status, Pageable pageable);

    long countByStatusNot(RecordStatus status);

    @Query("""
        select count(a)
        from Attendance a
        where a.attendanceSession.attendanceDate = :attendanceDate
          and a.status <> :status
        """)
    long countByAttendanceDateAndStatusNot(
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("status") RecordStatus status);

    @Query("""
        select count(a)
        from Attendance a
        where a.attendanceStatus = :attendanceStatus
          and a.attendanceSession.attendanceDate = :attendanceDate
          and a.status <> :status
        """)
    long countByAttendanceStatusAndAttendanceDateAndStatusNot(
            @Param("attendanceStatus") AttendanceStatus attendanceStatus,
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("status") RecordStatus status);

    @Query("""
        select count(distinct a.student.id)
        from Attendance a
        where a.attendanceSession.facultySubjectMapping.faculty.id = :facultyId
          and a.status <> :status
        """)
    long countDistinctStudentsByFacultyId(
            @Param("facultyId") Long facultyId,
            @Param("status") RecordStatus status);

    @Query("""
        select count(a)
        from Attendance a
        where a.attendanceSession.facultySubjectMapping.faculty.id = :facultyId
          and a.attendanceSession.attendanceDate = :date
          and a.status <> :status
        """)
    long countByFacultyIdAndAttendanceDateAndStatusNot(
            @Param("facultyId") Long facultyId,
            @Param("date") LocalDate date,
            @Param("status") RecordStatus status);

    @Query("""
        select count(a)
        from Attendance a
        where a.student.id = :studentId
          and a.status <> :status
        """)
    long countByStudentIdAndStatusNot(
            @Param("studentId") Long studentId,
            @Param("status") RecordStatus status);
}
