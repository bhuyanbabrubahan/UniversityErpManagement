package com.sms.dashboard.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admission.enums.AdmissionStatus;
import com.admission.repository.AdmissionRepository;
import com.sms.dashboard.service.DashboardService;
import com.sms.dto.DashboardResponseDto;
import com.sms.entity.Faculty;
import com.sms.entity.Student;
import com.sms.enums.AttendanceStatus;
import com.sms.enums.RecordStatus;
import com.sms.enums.ResultStatus;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.CourseRepository;
import com.sms.repository.DepartmentRepository;
import com.sms.repository.ExamRepository;
import com.sms.repository.FacultyRepository;
import com.sms.repository.FacultySubjectRepository;
import com.sms.repository.ResultRepository;
import com.sms.repository.SemesterRepository;
import com.sms.repository.StudentRepository;
import com.sms.repository.SubjectRepository;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.repository.AdminRepository;
import com.sms.security.repository.SuperAdminRepository;
import com.sms.security.repository.UserRepository;
import com.sms.service.FacultyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

	private final StudentRepository studentRepository;
	private final DepartmentRepository departmentRepository;
	private final CourseRepository courseRepository;
	private final SemesterRepository semesterRepository;
	private final SubjectRepository subjectRepository;
	private final FacultyRepository facultyRepository;
	private final FacultySubjectRepository facultySubjectRepository;
	private final AdmissionRepository admissionRepository;
	private final AttendanceRepository attendanceRepository;
	private final ExamRepository examRepository;
	private final ResultRepository resultRepository;
	private final StudentFeePaymentRepository paymentRepository;
	private final AdminRepository adminRepository;
	private final SuperAdminRepository superAdminRepository;
	private final StudentFeeReceiptRepository receiptRepository;
	private final StudentFeeRefundRepository refundRepository;
	// Role Based Dashboard 
	private final UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(FacultyService.class);
	
	
	
	@Override
	public DashboardResponseDto getDashboard(
	        Authentication authentication
	) {

	    User user =
	            userRepository.findByEmail(authentication.getName())
	                    .orElseThrow(
	                            () -> new RuntimeException("User not found")
	                    );


	    Role role = user.getRole();


	    return switch (role) {

	        case ADMIN, SUPER_ADMIN ->
	                getAdminDashboard();


	        case FACULTY ->
	                getFacultyDashboard(user);


	        case STUDENT ->
                getStudentDashboard(user);

        // Management/read-only operational roles use the same aggregate
        // query set but their Angular workspace exposes only permitted cards.
        case ACCOUNTANT, VC, REGISTRAR, PRINCIPAL, DEAN, HOD,
             ADMISSION_OFFICER, EXAM_CONTROLLER, LIBRARIAN,
             HOSTEL_WARDEN, TRANSPORT_MANAGER, PLACEMENT_OFFICER, HR,
             STAFF, PARENT, ALUMNI ->
                getAdminDashboard();

        default ->
                throw new RuntimeException(
                        "Dashboard not available for role : " + role
                );
	    };
	}
	
	
	private DashboardResponseDto getAdminDashboard() {


	    DashboardResponseDto dto = new DashboardResponseDto();


	    LocalDate today = LocalDate.now();


	    // ==========================================================
	    // Student Statistics
	    // ==========================================================

	    dto.setTotalStudents(
	            studentRepository.count()
	    );


	    dto.setActiveStudents(
	            studentRepository.countByStatus(
	                    RecordStatus.ACTIVE
	            )
	    );


	    dto.setInactiveStudents(
	            studentRepository.countByStatus(
	                    RecordStatus.INACTIVE
	            )
	    );


	    // ==========================================================
	    // Department Statistics
	    // ==========================================================

	    dto.setTotalDepartments(
	            departmentRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    // ==========================================================
	    // Course Statistics
	    // ==========================================================

	    dto.setTotalCourses(
	            courseRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    // ==========================================================
	    // Semester Statistics
	    // ==========================================================

	    dto.setTotalSemesters(
	            semesterRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    // ==========================================================
	    // Subject Statistics
	    // ==========================================================

	    dto.setTotalSubjects(
	            subjectRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    // ==========================================================
	    // Faculty Statistics
	    // ==========================================================

	    dto.setTotalFaculties(
	            facultyRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );

	    dto.setActiveFaculties(
	            facultyRepository.countByStatus(
	                    RecordStatus.ACTIVE
	            )
	    );


	    dto.setInactiveFaculties(
	            facultyRepository.countByStatus(
	                    RecordStatus.INACTIVE
	            )
	    );

	    // ==========================================================
	    // Faculty Subject Mapping Statistics
	    // ==========================================================

	    dto.setTotalFacultySubjects(
	            facultySubjectRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	 // ==========================================================
	 // Admin Statistics
	 // ==========================================================

	 dto.setTotalAdmins(
	         adminRepository.countByStatusNot(
	                 RecordStatus.DELETED
	         )
	 );


	 dto.setTotalSuperAdmins(
	         superAdminRepository.countByStatusNot(
	                 RecordStatus.DELETED
	         )
	 );
	    
	    // ==========================================================
	    // Admission Statistics
	    // ==========================================================

	    dto.setTotalAdmissions(
	            admissionRepository.count()
	    );


	    dto.setActiveAdmissions(
	            admissionRepository.countByAdmissionStatus(
	                    AdmissionStatus.ACTIVE
	            )
	    );


	    dto.setCancelledAdmissions(
	            admissionRepository.countByAdmissionStatus(
	                    AdmissionStatus.CANCELLED
	            )
	    );


	    dto.setCompletedAdmissions(
	            admissionRepository.countByAdmissionStatus(
	                    AdmissionStatus.COMPLETED
	            )
	    );


	    // ==========================================================
	    // Attendance Statistics
	    // ==========================================================

	    dto.setTotalAttendance(
	            attendanceRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    dto.setTodayAttendance(
	            attendanceRepository
	                    .countByAttendanceDateAndStatusNot(
	                            today,
	                            RecordStatus.DELETED
	                    )
	    );


	    dto.setPresentStudents(
	            attendanceRepository
	                    .countByAttendanceStatusAndAttendanceDateAndStatusNot(
	                            AttendanceStatus.PRESENT,
	                            today,
	                            RecordStatus.DELETED
	                    )
	    );


	    dto.setAbsentStudents(
	            attendanceRepository
	                    .countByAttendanceStatusAndAttendanceDateAndStatusNot(
	                            AttendanceStatus.ABSENT,
	                            today,
	                            RecordStatus.DELETED
	                    )
	    );


	    dto.setLeaveStudents(
	            attendanceRepository
	                    .countByAttendanceStatusAndAttendanceDateAndStatusNot(
	                            AttendanceStatus.LEAVE,
	                            today,
	                            RecordStatus.DELETED
	                    )
	    );


	    // ==========================================================
	    // Exam Statistics
	    // ==========================================================

	    dto.setTotalExams(
	            examRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    dto.setUpcomingExams(
	            examRepository.countByExamDateAfterAndStatusNot(
	                    today,
	                    RecordStatus.DELETED
	            )
	    );


	    dto.setCompletedExams(
	            examRepository.countByExamDateBeforeAndStatusNot(
	                    today,
	                    RecordStatus.DELETED
	            )
	    );


	    // ==========================================================
	    // Result Statistics
	    // ==========================================================

	    dto.setTotalResults(
	            resultRepository.countByStatusNot(
	                    RecordStatus.DELETED
	            )
	    );


	    dto.setPassResults(
	            resultRepository
	                    .countByResultStatusAndStatusNot(
	                            ResultStatus.PASS,
	                            RecordStatus.DELETED
	                    )
	    );


	    dto.setFailResults(
	            resultRepository
	                    .countByResultStatusAndStatusNot(
	                            ResultStatus.FAIL,
	                            RecordStatus.DELETED
	                    )
	    );


	    BigDecimal average =
	            resultRepository.findAveragePercentage(
	                    RecordStatus.DELETED
	            );


	    dto.setAveragePercentage(
	            average == null
	                    ? BigDecimal.ZERO
	                    : average
	    );
	    


		 // ==========================================================
		 // Fee Dashboard Statistics
		 // ==========================================================
	
	
		 dto.setTotalFeeCollection(
		         paymentRepository.getTotalFeeCollection(
		                 RecordStatus.DELETED
		         )
		 );
	
	
		 dto.setTodayCollection(
		         paymentRepository.getTodayCollection(
		                 today,
		                 RecordStatus.DELETED
		         )
		 );
	
	
		 dto.setMonthlyCollection(
		         paymentRepository.getMonthlyCollection(
		                 today.getYear(),
		                 today.getMonthValue(),
		                 RecordStatus.DELETED
		         )
		 );
	
	
		 dto.setPendingDue(
		         paymentRepository.getTotalPendingDue(
		                 RecordStatus.DELETED
		         )
		 );
	
	
		 dto.setTotalPayableFee(
		         paymentRepository.getTotalPayableFee(
		                 RecordStatus.DELETED
		         )
		 );
		    
		 BigDecimal payable =
			        dto.getTotalPayableFee();

			BigDecimal collected =
			        dto.getTotalFeeCollection();


			if(payable != null && payable.compareTo(BigDecimal.ZERO)>0){

			    dto.setCollectionPercentage(
			        collected
			        .multiply(BigDecimal.valueOf(100))
			        .divide(payable,2,java.math.RoundingMode.HALF_UP)
			    );

			}else{

			    dto.setCollectionPercentage(
			        BigDecimal.ZERO
			    );
			}
			
			long payments =
			        paymentRepository.countByStatusNot(
			                RecordStatus.DELETED
			        );


			dto.setTotalPayments(payments);


			if(payments>0){

			dto.setAverageCollection(
			        dto.getTotalFeeCollection()
			        .divide(
			             BigDecimal.valueOf(payments),
			             2,
			             java.math.RoundingMode.HALF_UP
			        )
			);

			}else{

			dto.setAverageCollection(BigDecimal.ZERO);

			}
			
			
			
			dto.setSuccessfulPayments(
			        paymentRepository.countByPaymentStatusAndStatusNot(
			                PaymentStatus.PAID,
			                RecordStatus.DELETED
			        )
			);


			dto.setFailedPayments(
			        paymentRepository.countByPaymentStatusAndStatusNot(
			                PaymentStatus.CANCELLED,
			                RecordStatus.DELETED
			        )
			);
			

			dto.setTotalRefunds(
			        refundRepository.countByStatusNot(
			                RecordStatus.DELETED
			        )
			);


			dto.setTotalRefundAmount(
			        refundRepository.getTotalRefundAmount(
			                RecordStatus.DELETED
			        )
			);
			
	    
			
		// ==========================================================
		// Receipt Statistics
		// ==========================================================

		dto.setTotalReceipts(
		        receiptRepository.countByStatusNot(
		                RecordStatus.DELETED
		        )
		);
	

	    return dto;
	}
	
	
	private DashboardResponseDto getStudentDashboard(User user) {

		
		

	    DashboardResponseDto dto =
	            new DashboardResponseDto();


	    Student student =
	            user.getStudent();


	    if(student == null){

	        throw new RuntimeException(
	                "Student profile not found"
	        );
	    }

	    

	    Long studentId =
	            student.getId();



	    // ==========================
	    // Fee Summary
	    // ==========================


	    dto.setStudentTotalFee(
	            zeroIfNull(
	                    paymentRepository.getStudentTotalFee(
	                            studentId,
	                            RecordStatus.DELETED
	                    )
	            )
	    );


	    dto.setStudentPaidFee(
	            zeroIfNull(
	                    paymentRepository.getStudentPaidFee(
	                            studentId,
	                            RecordStatus.DELETED
	                    )
	            )
	    );


	    dto.setStudentPendingFee(
	            zeroIfNull(
	                    paymentRepository.getStudentPendingFee(
	                            studentId,
	                            RecordStatus.DELETED
	                    )
	            )
	    );


	    // ==========================
	    // Attendance
	    // ==========================

	    dto.setStudentAttendance(

	        attendanceRepository
	        .countByStudentIdAndStatusNot(
	                studentId,
	                RecordStatus.DELETED
	        )

	    );



	    // ==========================
	    // Results
	    // ==========================

	    dto.setStudentResults(

	        resultRepository
	        .countByStudentIdAndStatusNot(
	                studentId,
	                RecordStatus.DELETED
	        )

	    );



	    // ==========================
	    // Exams
	    // ==========================

	    dto.setStudentExams(

	        examRepository
	        .countByStudentIdAndStatusNot(
	                studentId,
	                RecordStatus.DELETED
	        )

	    );


	    return dto;

	}
	 
	
	private DashboardResponseDto getFacultyDashboard(User user) {


	    DashboardResponseDto dto =
	            new DashboardResponseDto();


	    Faculty faculty =
	            user.getFaculty();
	    log.info("USER ID : {}", user.getId());
	    log.info("FACULTY OBJECT : {}", faculty);
	    log.info("FACULTY ID : {}", faculty.getId());
	    
	    long studentCount =
	            attendanceRepository.countDistinctStudentsByFacultyId(
	                    faculty.getId(),
	                    RecordStatus.DELETED
	            );

	    log.info("STUDENT COUNT : {}", studentCount);



	    LocalDate today =
	            LocalDate.now();



	    // =====================================================
	    // My Subjects
	    // =====================================================

	    long subjectCount =
	            facultySubjectRepository
	                    .countByFacultyIdAndStatusNot(
	                            faculty.getId(),
	                            RecordStatus.DELETED
	                    );


	    dto.setMySubjects(subjectCount);



	    // =====================================================
	    // My Students
	    // =====================================================

	    studentCount =
	            attendanceRepository
	                    .countDistinctStudentsByFacultyId(
	                            faculty.getId(),
	                            RecordStatus.DELETED
	                    );


	    dto.setMyStudents(studentCount);



	    // =====================================================
	    // Today's Attendance
	    // =====================================================

	    long todayAttendance =
	            attendanceRepository
	                    .countByFacultyIdAndAttendanceDateAndStatusNot(
	                            faculty.getId(),
	                            today,
	                            RecordStatus.DELETED
	                    );


	    dto.setTodayAttendanceByFaculty(
	            todayAttendance
	    );



	    // =====================================================
	    // My Exams
	    // =====================================================

	    long exams =
	            examRepository
	                    .countByFacultyIdAndStatusNot(
	                            faculty.getId(),
	                            RecordStatus.DELETED
	                    );


	    dto.setMyExams(exams);



	    // =====================================================
	    // My Results
	    // =====================================================

	    long results =
	            resultRepository
	                    .countByFacultyIdAndStatusNot(
	                            faculty.getId(),
	                            RecordStatus.DELETED
	                    );


	    dto.setMyResults(results);



	    return dto;
	}
	
	
	private BigDecimal zeroIfNull(BigDecimal value){

	    return value == null 
	            ? BigDecimal.ZERO 
	            : value;
	}
	
	
	
	

}