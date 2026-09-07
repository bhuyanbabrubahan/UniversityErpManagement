package com.sms.fee.report.service;


import java.util.List;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.report.FeeCollectionReportResponseDto;
import com.sms.fee.dto.report.FeeReportSearchRequestDto;
import com.sms.fee.report.dto.AcademicYearWiseFeeReportDto;
import com.sms.fee.report.dto.CourseWiseReportDto;
import com.sms.fee.report.dto.DepartmentFeeReportDto;
import com.sms.fee.report.dto.MonthlyCollectionReportDto;
import com.sms.fee.report.dto.PaymentModeReportDto;
import com.sms.fee.report.dto.StudentPaymentHistoryResponseDto;


public interface FeeReportService {

    // Department Wise Fee Report
    List<DepartmentFeeReportDto> getDepartmentWiseReport();


    // Course Wise Fee Report
    List<CourseWiseReportDto> getCourseWiseReport();


    // Academic Year Wise Fee Report
    List<AcademicYearWiseFeeReportDto> getAcademicYearWiseReport();


    // Payment Mode Wise Report
    List<PaymentModeReportDto> getPaymentModeReport();


    // Monthly Collection Report
    List<MonthlyCollectionReportDto> getMonthlyCollectionReport();


    
	 // =====================================================
	 // Fee Collection Report
	 // =====================================================
	
	 PageResponse<FeeCollectionReportResponseDto> getFeeCollectionReport(
	         FeeReportSearchRequestDto request
	 );
	 
	 
	 StudentPaymentHistoryResponseDto getStudentPaymentHistory(
	            Long studentId
	    );


}