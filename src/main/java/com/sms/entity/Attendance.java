package com.sms.entity;

import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.AttendanceStatus;
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
    name = "attendance",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_attendance_session_student",
            columnNames = {
                "attendance_session_id",
                "student_id"
            }
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================
    // Attendance Session
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attendance_session_id", nullable = false)
    @ToString.Exclude
    private AttendanceSession attendanceSession;

    // ==========================
    // Student
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    // ==========================
    // Attendance Status
    // ==========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus attendanceStatus;

    // ==========================
    // Remarks
    // ==========================

    @Column(length = 300)
    private String remarks;

    // ==========================
    // Marked Time
    // ==========================

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    // ==========================
    // Record Status
    // ==========================

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RecordStatus status;
}
