package com.sms.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.entity.Attendance;
import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceStatus;
import com.sms.enums.RecordStatus;

public final class AttendanceSpecification {

    private AttendanceSpecification() {
    }

    public static Specification<Attendance> hasSession(Long sessionId) {
        return (root, query, cb) -> cb.equal(
                root.get("attendanceSession").get("id"), sessionId);
    }

    public static Specification<Attendance> hasStudent(Long studentId) {
        return (root, query, cb) -> cb.equal(
                root.get("student").get("id"), studentId);
    }

    public static Specification<Attendance> hasFacultySubject(Long mappingId) {
        return (root, query, cb) -> cb.equal(
                root.get("attendanceSession").get("facultySubjectMapping").get("id"), mappingId);
    }

    public static Specification<Attendance> hasFaculty(Long facultyId) {
        return (root, query, cb) -> cb.equal(
                root.get("attendanceSession").get("facultySubjectMapping").get("faculty").get("id"), facultyId);
    }

    public static Specification<Attendance> hasSubject(Long subjectId) {
        return (root, query, cb) -> cb.equal(
                root.get("attendanceSession").get("facultySubjectMapping").get("subject").get("id"), subjectId);
    }

    public static Specification<Attendance> hasDate(LocalDate date) {
        return (root, query, cb) -> cb.equal(
                root.get("attendanceSession").get("attendanceDate"), date);
    }

    public static Specification<Attendance> hasAttendanceStatus(AttendanceStatus status) {
        return (root, query, cb) -> cb.equal(root.get("attendanceStatus"), status);
    }

    public static Specification<Attendance> hasSessionStatus(AttendanceSessionStatus status) {
        return (root, query, cb) -> cb.equal(
                root.get("attendanceSession").get("sessionStatus"), status);
    }

    public static Specification<Attendance> hasStatus(RecordStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Attendance> statusNot(RecordStatus status) {
        return (root, query, cb) -> cb.notEqual(root.get("status"), status);
    }
}
