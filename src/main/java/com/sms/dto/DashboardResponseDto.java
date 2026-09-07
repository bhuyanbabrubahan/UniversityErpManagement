package com.sms.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "DashboardResponseDto",
        description = "Dashboard summary information."
)
public class DashboardResponseDto {
	
	

    // ==========================
    // Student Statistics
    // ==========================

    private Long totalStudents;
    private Long activeStudents;
    private Long inactiveStudents;


    // ==========================
    // Faculty Statistics
    // ==========================

    private Long totalFaculties;
    private Long activeFaculties;
    private Long inactiveFaculties;


    // ==========================
    // Admin Statistics
    // ==========================

    private Long totalAdmins;
    private Long totalSuperAdmins;


    // ==========================
    // Academic Statistics
    // ==========================

    private Long totalDepartments;
    private Long totalCourses;
    private Long totalSemesters;
    private Long totalSubjects;
    private Long totalFacultySubjects;


    // ==========================
    // Admission Statistics
    // ==========================

    private Long totalAdmissions;
    private Long activeAdmissions;
    private Long cancelledAdmissions;
    private Long completedAdmissions;


    // ==========================
    // Attendance Statistics
    // ==========================

    private Long totalAttendance;
    private Long todayAttendance;

    private Long presentStudents;
    private Long absentStudents;
    private Long leaveStudents;


    // ==========================
    // Exam Statistics
    // ==========================

    private Long totalExams;
    private Long upcomingExams;
    private Long completedExams;


    // ==========================
    // Result Statistics
    // ==========================

    private Long totalResults;
    private Long passResults;
    private Long failResults;

    private BigDecimal averagePercentage;



    // ==========================
    // Fee Statistics
    // ==========================

    private BigDecimal totalFeeCollection;

    private BigDecimal todayCollection;

    private BigDecimal monthlyCollection;

    private BigDecimal pendingDue;

    private BigDecimal totalPayableFee;

    private BigDecimal collectionPercentage;

    private BigDecimal averageCollection;


    // ==========================
    // Payment Statistics
    // ==========================

    private Long totalPayments;

    private Long successfulPayments;

    private Long failedPayments;


    // ==========================
    // Receipt Statistics
    // ==========================

    private Long totalReceipts;


    // ==========================
    // Refund Statistics
    // ==========================

    private BigDecimal totalRefundAmount;

    private Long totalRefunds;
    
 // ==========================
 // Faculty Dashboard
 // ==========================

 private Long mySubjects;
 private Long myStudents;
 private Long todayAttendanceByFaculty;
 private Long myExams;
 private Long myResults;


 // ==========================
 // Student Dashboard
 // ==========================

 private BigDecimal studentTotalFee;
 private BigDecimal studentPaidFee;
 private BigDecimal studentPendingFee;

 private Long studentAttendance;
 private Long studentResults;
 private Long studentExams;

}