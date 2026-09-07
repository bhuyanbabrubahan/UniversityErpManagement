export interface DashboardResponse {

  // ==========================
  // Student
  // ==========================

  totalStudents:number;
  activeStudents:number;
  inactiveStudents:number;


  // ==========================
  // Faculty
  // ==========================

  totalFaculties:number;
  activeFaculties:number;
  inactiveFaculties:number;


  // ==========================
  // Admin
  // ==========================

  totalAdmins:number;
  totalSuperAdmins:number;


  // ==========================
  // Academic
  // ==========================

  totalDepartments:number;
  totalCourses:number;
  totalSemesters:number;
  totalSubjects:number;
  totalFacultySubjects:number;


  // ==========================
  // Admission
  // ==========================

  totalAdmissions:number;
  activeAdmissions:number;
  cancelledAdmissions:number;
  completedAdmissions:number;


  // ==========================
  // Attendance
  // ==========================

  totalAttendance:number;
  todayAttendance:number;
  presentStudents:number;
  absentStudents:number;
  leaveStudents:number;


  // ==========================
  // Exams
  // ==========================

  totalExams:number;
  upcomingExams:number;
  completedExams:number;


  // ==========================
  // Results
  // ==========================

  totalResults:number;
  passResults:number;
  failResults:number;
  averagePercentage:number;


  // ==========================
  // Fee Dashboard
  // ==========================

  totalFeeCollection:number;
  todayCollection:number;
  monthlyCollection:number;
  pendingDue:number;
  totalPayableFee:number;
  collectionPercentage:number;
  averageCollection:number;


  // ==========================
  // Payments
  // ==========================

  totalPayments:number;
  successfulPayments:number;
  failedPayments:number;


  // ==========================
  // Receipts
  // ==========================

  totalReceipts:number;


  // ==========================
  // Refunds
  // ==========================

  totalRefundAmount:number;
  totalRefunds:number;


  // ==========================
  // Faculty Dashboard
  // ==========================

  mySubjects:number;
  myStudents:number;
  todayAttendanceByFaculty:number;
  myExams:number;
  myResults:number;


  // ==========================
  // Student Dashboard
  // ==========================

  studentTotalFee:number;
  studentPaidFee:number;
  studentPendingFee:number;

  studentAttendance:number;
  studentResults:number;
  studentExams:number;

}