package com.sms.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.dto.attendance.AttendanceClassRequestDto;
import com.sms.dto.attendance.AttendanceClassResponseDto;
import com.sms.dto.attendance.AttendanceRequestDto;
import com.sms.dto.attendance.AttendanceResponseDto;
import com.sms.dto.attendance.AttendanceSearchRequest;
import com.sms.dto.attendance.AttendanceStudentDto;
import com.sms.dto.attendance.BulkAttendanceItemDto;
import com.sms.dto.attendance.BulkAttendanceRequestDto;
import com.sms.entity.Attendance;
import com.sms.entity.AttendanceSession;
import com.sms.entity.Course;
import com.sms.entity.Faculty;
import com.sms.entity.FacultySubjectMapping;
import com.sms.entity.Semester;
import com.sms.entity.Student;
import com.sms.entity.Subject;
import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.RecordStatus;
import com.sms.enums.StudentStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.AttendanceMapper;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.AttendanceSessionRepository;
import com.sms.repository.FacultyRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.AttendanceService;
import com.sms.specification.AttendanceSpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repository;
    private final AttendanceSessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AttendanceMapper mapper;

    @Override
    @Transactional
    @AuditLog(action = AuditAction.CREATE, module = "ATTENDANCE", description = "Mark student attendance")
    public AttendanceResponseDto createAttendance(AttendanceRequestDto dto) {

        AttendanceSession session = getFacultySession(dto.getAttendanceSessionId());
        validateSessionForModification(session);

        Student student = getActiveStudent(dto.getStudentId());
        validateStudentEligibility(student, session.getFacultySubjectMapping());

        Attendance existing = repository.findByAttendanceSessionIdAndStudentId(
                session.getId(), student.getId()).orElse(null);

        if (existing != null && existing.getStatus() != RecordStatus.DELETED) {
            throw new BusinessException("Attendance already marked for this student in this session.");
        }

        Attendance attendance = existing != null ? existing : mapper.toEntity(dto);
        attendance.setAttendanceSession(session);
        attendance.setStudent(student);
        attendance.setAttendanceStatus(dto.getAttendanceStatus());
        attendance.setRemarks(normalizeRemarks(dto.getRemarks()));
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setStatus(RecordStatus.ACTIVE);

        if (existing == null) {
            attendance.setCreatedByUserId(SecurityUtils.getCurrentUserId());
            attendance.setCreatedBy(SecurityUtils.getCurrentFullName());
            attendance.setCreatedAt(LocalDateTime.now());
        } else {
            attendance.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
            attendance.setUpdatedBy(SecurityUtils.getCurrentFullName());
            attendance.setUpdatedAt(LocalDateTime.now());
        }

        return mapper.toDto(repository.save(attendance));
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponseDto getAttendanceById(Long id) {
        Attendance attendance = repository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id : " + id));

        assertFacultyCanAccess(attendance.getAttendanceSession());
        return mapper.toDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AttendanceResponseDto> getAllAttendance(
            int page, int size, String sortBy, String direction) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                buildSort(sortBy, direction));

        Specification<Attendance> spec =
                AttendanceSpecification.statusNot(RecordStatus.DELETED);

        if ("FACULTY".equals(SecurityUtils.getCurrentRole())) {
            Faculty faculty = getCurrentFaculty();
            spec = spec.and(AttendanceSpecification.hasFaculty(faculty.getId()));
        }

        Page<Attendance> result = repository.findAll(spec, pageable);
        return toPageResponse(result);
    }

    @Override
    @Transactional
    @AuditLog(action = AuditAction.UPDATE, module = "ATTENDANCE", description = "Update student attendance")
    public AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto dto) {

        Attendance attendance = repository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id : " + id));

        AttendanceSession session = attendance.getAttendanceSession();
        assertFacultyCanAccess(session);
        validateSessionForModification(session);

        if (!session.getId().equals(dto.getAttendanceSessionId())) {
            throw new BusinessException("Attendance session cannot be changed during attendance update.");
        }
        if (!attendance.getStudent().getId().equals(dto.getStudentId())) {
            throw new BusinessException("Student cannot be changed during attendance update.");
        }

        mapper.updateEntity(attendance, dto);
        attendance.setAttendanceStatus(dto.getAttendanceStatus());
        attendance.setRemarks(normalizeRemarks(dto.getRemarks()));
        attendance.setMarkedAt(LocalDateTime.now());
        attendance.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        attendance.setUpdatedBy(SecurityUtils.getCurrentFullName());
        attendance.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(repository.save(attendance));
    }

    @Override
    @Transactional
    @AuditLog(action = AuditAction.DELETE, module = "ATTENDANCE", description = "Delete student attendance")
    public void deleteAttendance(Long id) {
        Attendance attendance = repository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id : " + id));

        assertFacultyCanAccess(attendance.getAttendanceSession());
        validateSessionForModification(attendance.getAttendanceSession());
        attendance.setStatus(RecordStatus.DELETED);
        attendance.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
        attendance.setUpdatedBy(SecurityUtils.getCurrentFullName());
        attendance.setUpdatedAt(LocalDateTime.now());
        repository.save(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AttendanceResponseDto> searchAttendance(
            AttendanceSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction) {

        Specification<Attendance> spec = Specification.where(null);

        if ("FACULTY".equals(SecurityUtils.getCurrentRole())) {
            Faculty faculty = getCurrentFaculty();
            spec = spec.and(AttendanceSpecification.hasFaculty(faculty.getId()));
        }

        spec = spec.and(request.getStatus() != null
                ? AttendanceSpecification.hasStatus(request.getStatus())
                : AttendanceSpecification.statusNot(RecordStatus.DELETED));

        if (request.getAttendanceSessionId() != null) {
            spec = spec.and(AttendanceSpecification.hasSession(request.getAttendanceSessionId()));
        }
        if (request.getStudentId() != null) {
            spec = spec.and(AttendanceSpecification.hasStudent(request.getStudentId()));
        }
        if (request.getFacultySubjectMappingId() != null) {
            spec = spec.and(AttendanceSpecification.hasFacultySubject(request.getFacultySubjectMappingId()));
        }
        if (request.getFacultyId() != null) {
            spec = spec.and(AttendanceSpecification.hasFaculty(request.getFacultyId()));
        }
        if (request.getSubjectId() != null) {
            spec = spec.and(AttendanceSpecification.hasSubject(request.getSubjectId()));
        }
        if (request.getAttendanceDate() != null) {
            spec = spec.and(AttendanceSpecification.hasDate(request.getAttendanceDate()));
        }
        if (request.getAttendanceStatus() != null) {
            spec = spec.and(AttendanceSpecification.hasAttendanceStatus(request.getAttendanceStatus()));
        }
        if (request.getSessionStatus() != null) {
            spec = spec.and(AttendanceSpecification.hasSessionStatus(request.getSessionStatus()));
        }

        Pageable pageable = PageRequest.of(
                Math.max(page, 0), Math.max(size, 1), buildSort(sortBy, direction));

        return toPageResponse(repository.findAll(spec, pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceClassResponseDto getClassAttendance(AttendanceClassRequestDto request) {

        AttendanceSession session = getFacultySession(request.getAttendanceSessionId());
        FacultySubjectMapping mapping = session.getFacultySubjectMapping();
        Subject subject = mapping.getSubject();

        if (subject == null || subject.getSemester() == null) {
            throw new BusinessException("Subject semester information not found.");
        }

        Semester semester = subject.getSemester();
        Course course = semester.getCourse();
        if (course == null) {
            throw new BusinessException("Course information not found.");
        }

        List<Student> students = studentRepository.findEligibleStudentsForAttendance(
                semester.getId(), course.getId(), RecordStatus.ACTIVE);

        List<Attendance> existing = repository.findByAttendanceSessionId(
                session.getId(), RecordStatus.DELETED);

        Map<Long, Attendance> attendanceMap = existing.stream()
                .collect(Collectors.toMap(a -> a.getStudent().getId(), Function.identity()));

        List<AttendanceStudentDto> studentDtos = students.stream()
                .map(student -> toStudentDto(student, attendanceMap.get(student.getId())))
                .toList();

        Faculty faculty = mapping.getFaculty();
        AttendanceClassResponseDto response = new AttendanceClassResponseDto();
        response.setAttendanceSessionId(session.getId());
        response.setSessionStatus(session.getSessionStatus());
        response.setAttendanceSource(session.getAttendanceSource());
        response.setFacultySubjectMappingId(mapping.getId());
        response.setFacultyId(faculty != null ? faculty.getId() : null);
        response.setFacultyName(faculty != null ? faculty.getFullName() : null);
        response.setSubjectId(subject.getId());
        response.setSubjectCode(subject.getSubjectCode());
        response.setSubjectName(subject.getSubjectName());
        response.setSemesterId(semester.getId());
        response.setSemesterName(semester.getSemesterName());
        response.setDepartmentId(students.isEmpty() || students.get(0).getDepartment() == null
                ? null : students.get(0).getDepartment().getId());
        response.setDepartmentName(students.isEmpty() || students.get(0).getDepartment() == null
                ? null : students.get(0).getDepartment().getDepartmentName());
        response.setAttendanceDate(session.getAttendanceDate());
        response.setAttendanceOpenedAt(session.getStartedAt());
        response.setStudents(studentDtos);
        return response;
    }

    @Override
    @Transactional
    @AuditLog(action = AuditAction.CREATE, module = "ATTENDANCE", description = "Bulk mark student attendance")
    public List<AttendanceResponseDto> saveBulkAttendance(BulkAttendanceRequestDto request) {

        AttendanceSession session = getFacultySession(request.getAttendanceSessionId());
        validateSessionForModification(session);

        if (request.getAttendanceRecords() == null || request.getAttendanceRecords().isEmpty()) {
            throw new BusinessException("Attendance records cannot be empty.");
        }

        Set<Long> studentIds = request.getAttendanceRecords().stream()
                .map(BulkAttendanceItemDto::getStudentId)
                .collect(Collectors.toSet());

        if (studentIds.size() != request.getAttendanceRecords().size()) {
            throw new BusinessException("Duplicate student found in attendance request.");
        }

        List<AttendanceResponseDto> response = new ArrayList<>();

        for (BulkAttendanceItemDto item : request.getAttendanceRecords()) {
            Student student = getActiveStudent(item.getStudentId());
            validateStudentEligibility(student, session.getFacultySubjectMapping());

            Attendance attendance = repository.findByAttendanceSessionIdAndStudentId(
                    session.getId(), student.getId()).orElse(null);

            boolean create = attendance == null;
            if (create) {
                attendance = new Attendance();
                attendance.setAttendanceSession(session);
                attendance.setStudent(student);
                attendance.setCreatedByUserId(SecurityUtils.getCurrentUserId());
                attendance.setCreatedBy(SecurityUtils.getCurrentFullName());
                attendance.setCreatedAt(LocalDateTime.now());
            } else {
                attendance.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
                attendance.setUpdatedBy(SecurityUtils.getCurrentFullName());
                attendance.setUpdatedAt(LocalDateTime.now());
            }

            attendance.setAttendanceStatus(item.getAttendanceStatus());
            attendance.setRemarks(normalizeRemarks(item.getRemarks()));
            attendance.setMarkedAt(LocalDateTime.now());
            attendance.setStatus(RecordStatus.ACTIVE);

            response.add(mapper.toDto(repository.save(attendance)));
        }

        return response;
    }

    private AttendanceSession getFacultySession(Long sessionId) {

        if (!"FACULTY".equals(SecurityUtils.getCurrentRole())) {
            throw new BusinessException("Only faculty can modify attendance.");
        }

        AttendanceSession session = sessionRepository.findByIdAndStatusNot(
                sessionId, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attendance session not found with id : " + sessionId));

        assertFacultyCanAccess(session);
        return session;
    }

    private Faculty getCurrentFaculty() {
        Long userId = SecurityUtils.getCurrentUserId();

        if (userId == null) {
            throw new BusinessException("Authenticated user not found.");
        }

        return facultyRepository.findByUserIdAndStatus(
                userId, RecordStatus.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Faculty account not found for authenticated user."));
    }

    private void assertFacultyCanAccess(AttendanceSession session) {
        String role = SecurityUtils.getCurrentRole();

        if ("ADMIN".equals(role) || "SUPER_ADMIN".equals(role)) {
            return;
        }

        if (!"FACULTY".equals(role)) {
            return;
        }

        Faculty faculty = getCurrentFaculty();

        if (session == null
                || session.getFacultySubjectMapping() == null
                || session.getFacultySubjectMapping().getFaculty() == null
                || !faculty.getId().equals(
                        session.getFacultySubjectMapping().getFaculty().getId())) {
            throw new ResourceNotFoundException(
                    "Attendance record is not assigned to the current faculty.");
        }
    }

    private Student getActiveStudent(Long id) {
        return studentRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id : " + id));
    }

    private void validateSessionForModification(AttendanceSession session) {
        if (session == null) {
            throw new BusinessException("Attendance session is required.");
        }
        if (session.getSessionStatus() == AttendanceSessionStatus.COMPLETED) {
            throw new BusinessException("Attendance session is already completed. Modification is not allowed.");
        }
        if (session.getSessionStatus() == AttendanceSessionStatus.CANCELLED) {
            throw new BusinessException("Attendance session is cancelled.");
        }
        if (session.getSessionStatus() != AttendanceSessionStatus.OPEN) {
            throw new BusinessException("Attendance session is not open.");
        }
    }

    private void validateStudentEligibility(Student student, FacultySubjectMapping mapping) {
        if (student.getStatus() == RecordStatus.DELETED) {
            throw new BusinessException("Student is deleted.");
        }
        if (student.getStudentStatus() != StudentStatus.ACTIVE) {
            throw new BusinessException("Student is not active: " + student.getRollNumber());
        }

        Subject subject = mapping.getSubject();
        if (subject == null || subject.getSemester() == null) {
            throw new BusinessException("Subject semester information not found.");
        }

        Semester semester = subject.getSemester();
        Course course = semester.getCourse();
        if (course == null) {
            throw new BusinessException("Course information not found.");
        }
        if (student.getCurrentSemester() == null
                || !student.getCurrentSemester().getId().equals(semester.getId())) {
            throw new BusinessException("Student " + student.getRollNumber()
                    + " does not belong to this semester.");
        }
        if (student.getCourse() == null
                || !student.getCourse().getId().equals(course.getId())) {
            throw new BusinessException("Student " + student.getRollNumber()
                    + " does not belong to this course.");
        }
    }

    private AttendanceStudentDto toStudentDto(Student student, Attendance attendance) {
        AttendanceStudentDto dto = new AttendanceStudentDto();
        dto.setStudentId(student.getId());
        dto.setStudentCode(student.getStudentCode());
        dto.setRollNumber(student.getRollNumber());
        dto.setStudentName(student.getFullName());
        if (student.getDepartment() != null) {
            dto.setDepartmentName(student.getDepartment().getDepartmentName());
        }
        if (student.getCourse() != null) {
            dto.setCourseName(student.getCourse().getCourseName());
        }
        if (student.getCurrentSemester() != null) {
            dto.setSemesterName(student.getCurrentSemester().getSemesterName());
        }
        if (attendance != null) {
            dto.setAttendanceStatus(attendance.getAttendanceStatus());
            dto.setRemarks(attendance.getRemarks());
        }
        return dto;
    }

    private String normalizeRemarks(String remarks) {
        return remarks == null ? null : remarks.trim();
    }

    private Sort buildSort(String sortBy, String direction) {
        String field = switch (sortBy == null ? "id" : sortBy) {
            case "attendanceDate" -> "attendanceSession.attendanceDate";
            case "sessionStatus" -> "attendanceSession.sessionStatus";
            case "facultySubjectMappingId" -> "attendanceSession.facultySubjectMapping.id";
            case "facultyId" -> "attendanceSession.facultySubjectMapping.faculty.id";
            case "subjectId" -> "attendanceSession.facultySubjectMapping.subject.id";
            case "studentId" -> "student.id";
            case "attendanceStatus" -> "attendanceStatus";
            case "markedAt" -> "markedAt";
            default -> "id";
        };
        return "asc".equalsIgnoreCase(direction)
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
    }

    private <T> PageResponse<AttendanceResponseDto> toPageResponse(Page<Attendance> result) {
        List<AttendanceResponseDto> content = result.getContent().stream()
                .map(mapper::toDto)
                .toList();

        PageResponse<AttendanceResponseDto> response = new PageResponse<>();
        response.setContent(content);
        response.setPageNumber(result.getNumber());
        response.setPageSize(result.getSize());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        response.setLast(result.isLast());
        return response;
    }
}
