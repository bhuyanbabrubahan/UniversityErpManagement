package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.attendance.AttendanceRequestDto;
import com.sms.dto.attendance.AttendanceResponseDto;
import com.sms.entity.Attendance;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    // =====================================================
    // DTO -> ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

    // Set manually in service
    @Mapping(target = "attendanceSession", ignore = true)
    @Mapping(target = "student", ignore = true)

    // System managed fields
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "markedAt", ignore = true)

    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    Attendance toEntity(AttendanceRequestDto dto);


    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    @Mapping(
        source = "attendanceSession.id",
        target = "attendanceSessionId"
    )

    @Mapping(
        source = "attendanceSession.sessionStatus",
        target = "sessionStatus"
    )

    @Mapping(
        source = "attendanceSession.facultySubjectMapping.id",
        target = "facultySubjectMappingId"
    )

    @Mapping(
        source = "attendanceSession.facultySubjectMapping.faculty.id",
        target = "facultyId"
    )

    @Mapping(
        expression = "java(getFacultyName(attendance))",
        target = "facultyName"
    )

    @Mapping(
        source = "attendanceSession.facultySubjectMapping.subject.id",
        target = "subjectId"
    )

    @Mapping(
        source = "attendanceSession.facultySubjectMapping.subject.subjectCode",
        target = "subjectCode"
    )

    @Mapping(
        source = "attendanceSession.facultySubjectMapping.subject.subjectName",
        target = "subjectName"
    )

    @Mapping(
        source = "attendanceSession.attendanceDate",
        target = "attendanceDate"
    )

    @Mapping(
        source = "attendanceSession.attendanceSource",
        target = "attendanceSource"
    )

    @Mapping(
        source = "student.id",
        target = "studentId"
    )

    @Mapping(
        source = "student.rollNumber",
        target = "rollNumber"
    )

    @Mapping(
        expression = "java(getStudentName(attendance))",
        target = "studentName"
    )

    AttendanceResponseDto toDto(Attendance attendance);


    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @BeanMapping(
        nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)

    // Session cannot be changed during attendance update
    @Mapping(target = "attendanceSession", ignore = true)

    // Student cannot be changed during attendance update
    @Mapping(target = "student", ignore = true)

    // System managed fields
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "markedAt", ignore = true)

    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEntity(
        @MappingTarget Attendance attendance,
        AttendanceRequestDto dto
    );


    // =====================================================
    // HELPER METHODS
    // =====================================================

    default String getFacultyName(Attendance attendance) {

        if (attendance == null
                || attendance.getAttendanceSession() == null
                || attendance.getAttendanceSession()
                        .getFacultySubjectMapping() == null
                || attendance.getAttendanceSession()
                        .getFacultySubjectMapping()
                        .getFaculty() == null) {

            return null;
        }

        var faculty =
                attendance.getAttendanceSession()
                        .getFacultySubjectMapping()
                        .getFaculty();

        String firstName = faculty.getFirstName();
        String lastName = faculty.getLastName();

        if (firstName == null && lastName == null) {
            return null;
        }

        if (firstName == null) {
            return lastName;
        }

        if (lastName == null) {
            return firstName;
        }

        return firstName + " " + lastName;
    }


    default String getStudentName(Attendance attendance) {

        if (attendance == null
                || attendance.getStudent() == null) {

            return null;
        }

        var student = attendance.getStudent();

        String firstName = student.getFirstName();
        String lastName = student.getLastName();

        if (firstName == null && lastName == null) {
            return null;
        }

        if (firstName == null) {
            return lastName;
        }

        if (lastName == null) {
            return firstName;
        }

        return firstName + " " + lastName;
    }
}