package com.admission.specification;

import org.springframework.data.jpa.domain.Specification;

import com.admission.entity.Admission;
import com.admission.enums.AdmissionStatus;
import com.sms.entity.Course;
import com.sms.entity.Department;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;

import jakarta.persistence.criteria.Join;

public class AdmissionSpecification {

    private AdmissionSpecification() {
    }

    // ==========================================================
    // Admission Number
    // ==========================================================

    public static Specification<Admission> hasAdmissionNumber(
            String admissionNumber) {

        return (root, query, cb) ->

                admissionNumber == null
                        || admissionNumber.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(
                                        root.get("admissionNumber")
                                ),
                                "%" +
                                        admissionNumber.toLowerCase() +
                                        "%"
                        );
    }

    // ==========================================================
    // Student ID
    // ==========================================================

    public static Specification<Admission> hasStudentId(
            Long studentId) {

        return (root, query, cb) ->

                studentId == null
                        ? null
                        : cb.equal(
                                root.get("student").get("id"),
                                studentId
                        );
    }

    // ==========================================================
    // Student Name
    // ==========================================================

    public static Specification<Admission> hasStudentName(
            String studentName) {

        return (root, query, cb) -> {

            if (studentName == null
                    || studentName.isBlank()) {

                return null;
            }

            Join<Admission, Student> student =
                    root.join("student");

            String keyword =
                    "%" +
                            studentName.toLowerCase() +
                            "%";

            return cb.or(

                    cb.like(
                            cb.lower(
                                    student.get("firstName")
                            ),
                            keyword
                    ),

                    cb.like(
                            cb.lower(
                                    student.get("lastName")
                            ),
                            keyword
                    )
            );
        };
    }

    // ==========================================================
    // Department
    // ==========================================================

    public static Specification<Admission> hasDepartmentId(
            Long departmentId) {

        return (root, query, cb) -> {

            if (departmentId == null) {
                return null;
            }

            Join<Admission, Department> department =
                    root.join("department");

            return cb.equal(
                    department.get("id"),
                    departmentId
            );
        };
    }

    // ==========================================================
    // Course
    // ==========================================================

    public static Specification<Admission> hasCourseId(
            Long courseId) {

        return (root, query, cb) -> {

            if (courseId == null) {
                return null;
            }

            Join<Admission, Course> course =
                    root.join("course");

            return cb.equal(
                    course.get("id"),
                    courseId
            );
        };
    }

    // ==========================================================
    // Academic Year
    // ==========================================================

    public static Specification<Admission> hasAcademicYear(
            String academicYear) {

        return (root, query, cb) ->

                academicYear == null
                        || academicYear.isBlank()
                        ? null
                        : cb.equal(
                                cb.lower(
                                        root.get("academicYear")
                                ),
                                academicYear.toLowerCase()
                        );
    }

    // ==========================================================
    // Semester
    // ==========================================================

    public static Specification<Admission> hasSemester(
            Long semesterId) {

        return (root, query, cb) ->

                semesterId == null
                        ? null
                        : cb.equal(
                                root.get("semester").get("id"),
                                semesterId
                        );
    }

    // ==========================================================
    // Admission Status
    // ==========================================================

    public static Specification<Admission> hasStatus(
            AdmissionStatus admissionStatus) {

        return (root, query, cb) ->

                admissionStatus == null
                        ? null
                        : cb.equal(
                                root.get("admissionStatus"),
                                admissionStatus
                        );
    }

    // ==========================================================
    // Record Status
    // ==========================================================

    public static Specification<Admission> hasRecordStatus(
            RecordStatus status) {

        return (root, query, cb) ->

                status == null
                        ? null
                        : cb.equal(
                                root.get("status"),
                                status
                        );
    }
}
