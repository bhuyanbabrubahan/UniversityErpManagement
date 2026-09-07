package com.sms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceSource;
import com.sms.enums.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "attendance_sessions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_attendance_session",
            columnNames = {
                "faculty_subject_mapping_id",
                "attendance_date"
            }
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // Faculty Subject Mapping
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_subject_mapping_id", nullable = false)
    @ToString.Exclude
    private FacultySubjectMapping facultySubjectMapping;

    // ==========================
    // Attendance Date
    // ==========================

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    // ==========================
    // Start Time
    // ==========================

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    // ==========================
    // Completed Time
    // ==========================

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // ==========================
    // Source
    // ==========================

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_source", nullable = false, length = 30)
    private AttendanceSource attendanceSource;

    // ==========================
    // Session Status
    // ==========================

    @Enumerated(EnumType.STRING)
    @Column(name = "session_status", nullable = false, length = 20)
    private AttendanceSessionStatus sessionStatus;

    // ==========================
    // Record Status
    // ==========================

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RecordStatus status;
}
