package com.sms.fee.refund.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.refund.dto.RefundActionRequestDto;
import com.sms.fee.refund.dto.RefundHistoryResponseDto;
import com.sms.fee.refund.dto.StudentFeeRefundRequestDto;
import com.sms.fee.refund.dto.StudentFeeRefundResponseDto;
import com.sms.fee.refund.dto.StudentFeeRefundSearchRequest;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.fee.refund.enums.RefundStatus;
import com.sms.fee.refund.mapper.RefundHistoryMapper;
import com.sms.fee.refund.mapper.StudentFeeRefundMapper;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.refund.service.StudentFeeRefundService;
import com.sms.fee.refund.specification.StudentFeeRefundSpecification;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentFeeRefundServiceImpl implements StudentFeeRefundService {

	private final StudentFeeRefundRepository refundRepository;

	private final StudentFeePaymentRepository paymentRepository;

	private final StudentFeeRefundMapper mapper;
	
	private final RefundHistoryMapper refundHistoryMapper;

	private final StudentFeeReceiptRepository receiptRepository;

	@AuditLog(
			action = AuditAction.CREATE, 
			module = "STUDENT_FEE_REFUND", 
			description = "Create refund")
	@Override
	public StudentFeeRefundResponseDto createRefund(StudentFeeRefundRequestDto request) {

	    // Validate Payment
	    StudentFeePayment payment = paymentRepository
	            .findByIdAndStatusNot(
	                    request.getPaymentId(),
	                    RecordStatus.DELETED
	            )
	            .orElseThrow(() -> new BusinessException(
	                    "Fee payment not found or deleted with ID : " + request.getPaymentId()
	            ));


	    // Validate Payment Status
	    if (payment.getPaymentStatus() == PaymentStatus.CANCELLED
	            || payment.getPaymentStatus() == PaymentStatus.PENDING) {

	        throw new BusinessException(
	                "Refund allowed only for paid payment"
	        );
	    }


	    // Validate Paid Amount
	    if (payment.getPaidAmount() == null
	            || payment.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {

	        throw new BusinessException(
	                "No paid amount available for refund"
	        );
	    }


	    // Validate Refund Amount
	    validateRefundAmount(
	            payment,
	            request.getRefundAmount(),
	            null
	    );


	    // Convert DTO To Entity
	    StudentFeeRefund refund = mapper.toEntity(request);


	    // Map Payment
	    refund.setPayment(payment);


	    // Validate Receipt
	    StudentFeeReceipt receipt = receiptRepository
	            .findByPaymentIdAndStatusNot(
	                    payment.getId(),
	                    RecordStatus.DELETED
	            )
	            .orElseThrow(() -> new BusinessException(
	                    "Please generate fee receipt before creating refund."
	            ));


	    // Validate Receipt Status
	    if (receipt.getStatus() == RecordStatus.DELETED) {

	        throw new BusinessException(
	                "Receipt is deleted"
	        );
	    }


	    // Map Receipt
	    refund.setReceipt(receipt);

	    refund.setReceiptNumber(
	            receipt.getReceiptNumber()
	    );

	    refund.setReceiptDate(
	            receipt.getReceiptDate()
	    );


	    // Generate Refund Number
	    refund.setRefundNumber(
	            "SMS-REF-"
	                    + LocalDate.now()
	                    .toString()
	                    .replace("-", "")
	                    + "-"
	                    + UUID.randomUUID()
	                    .toString()
	                    .substring(0, 8)
	                    .toUpperCase()
	    );


	    // Set Refund Date
	    refund.setRefundDate(
	            LocalDate.now()
	    );


	    // Set Refund Status
	    refund.setRefundStatus(
	            RefundStatus.REQUESTED
	    );


	    // Set Request Information
	    refund.setRequestedDate(
	            LocalDateTime.now()
	    );

	    refund.setRequestedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    refund.setRequestedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Set Student Snapshot
	    refund.setStudentId(
	            payment.getStudent().getId()
	    );

	    refund.setStudentName(
	            payment.getStudent().getFullName()
	    );

	    refund.setRollNumber(
	            payment.getStudent().getRollNumber()
	    );


	    // Set Admission Snapshot
	    refund.setAdmissionId(
	            payment.getAdmission().getId()
	    );

	    refund.setAdmissionNumber(
	            payment.getAdmission().getAdmissionNumber()
	    );


	    // Set Payment Transaction Snapshot
	    refund.setPaymentTransactionReference(
	            payment.getTransactionReference()
	    );


	    // Set Default Status
	    refund.setStatus(
	            RecordStatus.ACTIVE
	    );


	    // Save Refund
	    StudentFeeRefund saved = refundRepository.save(refund);


	    // Return Response
	    return mapper.toDto(saved);

	}

	@Override
	@Transactional(readOnly = true)
	public StudentFeeRefundResponseDto getRefundById(Long id) {

		StudentFeeRefund refund = refundRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new BusinessException("Refund not found"));

		return mapper.toDto(refund);

	}

	
	
	@AuditLog(
	        action = AuditAction.DELETE,
	        module = "STUDENT_FEE_REFUND",
	        description = "Delete refund"
	)
	@Override
	public void deleteRefund(Long id) {


	    // Validate Refund Exists
	    StudentFeeRefund refund =
	            refundRepository
	                    .findByIdAndStatusNot(
	                            id,
	                            RecordStatus.DELETED
	                    )
	                    .orElseThrow(() ->
	                            new BusinessException(
	                                    "Refund not found"
	                            )
	                    );


	    // Prevent Completed Refund Deletion
	    if (refund.getRefundStatus() == RefundStatus.COMPLETED) {

	        throw new BusinessException(
	                "Completed refund cannot be deleted"
	        );
	    }


	    // Set Delete Audit Information
	    refund.setDeletedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    refund.setDeletedBy(
	            SecurityUtils.getCurrentFullName()
	    );

	    refund.setDeletedDate(
	            LocalDateTime.now()
	    );


	    // Apply Soft Delete
	    refund.setStatus(
	            RecordStatus.DELETED
	    );


	    // Save Deleted Refund
	    refundRepository.save(refund);

	}
	
	

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentFeeRefundResponseDto> getAllRefunds(
	        int page,
	        int size,
	        String sortBy,
	        String direction) {


	    // Create Sorting And Pagination
	    Sort sort = direction.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();


	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            sort
	    );


	    // Fetch All Active Refund Records
	    Page<StudentFeeRefund> result =
	            refundRepository.findByStatusNot(
	                    RecordStatus.DELETED,
	                    pageable
	            );


	    // Convert Refund Entity To Response DTO
	    Page<StudentFeeRefundResponseDto> dtoPage =
	            result.map(mapper::toDto);


	    // Prepare Paginated Response
	    return new PageResponse<>(

	            dtoPage.getContent(),

	            dtoPage.getNumber(),

	            dtoPage.getSize(),

	            dtoPage.getTotalElements(),

	            dtoPage.getTotalPages(),

	            dtoPage.isLast()

	    );

	}

	
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentFeeRefundResponseDto> searchRefunds(
	        StudentFeeRefundSearchRequest request,
	        int page,
	        int size,
	        String sortBy,
	        String direction) {


	    // Create Sorting And Pagination
	    Sort sort = direction.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();


	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            sort
	    );


	    // Validate Refund Search Date Range
	    if (request.getFromDate() != null
	            && request.getToDate() != null
	            && request.getFromDate().isAfter(request.getToDate())) {

	        throw new BusinessException(
	                "From date cannot be greater than To date"
	        );
	    }


	    // Fetch Refund Data Using Dynamic Specification
	    Page<StudentFeeRefund> result =
	            refundRepository.findAll(
	                    StudentFeeRefundSpecification.search(request),
	                    pageable
	            );


	    // Convert Refund Entity To Response DTO
	    Page<StudentFeeRefundResponseDto> dtoPage =
	            result.map(mapper::toDto);


	    // Prepare Paginated Response
	    return new PageResponse<>(

	            dtoPage.getContent(),

	            dtoPage.getNumber(),

	            dtoPage.getSize(),

	            dtoPage.getTotalElements(),

	            dtoPage.getTotalPages(),

	            dtoPage.isLast()

	    );

	}

	// =====================================================
	// Validate Refund Amount
	// =====================================================

	private void validateRefundAmount(
	        StudentFeePayment payment,
	        BigDecimal refundAmount,
	        Long refundId
	) {

	    // Validate refund amount input

	    if (refundAmount == null) {

	        throw new BusinessException("Refund amount is required");
	    }


	    if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {

	        throw new BusinessException(
	                "Refund amount must be greater than zero"
	        );
	    }


	    // Calculate already refunded amount

	    BigDecimal existingRefundAmount =
	            refundRepository.getTotalRefundAmountByPayment(
	                    payment.getId(),
	                    RecordStatus.DELETED,
	                    refundId
	            );


	    if (existingRefundAmount == null) {

	        existingRefundAmount = BigDecimal.ZERO;
	    }


	    // Calculate total refund after current request

	    BigDecimal totalRefundAmount =
	            existingRefundAmount.add(refundAmount);


	    // Ensure refund does not exceed paid amount

	    if (totalRefundAmount.compareTo(payment.getPaidAmount()) > 0) {

	        throw new BusinessException(
	                "Total refund amount cannot exceed total paid amount."
	        );
	    }

	}
	
	

	@AuditLog(
	        action = AuditAction.APPROVE,
	        module = "STUDENT_FEE_REFUND",
	        description = "Approve refund"
	)
	@Override
	public StudentFeeRefundResponseDto approveRefund(Long refundId) {

	    // Validate Refund

	    StudentFeeRefund refund = refundRepository
	            .findByIdAndStatusNot(refundId, RecordStatus.DELETED)
	            .orElseThrow(() -> new BusinessException(
	                    "Refund not found with ID : " + refundId
	            ));


	    // Validate Refund Status

	    if (refund.getRefundStatus() != RefundStatus.REQUESTED) {

	        throw new BusinessException(
	                "Only REQUESTED refund can be approved."
	        );
	    }


	    // Approve Refund

	    refund.setRefundStatus(
	            RefundStatus.APPROVED
	    );


	    // Approval Information

	    refund.setApprovedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    refund.setApprovedBy(
	            SecurityUtils.getCurrentFullName()
	    );

	    refund.setApprovedDate(
	            LocalDateTime.now()
	    );


	    // Clear Rejection Information

	    refund.setRejectedByUserId(null);

	    refund.setRejectedBy(null);

	    refund.setRejectedDate(null);

	    refund.setRejectionReason(null);


	    // Save Refund

	    StudentFeeRefund updated =
	            refundRepository.save(refund);


	    // Return Response

	    return mapper.toDto(updated);
	}

	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "STUDENT_FEE_REFUND",
	        description = "Complete refund"
	)
	@Override
	public StudentFeeRefundResponseDto completeRefund(Long refundId) {

	    // Validate Refund

	    StudentFeeRefund refund = refundRepository
	            .findByIdAndStatusNot(refundId, RecordStatus.DELETED)
	            .orElseThrow(() -> new BusinessException(
	                    "Refund not found with ID : " + refundId
	            ));


	    // Validate Refund Status

	    if (refund.getRefundStatus() != RefundStatus.APPROVED) {

	        throw new BusinessException(
	                "Only APPROVED refund can be completed."
	        );
	    }


	    // Get Parent Payment

	    StudentFeePayment payment = refund.getPayment();


	    // Validate Refund Amount

	    if (refund.getRefundAmount().compareTo(payment.getPaidAmount()) > 0) {

	        throw new BusinessException(
	                "Refund amount cannot exceed current paid amount."
	        );
	    }


	    // Update Paid Amount

	    BigDecimal updatedPaidAmount =
	            payment.getPaidAmount()
	            .subtract(refund.getRefundAmount());

	    payment.setPaidAmount(
	            updatedPaidAmount
	    );


	    // Recalculate Due Amount

	    BigDecimal dueAmount =
	            payment.getFinalPayableAmount()
	            .subtract(updatedPaidAmount);

	    payment.setDueAmount(
	            dueAmount
	    );


	    // Update Payment Status

	    if (updatedPaidAmount.compareTo(BigDecimal.ZERO) == 0) {

	        payment.setPaymentStatus(
	                PaymentStatus.PENDING
	        );

	    } else if (dueAmount.compareTo(BigDecimal.ZERO) == 0) {

	        payment.setPaymentStatus(
	                PaymentStatus.PAID
	        );

	    } else {

	        payment.setPaymentStatus(
	                PaymentStatus.PARTIAL
	        );
	    }


	    // Update Payment Audit

	    payment.setUpdatedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    payment.setUpdatedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Save Payment

	    paymentRepository.save(payment);


	    // Complete Refund

	    refund.setRefundStatus(
	            RefundStatus.COMPLETED
	    );


	    // Completion Information

	    refund.setCompletedDate(
	            LocalDateTime.now()
	    );

	    refund.setCompletedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    refund.setCompletedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Save Refund

	    StudentFeeRefund updated =
	            refundRepository.save(refund);


	    // Return Response

	    return mapper.toDto(updated);
	}
	
	

	@AuditLog(
	        action = AuditAction.REJECT,
	        module = "STUDENT_FEE_REFUND",
	        description = "Reject refund"
	)
	@Override
	public StudentFeeRefundResponseDto rejectRefund(
	        Long refundId,
	        RefundActionRequestDto request
	) {

	    // Validate Refund

	    StudentFeeRefund refund = refundRepository
	            .findByIdAndStatusNot(refundId, RecordStatus.DELETED)
	            .orElseThrow(() -> new BusinessException(
	                    "Refund not found with ID : " + refundId
	            ));


	    // Validate Refund Status

	    if (refund.getRefundStatus() != RefundStatus.REQUESTED) {

	        throw new BusinessException(
	                "Only REQUESTED refund can be rejected."
	        );
	    }


	    // Validate Rejection Reason

	    if (request.getReason() == null
	            || request.getReason().isBlank()) {

	        throw new BusinessException(
	                "Rejection reason is required"
	        );
	    }


	    // Reject Refund

	    refund.setRefundStatus(
	            RefundStatus.REJECTED
	    );


	    // Rejection Information

	    refund.setRejectionReason(
	            request.getReason().trim()
	    );

	    refund.setRejectedDate(
	            LocalDateTime.now()
	    );

	    refund.setRejectedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    refund.setRejectedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Clear Approval Information

	    refund.setApprovedByUserId(null);

	    refund.setApprovedBy(null);

	    refund.setApprovedDate(null);


	    // Save Refund

	    StudentFeeRefund updated =
	            refundRepository.save(refund);


	    // Return Response

	    return mapper.toDto(updated);
	}

	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "STUDENT_FEE_REFUND",
	        description = "Cancel refund"
	)
	@Override
	public StudentFeeRefundResponseDto cancelRefund(
	        Long refundId,
	        RefundActionRequestDto request
	) {

	    // Validate Refund

	    StudentFeeRefund refund = refundRepository
	            .findByIdAndStatusNot(refundId, RecordStatus.DELETED)
	            .orElseThrow(() -> new BusinessException(
	                    "Refund not found with ID : " + refundId
	            ));


	    // Validate Refund Status

	    if (refund.getRefundStatus() != RefundStatus.REQUESTED) {

	        throw new BusinessException(
	                "Only REQUESTED refund can be cancelled."
	        );
	    }


	    // Validate Cancellation Reason

	    if (request.getReason() == null
	            || request.getReason().isBlank()) {

	        throw new BusinessException(
	                "Cancellation reason is required"
	        );
	    }


	    // Cancel Refund

	    refund.setRefundStatus(
	            RefundStatus.CANCELLED
	    );


	    // Cancellation Information

	    refund.setCancellationReason(
	            request.getReason().trim()
	    );

	    refund.setCancelledDate(
	            LocalDateTime.now()
	    );

	    refund.setCancelledByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    refund.setCancelledBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Clear Approval Information

	    refund.setApprovedByUserId(null);

	    refund.setApprovedBy(null);

	    refund.setApprovedDate(null);


	    // Save Refund

	    StudentFeeRefund updated =
	            refundRepository.save(refund);


	    // Return Response

	    return mapper.toDto(updated);
	}

	
	
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<RefundHistoryResponseDto> getRefundHistory(
	        Long paymentId,
	        int page,
	        int size,
	        String sortBy,
	        String direction) {


	    // Create Sorting And Pagination
	    Sort sort = direction.equalsIgnoreCase("asc")
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();


	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            sort
	    );


	    // Fetch Refund Records By Payment
	    Page<StudentFeeRefund> refundPage =
	            refundRepository.findByPaymentIdAndStatusNot(
	                    paymentId,
	                    RecordStatus.DELETED,
	                    pageable
	            );


	    // Convert Refund Entity To Response DTO
	    Page<RefundHistoryResponseDto> dtoPage =
	            refundPage.map(refundHistoryMapper::toDto);


	    // Prepare Paginated Response
	    return new PageResponse<>(

	            dtoPage.getContent(),

	            dtoPage.getNumber(),

	            dtoPage.getSize(),

	            dtoPage.getTotalElements(),

	            dtoPage.getTotalPages(),

	            dtoPage.isLast()

	    );

	}

}