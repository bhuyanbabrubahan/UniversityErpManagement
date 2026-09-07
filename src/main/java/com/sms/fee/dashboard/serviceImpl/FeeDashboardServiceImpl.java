package com.sms.fee.dashboard.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.fee.dashboard.dto.FeeDashboardResponseDto;
import com.sms.fee.dashboard.service.FeeDashboardService;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.repository.StudentScholarshipRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.fee.refund.enums.RefundStatus;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.installment.enums.InstallmentStatus;
import com.sms.fee.installment.repository.StudentFeeInstallmentRepository;
import com.sms.fee.dashboard.dto.FeeActivityItemDto;
import com.sms.fee.dashboard.dto.FeeDueStudentDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeeDashboardServiceImpl
        implements FeeDashboardService {

    private final StudentFeePaymentRepository paymentRepository;

    private final StudentScholarshipRepository scholarshipRepository;
    
    private final StudentFeeReceiptRepository receiptRepository;
    private final StudentFeeRefundRepository refundRepository;
    private final StudentFeeInstallmentRepository installmentRepository;

    @Override
    public FeeDashboardResponseDto getDashboard() {

        FeeDashboardResponseDto dto =
                new FeeDashboardResponseDto();

        LocalDate today =
                LocalDate.now();

        // =====================================================
        // Collection Summary
        // =====================================================

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

        dto.setTotalPendingDue(
                paymentRepository.getTotalPendingDue(
                        RecordStatus.DELETED
                )
        );

        // =====================================================
        // Payment Statistics
        // =====================================================

        dto.setTotalPayments(
                paymentRepository.countByStatusNot(
                        RecordStatus.DELETED
                )
        );

        dto.setPaidPayments(
                paymentRepository.countByPaymentStatusAndStatusNot(
                        PaymentStatus.PAID,
                        RecordStatus.DELETED
                )
        );

        dto.setPartialPayments(
                paymentRepository.countByPaymentStatusAndStatusNot(
                        PaymentStatus.PARTIAL,
                        RecordStatus.DELETED
                )
        );

        dto.setPendingPayments(
                paymentRepository.countByPaymentStatusAndStatusNot(
                        PaymentStatus.PENDING,
                        RecordStatus.DELETED
                )
        );

        dto.setCancelledPayments(
                paymentRepository.countByPaymentStatusAndStatusNot(
                        PaymentStatus.CANCELLED,
                        RecordStatus.DELETED
                )
        );

        // =====================================================
        // Payment Mode Collection
        // =====================================================

        dto.setCashCollection(
                paymentRepository.getCollectionByPaymentMode(
                        PaymentMode.CASH,
                        RecordStatus.DELETED
                )
        );

        dto.setOnlineCollection(
                paymentRepository.getCollectionByPaymentMode(
                        PaymentMode.ONLINE,
                        RecordStatus.DELETED
                )
        );

        dto.setUpiCollection(
                paymentRepository.getCollectionByPaymentMode(
                        PaymentMode.UPI,
                        RecordStatus.DELETED
                )
        );

        dto.setChequeCollection(
                paymentRepository.getCollectionByPaymentMode(
                        PaymentMode.CHEQUE,
                        RecordStatus.DELETED
                )
        );

        // =====================================================
        // Scholarship Summary
        // =====================================================

        dto.setTotalScholarshipAmount(
                scholarshipRepository.getTotalApprovedScholarshipAmount(
                        RecordStatus.DELETED,
                        ScholarshipStatus.APPROVED
                )
        );

        dto.setTotalScholarshipStudents(
                scholarshipRepository.countByScholarshipStatusAndStatusNot(
                        ScholarshipStatus.APPROVED,
                        RecordStatus.DELETED
                )
        );
        
        
        // =====================================================
        // Average Collection
        // =====================================================
        
        if(dto.getTotalPayments()!=null 
                && dto.getTotalPayments()>0){

            dto.setAverageCollection(
                    dto.getTotalFeeCollection()
                    .divide(
                        BigDecimal.valueOf(
                            dto.getTotalPayments()
                        ),
                        2,
                        RoundingMode.HALF_UP
                    )
            );

        }
        else{

            dto.setAverageCollection(
                    BigDecimal.ZERO
            );

        }
        
        // =====================================================
        // Collection Percentage
        // =====================================================
        
        BigDecimal totalPayable =
                paymentRepository.getTotalPayableFee(
                        RecordStatus.DELETED
                );
        
        if(totalPayable.compareTo(BigDecimal.ZERO)>0){

            dto.setCollectionPercentage(

                dto.getTotalFeeCollection()
                .multiply(
                     BigDecimal.valueOf(100)
                )
                .divide(
                     totalPayable,
                     2,
                     RoundingMode.HALF_UP
                )

            );

        }
        else{

            dto.setCollectionPercentage(
                    BigDecimal.ZERO
            );

        }
        
        
        
     // =====================================================
     // Receipt Summary
     // =====================================================

     dto.setTotalReceipts(
             receiptRepository.countByStatusNot(
                     RecordStatus.DELETED
             )
     );

     LocalDate monthStart = today.withDayOfMonth(1);
     LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
     LocalDateTime dayStart = today.atStartOfDay();
     LocalDateTime dayEnd = today.plusDays(1).atStartOfDay();

     // Refund summary
     dto.setTotalRefundAmount(defaultZero(refundRepository.getTotalRefundAmount(RecordStatus.DELETED)));
     dto.setTodayRefunds(refundRepository.countByRefundDateAndStatusNot(today, RecordStatus.DELETED));
     dto.setMonthlyRefunds(refundRepository.countByRefundDateBetweenAndStatusNot(monthStart, monthEnd, RecordStatus.DELETED));
     dto.setCompletedRefunds(refundRepository.countByRefundStatusAndStatusNot(RefundStatus.COMPLETED, RecordStatus.DELETED));
     dto.setPendingRefunds(refundRepository.countByRefundStatusAndStatusNot(RefundStatus.REQUESTED, RecordStatus.DELETED));
     dto.setRejectedRefunds(refundRepository.countByRefundStatusAndStatusNot(RefundStatus.REJECTED, RecordStatus.DELETED));
     dto.setCancelledRefunds(refundRepository.countByRefundStatusAndStatusNot(RefundStatus.CANCELLED, RecordStatus.DELETED));

     // Installment summary
     dto.setTotalInstallments(installmentRepository.countByStatusNot(RecordStatus.DELETED));
     dto.setPaidInstallments(installmentRepository.countByInstallmentStatusAndStatusNot(InstallmentStatus.PAID, RecordStatus.DELETED));
     dto.setPendingInstallments(installmentRepository.countByInstallmentStatusAndStatusNot(InstallmentStatus.PENDING, RecordStatus.DELETED));
     dto.setOverdueInstallments(
    	        installmentRepository.countOverdue(
    	                today,
    	                InstallmentStatus.PENDING,
    	                RecordStatus.DELETED
    	        )
    	);


     // Receipt summary
     dto.setTodayReceipts(receiptRepository.countByReceiptDateAndStatusNot(today, RecordStatus.DELETED));
     dto.setMonthlyReceipts(receiptRepository.countByReceiptDateBetweenAndStatusNot(monthStart, monthEnd, RecordStatus.DELETED));
     dto.setGeneratedToday(dto.getTodayReceipts());
     dto.setDownloadedToday(receiptRepository.countByPrintedDateBetweenAndStatusNot(dayStart, dayEnd, RecordStatus.DELETED));

     // Student fee summary is based on payment-level status.
     dto.setTotalStudentsWithFees(paymentRepository.countDistinctStudents(RecordStatus.DELETED));
     dto.setPaidStudents(paymentRepository.countDistinctStudentsByPaymentStatus(PaymentStatus.PAID, RecordStatus.DELETED));
     dto.setPartialStudents(paymentRepository.countDistinctStudentsByPaymentStatus(PaymentStatus.PARTIAL, RecordStatus.DELETED));
     dto.setPendingStudents(paymentRepository.countDistinctStudentsByPaymentStatus(PaymentStatus.PENDING, RecordStatus.DELETED));

     // Alerts
     dto.setOverdueInstallmentAlerts(dto.getOverdueInstallments());
     dto.setPendingRefundApprovals(dto.getPendingRefunds());
     dto.setFailedPayments(paymentRepository.countByPaymentStatusAndStatusNot(PaymentStatus.CANCELLED, RecordStatus.DELETED));

     // Recent activity
     dto.setRecentPayments(new ArrayList<>());
     paymentRepository.findTop10ByStatusNotOrderByPaymentDateDesc(RecordStatus.DELETED).forEach(p -> {
         FeeActivityItemDto item = new FeeActivityItemDto();
         item.setId(p.getId()); item.setReference(p.getTransactionReference()); item.setAmount(p.getPaidAmount());
         item.setDate(p.getPaymentDate()); item.setStatus(p.getPaymentStatus().name());
         dto.getRecentPayments().add(item);
     });

     dto.setRecentReceipts(new ArrayList<>());
     receiptRepository.findTop10ByStatusNotOrderByReceiptDateDesc(RecordStatus.DELETED).forEach(r -> {
         FeeActivityItemDto item = new FeeActivityItemDto();
         item.setId(r.getId()); item.setReference(r.getReceiptNumber()); item.setName(r.getStudentName());
         item.setAmount(r.getPaidAmount()); item.setDate(r.getReceiptDate()); item.setStatus(r.getPaymentStatus().name());
         dto.getRecentReceipts().add(item);
     });

     dto.setRecentRefunds(new ArrayList<>());
     refundRepository.findTop10ByStatusNotOrderByRefundDateDesc(RecordStatus.DELETED).forEach(r -> {
         FeeActivityItemDto item = new FeeActivityItemDto();
         item.setId(r.getId()); item.setReference(r.getRefundNumber()); item.setName(r.getStudentName());
         item.setAmount(r.getRefundAmount()); item.setDate(r.getRefundDate()); item.setStatus(r.getRefundStatus().name());
         dto.getRecentRefunds().add(item);
     });

     dto.setRecentScholarships(new ArrayList<>());
     scholarshipRepository.findTop10ByStatusNotOrderByCreatedAtDesc(RecordStatus.DELETED).forEach(s -> {
         FeeActivityItemDto item = new FeeActivityItemDto();
         item.setId(s.getId()); item.setReference(s.getScholarshipName()); item.setAmount(s.getApprovedAmount());
         item.setDate(s.getApprovalDate() == null ? today : s.getApprovalDate().toLocalDate());
         item.setStatus(s.getScholarshipStatus().name());
         dto.getRecentScholarships().add(item);
     });

     dto.setHighestDueStudents(new ArrayList<>());
     Set<Long> seenStudents = new HashSet<>();
     paymentRepository.findTop50ByStatusNotOrderByDueAmountDesc(RecordStatus.DELETED).forEach(p -> {
         if (p.getStudent() != null && seenStudents.size() < 10 && seenStudents.add(p.getStudent().getId())) {
             dto.getHighestDueStudents().add(new FeeDueStudentDto(
                     p.getStudent().getId(), p.getStudent().getFullName(), p.getStudent().getRollNumber(), p.getDueAmount()));
         }
     });

        return dto;
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
