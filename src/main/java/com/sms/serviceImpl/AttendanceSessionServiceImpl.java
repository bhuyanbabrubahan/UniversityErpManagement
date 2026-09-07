package com.sms.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.attendance.AttendanceSessionRequestDto;
import com.sms.dto.attendance.AttendanceSessionResponseDto;
import com.sms.dto.attendance.AttendanceSessionDetailsResponseDto;
import com.sms.dto.attendance.AttendanceResponseDto;
import com.sms.entity.Attendance;
import com.sms.entity.AttendanceSession;
import com.sms.entity.Course;
import com.sms.entity.Faculty;
import com.sms.entity.FacultySubjectMapping;
import com.sms.entity.Semester;
import com.sms.entity.Subject;
import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceSource;
import com.sms.enums.AttendanceStatus;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.AttendanceSessionRepository;
import com.sms.repository.FacultyRepository;
import com.sms.repository.FacultySubjectRepository;
import com.sms.repository.StudentRepository;
import com.sms.service.AttendanceSessionService;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceSessionServiceImpl implements AttendanceSessionService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    @AuditLog(action = AuditAction.CREATE, module = "ATTENDANCE_SESSION", description = "Open attendance session")
    public AttendanceSessionResponseDto openSession(AttendanceSessionRequestDto request) {

    	Faculty faculty = getCurrentFaculty();

    	FacultySubjectMapping mapping =
    	        facultySubjectRepository
    	                .findDetailedByIdAndFacultyIdAndStatusNot(
    	                        request.getFacultySubjectMappingId(),
    	                        faculty.getId(),
    	                        RecordStatus.DELETED)
    	                .orElseThrow(() ->
    	                        new ResourceNotFoundException(
    	                                "Faculty subject mapping not found or not assigned to this faculty."
    	                        ));


        validateSessionDate(request.getAttendanceDate(), mapping, request.getAttendanceSource());

        AttendanceSession existing = sessionRepository
                .findByFacultySubjectMappingIdAndAttendanceDateAndStatusNot(
                        mapping.getId(), request.getAttendanceDate(), RecordStatus.DELETED)
                .orElse(null);

        if (existing != null) {
            if (existing.getSessionStatus() == AttendanceSessionStatus.COMPLETED) {
                throw new BusinessException("Attendance for this date is already completed.");
            }
            if (existing.getSessionStatus() == AttendanceSessionStatus.CANCELLED) {
                throw new BusinessException("Attendance session for this date is cancelled.");
            }
            return toResponse(existing);
        }

        AttendanceSession session = new AttendanceSession();
        session.setFacultySubjectMapping(mapping);
        session.setAttendanceDate(request.getAttendanceDate());
        session.setAttendanceSource(request.getAttendanceSource());
        session.setSessionStatus(AttendanceSessionStatus.OPEN);
        session.setStatus(RecordStatus.ACTIVE);
        session.setStartedAt(LocalDateTime.now());
        session.setCreatedByUserId(SecurityUtils.getCurrentUserId());
        session.setCreatedBy(SecurityUtils.getCurrentFullName());

        return toResponse(sessionRepository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSessionDetailsResponseDto getSessionDetailsForManagement(Long id) {
        AttendanceSession session = getActiveSession(id);
        String role = SecurityUtils.getCurrentRole();
        FacultySubjectMapping mapping = session.getFacultySubjectMapping();
        if ("FACULTY".equals(role)) {
            Faculty faculty = getCurrentFaculty();
            if (mapping == null || mapping.getFaculty() == null || !faculty.getId().equals(mapping.getFaculty().getId())) {
                throw new ResourceNotFoundException("Attendance session is not assigned to the current faculty.");
            }
        } else if (!"ADMIN".equals(role) && !"SUPER_ADMIN".equals(role)) {
            throw new BusinessException("You are not authorized to view attendance session details.");
        }
        AttendanceSessionDetailsResponseDto dto = new AttendanceSessionDetailsResponseDto();
        dto.setSession(toResponse(session));
        dto.setAttendance(attendanceRepository.findByAttendanceSessionId(session.getId(), RecordStatus.DELETED)
                .stream().map(this::toAttendanceResponse).toList());
        return dto;
    }

    private AttendanceResponseDto toAttendanceResponse(Attendance attendance) {
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.setId(attendance.getId());
        dto.setAttendanceSessionId(attendance.getAttendanceSession() != null ? attendance.getAttendanceSession().getId() : null);
        dto.setSessionStatus(attendance.getAttendanceSession() != null ? attendance.getAttendanceSession().getSessionStatus() : null);
        dto.setStudentId(attendance.getStudent() != null ? attendance.getStudent().getId() : null);
        dto.setStudentName(attendance.getStudent() != null ? attendance.getStudent().getFullName() : null);
        dto.setRollNumber(attendance.getStudent() != null ? attendance.getStudent().getRollNumber() : null);
        dto.setAttendanceDate(attendance.getAttendanceSession() != null ? attendance.getAttendanceSession().getAttendanceDate() : null);
        dto.setAttendanceStatus(attendance.getAttendanceStatus());
        dto.setRemarks(attendance.getRemarks());
        dto.setAttendanceSource(attendance.getAttendanceSession() != null ? attendance.getAttendanceSession().getAttendanceSource() : null);
        dto.setMarkedAt(attendance.getMarkedAt());
        dto.setStatus(attendance.getStatus());
        dto.setCreatedByUserId(attendance.getCreatedByUserId());
        dto.setCreatedBy(attendance.getCreatedBy());
        dto.setCreatedAt(attendance.getCreatedAt());
        dto.setUpdatedByUserId(attendance.getUpdatedByUserId());
        dto.setUpdatedBy(attendance.getUpdatedBy());
        dto.setUpdatedAt(attendance.getUpdatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSessionResponseDto getSessionById(Long id) {
        AttendanceSession session = getActiveSession(id);
        Faculty faculty = getCurrentFaculty();
        FacultySubjectMapping mapping = session.getFacultySubjectMapping();
        if (mapping == null || mapping.getFaculty() == null
                || !faculty.getId().equals(mapping.getFaculty().getId())) {
            throw new ResourceNotFoundException("Attendance session is not assigned to the current faculty.");
        }
        return toResponse(session);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSessionResponseDto getSessionByMappingAndDate(
            Long mappingId,
            LocalDate date) {

        if (mappingId == null || mappingId <= 0) {
            throw new BusinessException(
                    "Valid faculty subject mapping is required.");
        }

        if (date == null) {
            throw new BusinessException(
                    "Attendance date is required.");
        }

        // ---------------------------------------------------------
        // CURRENT LOGGED-IN FACULTY
        // ---------------------------------------------------------

        Faculty faculty = getCurrentFaculty();

        // ---------------------------------------------------------
        // VERIFY MAPPING BELONGS TO CURRENT FACULTY
        // ---------------------------------------------------------

        FacultySubjectMapping mapping =
                facultySubjectRepository
                        .findDetailedByIdAndFacultyIdAndStatusNot(
                                mappingId,
                                faculty.getId(),
                                RecordStatus.DELETED)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Faculty subject mapping not found or not assigned to this faculty."
                                ));

        // ---------------------------------------------------------
        // FIND SESSION
        // ---------------------------------------------------------

        return sessionRepository
                .findByFacultySubjectMappingIdAndAttendanceDateAndStatusNot(
                        mapping.getId(),
                        date,
                        RecordStatus.DELETED)
                .map(this::toResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance session not found for mapping "
                                + mappingId
                                + " and date "
                                + date
                        ));
    }


    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSessionResponseDto> getSessionHistory(Long mappingId) {

        Faculty faculty = getCurrentFaculty();

        FacultySubjectMapping mapping =
                facultySubjectRepository
                        .findDetailedByIdAndFacultyIdAndStatusNot(
                                mappingId,
                                faculty.getId(),
                                RecordStatus.DELETED)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Faculty subject mapping not found or not assigned to this faculty."
                                ));

        return sessionRepository
                .findByFacultySubjectMappingIdAndStatusNotOrderByAttendanceDateDesc(
                        mapping.getId(),
                        RecordStatus.DELETED)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    @Transactional
    @AuditLog(action = AuditAction.UPDATE, module = "ATTENDANCE_SESSION", description = "Complete attendance session")
    public AttendanceSessionResponseDto completeSession(Long id) {
        AttendanceSession session = getFacultySession(id);

        if (session.getSessionStatus() == AttendanceSessionStatus.COMPLETED) {
            throw new BusinessException("Attendance session is already completed.");
        }
        if (session.getSessionStatus() == AttendanceSessionStatus.CANCELLED) {
            throw new BusinessException("Cancelled attendance session cannot be completed.");
        }

        FacultySubjectMapping mapping = session.getFacultySubjectMapping();
        if (mapping == null || mapping.getSubject() == null || mapping.getSubject().getSemester() == null
                || mapping.getSubject().getSemester().getCourse() == null) {
            throw new BusinessException("Session academic information is incomplete.");
        }

        int eligibleStudents = studentRepository.findEligibleStudentsForAttendance(
                mapping.getSubject().getSemester().getId(),
                mapping.getSubject().getSemester().getCourse().getId(),
                RecordStatus.ACTIVE).size();
        int markedStudents = attendanceRepository.findByAttendanceSessionId(
                session.getId(), RecordStatus.DELETED).size();

        if (eligibleStudents > 0 && markedStudents < eligibleStudents) {
            throw new BusinessException(
                    "Attendance cannot be completed. Attendance is pending for "
                            + (eligibleStudents - markedStudents) + " student(s).");
        }

        session.setSessionStatus(AttendanceSessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        session.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        session.setUpdatedBy(SecurityUtils.getCurrentFullName());

        return toResponse(sessionRepository.save(session));
    }

    @Override
    @Transactional
    @AuditLog(action = AuditAction.UPDATE, module = "ATTENDANCE_SESSION", description = "Cancel attendance session")
    public AttendanceSessionResponseDto cancelSession(Long id) {
        AttendanceSession session = getFacultySession(id);

        if (session.getSessionStatus() == AttendanceSessionStatus.COMPLETED) {
            throw new BusinessException("Completed attendance session cannot be cancelled.");
        }
        if (session.getSessionStatus() == AttendanceSessionStatus.CANCELLED) {
            throw new BusinessException("Attendance session is already cancelled.");
        }

        session.setSessionStatus(AttendanceSessionStatus.CANCELLED);
        session.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        session.setUpdatedBy(SecurityUtils.getCurrentFullName());

        return toResponse(sessionRepository.save(session));
    }

    private AttendanceSession getActiveSession(Long id) {
        return sessionRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attendance session not found with id : " + id));
    }


    private AttendanceSession getFacultySession(Long id) {
        AttendanceSession session = getActiveSession(id);
        Faculty faculty = getCurrentFaculty();
        FacultySubjectMapping mapping = session.getFacultySubjectMapping();
        if (mapping == null || mapping.getFaculty() == null
                || !faculty.getId().equals(mapping.getFaculty().getId())) {
            throw new ResourceNotFoundException("Attendance session is not assigned to the current faculty.");
        }
        return session;
    }

    private Faculty getCurrentFaculty() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("Logged-in faculty information not found.");
        }
        return facultyRepository.findByUserIdAndStatus(userId, RecordStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty account not found."));
    }

    private void validateSessionDate(
            LocalDate date,
            FacultySubjectMapping mapping,
            AttendanceSource source) {

        if (date == null) {
            throw new BusinessException("Attendance date is required.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new BusinessException("Attendance cannot be marked for future date.");
        }
        if (date.isBefore(LocalDate.now()) && source != AttendanceSource.MANUAL_BACKDATE) {
            throw new BusinessException("Past-date attendance must use MANUAL_BACKDATE source.");
        }
        if (mapping.getAssignedDate() != null && date.isBefore(mapping.getAssignedDate())) {
            throw new BusinessException("Attendance cannot be before faculty assignment date.");
        }

        Subject subject = mapping.getSubject();
        if (subject == null || subject.getSemester() == null) {
            throw new BusinessException("Semester information not found for subject.");
        }

        Semester semester = subject.getSemester();
        if (semester.getSemesterStartDate() == null || semester.getSemesterEndDate() == null) {
            throw new BusinessException("Semester date information missing.");
        }
        if (date.isBefore(semester.getSemesterStartDate())) {
            throw new BusinessException("Attendance cannot be before semester start date.");
        }
        if (date.isAfter(semester.getSemesterEndDate())) {
            throw new BusinessException("Attendance cannot be after semester end date.");
        }
    }

    private AttendanceSessionResponseDto toResponse(AttendanceSession session) {
        FacultySubjectMapping mapping = session.getFacultySubjectMapping();
        Faculty faculty = mapping != null ? mapping.getFaculty() : null;
        Subject subject = mapping != null ? mapping.getSubject() : null;
        Semester semester = subject != null ? subject.getSemester() : null;
        Course course = semester != null ? semester.getCourse() : null;

        AttendanceSessionResponseDto dto = new AttendanceSessionResponseDto();
        dto.setId(session.getId());
        dto.setFacultySubjectMappingId(mapping != null ? mapping.getId() : null);
        dto.setFacultyId(faculty != null ? faculty.getId() : null);
        dto.setFacultyName(faculty != null ? faculty.getFirstName() + " " + faculty.getLastName() : null);
        dto.setSubjectId(subject != null ? subject.getId() : null);
        dto.setSubjectCode(subject != null ? subject.getSubjectCode() : null);
        dto.setSubjectName(subject != null ? subject.getSubjectName() : null);
        dto.setSemesterId(semester != null ? semester.getId() : null);
        dto.setSemesterName(semester != null ? semester.getSemesterName() : null);
        dto.setAttendanceDate(session.getAttendanceDate());
        dto.setStartedAt(session.getStartedAt());
        dto.setCompletedAt(session.getCompletedAt());
        dto.setAttendanceSource(session.getAttendanceSource());
        dto.setSessionStatus(session.getSessionStatus());
        dto.setStatus(session.getStatus());

        List<Attendance> records = attendanceRepository.findByAttendanceSessionId(
                session.getId(), RecordStatus.DELETED);
        int totalStudents = records.size();
        if (semester != null && course != null) {
            totalStudents = studentRepository.findEligibleStudentsForAttendance(
                    semester.getId(), course.getId(), RecordStatus.ACTIVE).size();
        }
        dto.setTotalStudents(totalStudents);
        dto.setMarkedStudents(records.size());
        dto.setPresentCount((int) records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.PRESENT).count());
        dto.setAbsentCount((int) records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.ABSENT).count());
        dto.setHalfDayCount((int) records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.HALF_DAY).count());
        dto.setLeaveCount((int) records.stream().filter(a -> a.getAttendanceStatus() == AttendanceStatus.LEAVE).count());
        return dto;
    }
}
