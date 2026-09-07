package com.sms.fee.report.serviceImpl;


import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admission.entity.Admission;
import com.admission.repository.AdmissionRepository;
import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.fee.dto.report.FeeCollectionReportResponseDto;
import com.sms.fee.dto.report.FeeReportSearchRequestDto;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.fee.refund.enums.RefundStatus;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.report.dto.AcademicYearWiseFeeReportDto;
import com.sms.fee.report.dto.CourseWiseReportDto;
import com.sms.fee.report.dto.DepartmentFeeReportDto;
import com.sms.fee.report.dto.MonthlyCollectionReportDto;
import com.sms.fee.report.dto.PaymentModeReportDto;
import com.sms.fee.report.dto.StudentPaymentHistoryResponseDto;
import com.sms.fee.report.mapper.FeeCollectionReportMapper;
import com.sms.fee.report.mapper.StudentPaymentHistoryReportMapper;
import com.sms.fee.report.repository.FeeReportRepository;
import com.sms.fee.report.service.FeeReportService;
import com.sms.fee.report.specification.FeeReportSpecification;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.repository.StudentRepository;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.repository.StudentScholarshipRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeeReportServiceImpl implements FeeReportService {

    private final StudentRepository studentRepository;

    private final StudentFeePaymentRepository paymentRepository;

    private final StudentFeeReceiptRepository receiptRepository;

    private final StudentFeeRefundRepository refundRepository;

    private final StudentScholarshipRepository scholarshipRepository;
    
    private final FeeReportRepository feeReportRepository;

    private final FeeCollectionReportMapper feeCollectionReportMapper;
    
    private final StudentPaymentHistoryReportMapper reportMapper;
    
    private final AdmissionRepository admissionRepository;
    

    // =====================================================
    // Department Wise Fee Report
    // =====================================================


    @Override
    public List<DepartmentFeeReportDto> getDepartmentWiseReport() {



        List<Object[]> result =
                feeReportRepository
                .getDepartmentWiseCollection(
                        RecordStatus.DELETED
                );



        List<DepartmentFeeReportDto> response =
                new ArrayList<>();



        for(Object[] row : result){


            DepartmentFeeReportDto dto =
                    new DepartmentFeeReportDto();



            dto.setDepartmentId(
                    row[0] != null
                    ?
                    ((Number)row[0]).longValue()
                    :
                    null
            );



            dto.setDepartmentName(
                    row[1] != null
                    ?
                    row[1].toString()
                    :
                    null
            );



            dto.setTotalStudents(
                    row[2] != null
                    ?
                    ((Number)row[2]).longValue()
                    :
                    0L
            );



            dto.setTotalFee(
                    row[3] != null
                    ?
                    (BigDecimal)row[3]
                    :
                    BigDecimal.ZERO
            );



            dto.setCollectedAmount(
                    row[4] != null
                    ?
                    (BigDecimal)row[4]
                    :
                    BigDecimal.ZERO
            );



            dto.setPendingAmount(
                    row[5] != null
                    ?
                    (BigDecimal)row[5]
                    :
                    BigDecimal.ZERO
            );



            response.add(dto);

        }



        return response;

    }



	 // =====================================================
	 // Course Wise Fee Report
	 // =====================================================
	
	
	 @Override
	 public List<CourseWiseReportDto> getCourseWiseReport() {
	
	
	     List<Object[]> result =
	             feeReportRepository
	             .getCourseWiseCollection(
	                     RecordStatus.DELETED
	             );
	
	
	
	     List<CourseWiseReportDto> response =
	             new ArrayList<>();
	
	
	
	     for(Object[] row : result){
	
	
	
	         CourseWiseReportDto dto =
	                 new CourseWiseReportDto();
	
	
	
	         dto.setCourseId(
	                 row[0] != null
	                 ?
	                 ((Number)row[0]).longValue()
	                 :
	                 null
	         );
	
	
	
	         dto.setCourseName(
	                 row[1] != null
	                 ?
	                 row[1].toString()
	                 :
	                 null
	         );
	
	
	
	         dto.setDepartmentName(
	                 row[2] != null
	                 ?
	                 row[2].toString()
	                 :
	                 null
	         );
	
	
	
	         dto.setTotalStudents(
	                 row[3] != null
	                 ?
	                 ((Number)row[3]).longValue()
	                 :
	                 0L
	         );
	
	
	
	         dto.setTotalFee(
	                 row[4] != null
	                 ?
	                 (BigDecimal) row[4]
	                 :
	                 BigDecimal.ZERO
	         );
	
	
	
	         dto.setCollectedAmount(
	                 row[5] != null
	                 ?
	                 (BigDecimal) row[5]
	                 :
	                 BigDecimal.ZERO
	         );
	
	
	
	         dto.setPendingAmount(
	                 row[6] != null
	                 ?
	                 (BigDecimal) row[6]
	                 :
	                 BigDecimal.ZERO
	         );
	
	
	
	         response.add(dto);
	
	     }
	
	
	
	     return response;
	
	 }



	// =====================================================
	// Academic Year Wise Fee Report
	// =====================================================


	@Override
	public List<AcademicYearWiseFeeReportDto> getAcademicYearWiseReport() {



	    List<Object[]> result =
	            feeReportRepository
	            .getAcademicYearWiseCollection(
	                    RecordStatus.DELETED
	            );



	    List<AcademicYearWiseFeeReportDto> response =
	            new ArrayList<>();



	    for(Object[] row : result){



	        AcademicYearWiseFeeReportDto dto =
	                new AcademicYearWiseFeeReportDto();




	        // Academic Year

	        dto.setAcademicYear(
	                row[0] != null
	                ?
	                row[0].toString()
	                :
	                null
	        );




	        // Total Students

	        dto.setTotalStudents(
	                row[1] != null
	                ?
	                ((Number)row[1]).longValue()
	                :
	                0L
	        );




	        // Total Fee

	        dto.setTotalFee(
	                row[2] != null
	                ?
	                (BigDecimal)row[2]
	                :
	                BigDecimal.ZERO
	        );




	        // Collected Amount

	        dto.setCollectedAmount(
	                row[3] != null
	                ?
	                (BigDecimal)row[3]
	                :
	                BigDecimal.ZERO
	        );




	        // Pending Amount

	        dto.setPendingAmount(
	                row[4] != null
	                ?
	                (BigDecimal)row[4]
	                :
	                BigDecimal.ZERO
	        );




	        // Total Payments

	        dto.setTotalPayments(
	                row[5] != null
	                ?
	                ((Number)row[5]).longValue()
	                :
	                0L
	        );




	        response.add(dto);

	    }



	    return response;

	}



	// =====================================================
	// Payment Mode Wise Report
	// =====================================================


	@Override
	public List<PaymentModeReportDto> getPaymentModeReport() {



	    List<Object[]> result =
	            feeReportRepository
	            .getPaymentModeWiseCollection(
	                    RecordStatus.DELETED
	            );



	    List<PaymentModeReportDto> response =
	            new ArrayList<>();



	    for(Object[] row : result){



	        PaymentModeReportDto dto =
	                new PaymentModeReportDto();




	        // Payment Mode

	        dto.setPaymentMode(
	                row[0] != null
	                ?
	                row[0].toString()
	                :
	                null
	        );




	        // Total Transactions

	        dto.setTotalTransactions(
	                row[1] != null
	                ?
	                ((Number)row[1]).longValue()
	                :
	                0L
	        );




	        // Total Collection

	        dto.setTotalCollection(
	                row[2] != null
	                ?
	                (BigDecimal)row[2]
	                :
	                BigDecimal.ZERO
	        );




	        response.add(dto);

	    }



	    return response;

	}



	// =====================================================
	// Monthly Collection Report
	// =====================================================


	@Override
	public List<MonthlyCollectionReportDto> getMonthlyCollectionReport() {



	    List<Object[]> result =
	            feeReportRepository
	            .getMonthlyCollection(
	                    RecordStatus.DELETED
	            );



	    List<MonthlyCollectionReportDto> response =
	            new ArrayList<>();



	    for(Object[] row : result){



	        MonthlyCollectionReportDto dto =
	                new MonthlyCollectionReportDto();




	        // Month Number

	        Integer month =
	                row[0] != null
	                ?
	                ((Number)row[0]).intValue()
	                :
	                null;



	        dto.setMonth(month);




	        // Month Name

	        if(month != null){

	            dto.setMonthName(
	                    Month.of(month)
	                    .name()
	            );

	        }




	        // Year

	        dto.setYear(
	                row[1] != null
	                ?
	                ((Number)row[1]).intValue()
	                :
	                null
	        );




	        // Total Transactions

	        dto.setTotalTransactions(
	                row[2] != null
	                ?
	                ((Number)row[2]).longValue()
	                :
	                0L
	        );




	        // Total Collection

	        dto.setTotalCollection(
	                row[3] != null
	                ?
	                (BigDecimal)row[3]
	                :
	                BigDecimal.ZERO
	        );




	        response.add(dto);

	    }



	    return response;

	}



	
	
	//=========================================================================================================
	//Advanced Fee Reports
	//=========================================================================================================
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<FeeCollectionReportResponseDto> getFeeCollectionReport(
	        FeeReportSearchRequestDto request) {


	    Sort sort =
	            Sort.by(
	                    Sort.Direction.fromString(
	                            request.getSortDirection()
	                    ),
	                    request.getSortBy()
	            );


	    Pageable pageable =
	            PageRequest.of(
	                    request.getPage(),
	                    request.getSize(),
	                    sort
	            );


	    Page<StudentFeeReceipt> receipts =
	            receiptRepository.findAll(
	                    FeeReportSpecification.build(request),
	                    pageable
	            );


	    return PageResponse.<FeeCollectionReportResponseDto>builder()

	            .content(
	                    receipts
	                            .map(
	                                    feeCollectionReportMapper::toDto
	                            )
	                            .getContent()
	            )

	            .pageNumber(
	                    receipts.getNumber()
	            )

	            .pageSize(
	                    receipts.getSize()
	            )

	            .totalElements(
	                    receipts.getTotalElements()
	            )

	            .totalPages(
	                    receipts.getTotalPages()
	            )

	            .last(
	                    receipts.isLast()
	            )

	            .build();

	}


	@AuditLog(
	        action = AuditAction.VIEW,
	        module = "FEE_REPORT",
	        description = "View student payment history report"
	)
	@Override
	public StudentPaymentHistoryResponseDto getStudentPaymentHistory(
	        Long studentId
	) {

	    // =====================================================
	    // Validate Student
	    // =====================================================

	    Student student =
	            studentRepository
	                    .findByIdAndStatusNot(
	                            studentId,
	                            RecordStatus.DELETED
	                    )
	                    .orElseThrow(() ->
	                            new BusinessException(
	                                    "Student not found with ID : "
	                                            + studentId
	                            )
	                    );

	    // =====================================================
	    // Fetch Admission
	    // =====================================================

	    Admission admission =
	            admissionRepository
	                    .findByStudentIdAndStatusNot(
	                            studentId,
	                            RecordStatus.DELETED
	                    )
	                    .orElseThrow(() ->
	                            new BusinessException(
	                                    "Admission not found for student."
	                            )
	                    );

	    // =====================================================
	    // Fetch Payment History
	    // =====================================================

	    List<StudentFeePayment> payments =
	            paymentRepository
	                    .findByStudentIdAndStatusNotOrderByPaymentDateDesc(
	                            studentId,
	                            RecordStatus.DELETED
	                    );

	    // =====================================================
	    // Fetch Receipt History
	    // =====================================================

	    List<StudentFeeReceipt> receipts =
	            receiptRepository
	                    .findByStudentIdAndStatusNotOrderByReceiptDateDesc(
	                            studentId,
	                            RecordStatus.DELETED
	                    );

	    // =====================================================
	    // Fetch Refund History
	    // =====================================================

	    List<StudentFeeRefund> refunds =
	            refundRepository
	                    .findByStudentIdAndStatusNotOrderByRefundDateDesc(
	                            studentId,
	                            RecordStatus.DELETED
	                    );

	    // =====================================================
	    // Fetch Scholarship History
	    // =====================================================

	    List<StudentScholarship> scholarships =
	            scholarshipRepository
	                    .findByStudentIdAndStatusNotOrderByCreatedAtDesc(
	                            studentId,
	                            RecordStatus.DELETED
	                    );

	    // =====================================================
	    // Map Entity To Response
	    // =====================================================

	    StudentPaymentHistoryResponseDto response =
	            reportMapper.toDto(
	                    student,
	                    admission,
	                    payments,
	                    receipts,
	                    refunds,
	                    scholarships
	            );

	    // =====================================================
	    // Populate Financial Summary
	    // =====================================================

	    populateFinancialSummary(
	            response,
	            payments,
	            refunds,
	            scholarships
	    );

	    return response;
	}
	
	
	// =====================================================
	// Populate Financial Summary
	// =====================================================

	private void populateFinancialSummary(

	        StudentPaymentHistoryResponseDto response,

	        List<StudentFeePayment> payments,

	        List<StudentFeeRefund> refunds,

	        List<StudentScholarship> scholarships

	) {

		response.setTotalFee(
		        calculateTotalFee(payments)
		);

		response.setTotalScholarship(
		        calculateTotalScholarship(scholarships)
		);

		response.setFinalPayableAmount(
		        calculateFinalPayableAmount(payments)
		);

		response.setTotalPaidAmount(
		        calculateTotalPaidAmount(payments)
		);

		response.setTotalRefundAmount(
		        calculateCompletedRefundAmount(refunds)
		);

		response.setTotalDueAmount(
		        calculateOutstandingAmount(payments)
		);

		response.setPaymentStatus(
		        calculatePaymentStatus(payments)
		);

	}
	
	
	
	// =====================================================
	// Calculate Total Fee
	// =====================================================

	private BigDecimal calculateTotalFee(
	        List<StudentFeePayment> payments
	)
	{

	    return payments.stream()
	            .findFirst()
	            .map(StudentFeePayment::getTotalFee)
	            .orElse(BigDecimal.ZERO);

	}
	
	// =====================================================
	// Calculate Total Paid Amount
	// =====================================================

	private BigDecimal calculateTotalPaidAmount(
	        List<StudentFeePayment> payments
	) {

		return payments.stream()
		        .findFirst()
		        .map(StudentFeePayment::getPaidAmount)
		        .orElse(BigDecimal.ZERO);

	}
	
	
	
	// =====================================================
	// Calculate Approved Scholarship Amount
	// =====================================================

	private BigDecimal calculateTotalScholarship(
	        List<StudentScholarship> scholarships
	) {

	    return scholarships.stream()

	            .filter(
	                    scholarship ->
	                            scholarship.getScholarshipStatus()
	                                    == ScholarshipStatus.APPROVED
	            )

	            .map(StudentScholarship::getApprovedAmount)

	            .filter(Objects::nonNull)

	            .reduce(
	                    BigDecimal.ZERO,
	                    BigDecimal::add
	            );

	}
	
	
	// =====================================================
	// Calculate Completed Refund Amount
	// =====================================================

	private BigDecimal calculateCompletedRefundAmount(
	        List<StudentFeeRefund> refunds
	) {

	    return refunds.stream()

	            .filter(
	                    refund ->
	                            refund.getRefundStatus()
	                                    == RefundStatus.COMPLETED
	            )

	            .map(StudentFeeRefund::getRefundAmount)

	            .filter(Objects::nonNull)

	            .reduce(
	                    BigDecimal.ZERO,
	                    BigDecimal::add
	            );

	}
	
	
	// =====================================================
	// Calculate Outstanding Amount
	// =====================================================

	private BigDecimal calculateOutstandingAmount(
	        List<StudentFeePayment> payments
	) {

	    return payments.stream()
	            .findFirst()
	            .map(StudentFeePayment::getDueAmount)
	            .orElse(BigDecimal.ZERO);

	}
	
	private BigDecimal calculateFinalPayableAmount(
	        List<StudentFeePayment> payments
	) {

	    return payments.stream()
	            .findFirst()
	            .map(StudentFeePayment::getFinalPayableAmount)
	            .orElse(BigDecimal.ZERO);

	}
	

	
	private PaymentStatus calculatePaymentStatus(
	        List<StudentFeePayment> payments
	) {

	    return payments.stream()
	            .findFirst()
	            .map(StudentFeePayment::getPaymentStatus)
	            .orElse(PaymentStatus.PENDING);

	}
	
	
}