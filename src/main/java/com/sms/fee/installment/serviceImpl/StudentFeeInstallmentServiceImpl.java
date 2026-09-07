package com.sms.fee.installment.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admission.entity.Admission;
import com.sms.dto.PageResponse;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceAlreadyExistsException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.installment.dto.PayInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentResponseDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentSearchRequestDto;
import com.sms.fee.installment.entity.StudentFeeInstallment;
import com.sms.fee.installment.enums.InstallmentStatus;
import com.sms.fee.installment.mapper.StudentFeeInstallmentMapper;
import com.sms.fee.installment.repository.StudentFeeInstallmentRepository;
import com.sms.fee.installment.service.StudentFeeInstallmentService;
import com.sms.fee.installment.specification.StudentFeeInstallmentSpecification;
import com.sms.fee.refund.enums.RefundStatus;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentFeeInstallmentServiceImpl implements StudentFeeInstallmentService {

	private final StudentFeeInstallmentRepository installmentRepository;

	private final StudentFeePaymentRepository paymentRepository;
	
	private final StudentFeeRefundRepository studentFeeRefundRepository;

	private final StudentFeeInstallmentMapper installmentMapper;

	// Allowed Sorting Fields

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(

			"id",

			"installmentNumber",

			"installmentSequence",

			"studentName",

			"rollNumber",

			"academicYear",

			"installmentAmount",

			"paidAmount",

			"remainingAmount",

			"dueDate",

			"paymentDate",

			"installmentStatus"

	);

	@Override
	public StudentFeeInstallmentResponseDto createInstallment(StudentFeeInstallmentRequestDto request) {
		
		

		// Validate fee payment
		StudentFeePayment payment = getPayment(request.getPaymentId());
		
		boolean refundExists =
				studentFeeRefundRepository.existsByPaymentIdAndRefundStatusAndStatusNot(
						payment.getId(),
						RefundStatus.COMPLETED,
						RecordStatus.DELETED
				);

		if(refundExists){

			throw new BusinessException(
					"Installment cannot be created after completed refund."
			);
		}

		// Validate payment status
		if (payment.getPaymentStatus() == PaymentStatus.PAID) {

			throw new BusinessException(
					"Cannot create installment for a fully paid fee payment."
			);
		}

		if (payment.getPaidAmount().compareTo(payment.getFinalPayableAmount()) >= 0) {

			throw new BusinessException(
					"Cannot create installment. Fee is already fully paid."
			);
		}

		// Validate duplicate installment sequence
		validateDuplicateSequence(
				payment.getId(),
				request.getInstallmentSequence(),
				null
		);

		// Validate installment amount
		if (request.getInstallmentAmount() == null) {

			throw new BusinessException(
					"Installment amount is required"
			);
		}

		if (request.getInstallmentAmount().compareTo(BigDecimal.ZERO) <= 0) {

			throw new BusinessException(
					"Installment amount must be greater than zero"
			);
		}
		
		// Validate due date

		if (request.getDueDate() == null) {

			throw new BusinessException(
					"Due date is required."
			);
		}

		if (request.getDueDate().isBefore(LocalDate.now())) {

			throw new BusinessException(
					"Due date cannot be in the past."
			);
		}

		// Validate total installment amount
		validateInstallmentAmount(
				payment,
				request.getInstallmentAmount(),
				null
		);

		// Convert request to entity
		StudentFeeInstallment installment =
				installmentMapper.toEntity(request);

		// Set entity relationship
		installment.setPayment(payment);

		installment.setStudent(payment.getStudent());

		installment.setAdmission(payment.getAdmission());

		// Copy payment snapshot
		copyPaymentSnapshot(
				installment,
				payment
		);

		// Set installment amount details
		installment.setPaidAmount(
				BigDecimal.ZERO
		);

		installment.setRemainingAmount(
				request.getInstallmentAmount()
		);

		// Set installment status
		installment.setInstallmentStatus(
				InstallmentStatus.PENDING
		);

		installment.setStatus(
				RecordStatus.ACTIVE
		);

		// Generate installment number
		installment.setInstallmentNumber(
				generateInstallmentNumber()
		);

		// Save installment
		installment = installmentRepository.save(installment);

		return installmentMapper.toResponseDto(installment);
	}

	// =====================================================
	// Get Fee Payment
	// =====================================================

	private StudentFeePayment getPayment(Long paymentId) {

		return paymentRepository.findByIdAndStatusNot(paymentId, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Fee payment not found with ID : " + paymentId));

	}

	// =====================================================
	// Generate Installment Number
	// =====================================================

	private String generateInstallmentNumber() {

		return "SMS-INS-" + LocalDate.now().toString().replace("-", "") + "-"
				+ java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();

	}

	// =====================================================
	// Validate Duplicate Installment Sequence
	// =====================================================

	private void validateDuplicateSequence(Long paymentId, Integer installmentSequence, Long installmentId) {

		boolean exists;

		if (installmentId == null) {

			exists = installmentRepository.existsByPayment_IdAndInstallmentSequenceAndStatusNot(paymentId,
					installmentSequence, RecordStatus.DELETED);

		} else {

			exists = installmentRepository.existsByPayment_IdAndInstallmentSequenceAndStatusNotAndIdNot(paymentId,
					installmentSequence, RecordStatus.DELETED, installmentId);

		}

		if (exists) {

			throw new ResourceAlreadyExistsException(
					"Installment sequence " + installmentSequence + " already exists for this fee payment.");

		}

	}

	// =====================================================
	// Copy Payment Snapshot
	// =====================================================

	private void copyPaymentSnapshot(StudentFeeInstallment installment, StudentFeePayment payment) {
		
		// Validate payment mapping

		if(payment.getStudent()==null){

			throw new BusinessException(
					"Student mapping missing in payment."
			);
		}

		if(payment.getAdmission()==null){

			throw new BusinessException(
					"Admission mapping missing in payment."
			);
		}

		Student student = payment.getStudent();

		Admission admission = payment.getAdmission();

		installment.setStudentSnapshotId(student.getId());

		installment.setStudentName(student.getFirstName() + " " + student.getLastName());

		installment.setRollNumber(student.getRollNumber());

		installment.setAdmissionSnapshotId(admission.getId());

		installment.setAdmissionSnapshotNumber(admission.getAdmissionNumber());

		installment.setAcademicYear(payment.getAcademicYear());

	}

	// =====================================================
	// Validate Installment Amount
	// =====================================================

	private void validateInstallmentAmount(StudentFeePayment payment, BigDecimal installmentAmount,
			Long installmentId) {

		// =====================================================
		// Existing Installment Total
		// =====================================================

		BigDecimal existingInstallmentAmount = installmentRepository.getTotalInstallmentAmountByPayment(payment.getId(),
				RecordStatus.DELETED, installmentId);

		if (existingInstallmentAmount == null) {

			existingInstallmentAmount = BigDecimal.ZERO;

		}

		// =====================================================
		// Total After Current Installment
		// =====================================================

		BigDecimal totalInstallmentAmount = existingInstallmentAmount.add(installmentAmount);

		// =====================================================
		// Validate Against Final Payable Amount
		// =====================================================

		if (totalInstallmentAmount.compareTo(payment.getFinalPayableAmount()) > 0) {

			throw new BusinessException("Total installment amount cannot exceed final payable amount.");

		}

	}

	// =====================================================
	// Get Installment
	// =====================================================

	private StudentFeeInstallment getInstallment(Long installmentId) {

		return installmentRepository.findByIdAndStatusNot(installmentId, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Installment not found with ID : " + installmentId));

	}

	@Override
	@Transactional
	public StudentFeeInstallmentResponseDto payInstallment(
	        Long id,
	        PayInstallmentRequestDto request) {

	    // Validate Installment

	    StudentFeeInstallment installment = getInstallment(id);
	    
	   // Validate paid amount

	    if (request.getPaidAmount() == null) {

	    	throw new BusinessException(
	    			"Paid amount is required."
	    	);
	    }

	    if (request.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {

	    	throw new BusinessException(
	    			"Paid amount must be greater than zero."
	    	);
	    }

	    
	    // Validate payment date

	    if (request.getPaymentDate() == null) {

	    	throw new BusinessException(
	    			"Payment date is required."
	    	);
	    }
	    
	 // Validate payment mode

	    if (request.getPaymentMode() == null) {

	    	throw new BusinessException(
	    			"Payment mode is required."
	    	);
	    }
	    

	    // Validate Payment Eligibility

	    if (installment.getInstallmentStatus() == InstallmentStatus.PAID) {

	        throw new BusinessException(
	                "This installment has already been paid."
	        );
	    }


	    // Validate Payment Amount

	    if (request.getPaidAmount() == null
	            || request.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {

	        throw new BusinessException(
	                "Paid amount must be greater than zero."
	        );
	    }


	    if (request.getPaidAmount()
	            .compareTo(installment.getRemainingAmount()) > 0) {

	        throw new BusinessException(
	                "Paid amount cannot exceed remaining installment amount."
	        );
	    }


	    // Update Installment Amount Details

	    BigDecimal updatedPaidAmount =
	            installment.getPaidAmount()
	                    .add(request.getPaidAmount());


	    installment.setPaidAmount(updatedPaidAmount);


	    BigDecimal remainingAmount =
	            installment.getInstallmentAmount()
	                    .subtract(updatedPaidAmount);


	    installment.setRemainingAmount(remainingAmount);



	    // Update Payment Transaction Details

	    installment.setPaymentDate(
	            request.getPaymentDate()
	    );

	    installment.setPaymentMode(
	            request.getPaymentMode()
	    );

	    installment.setTransactionReference(
	            request.getTransactionReference()
	    );

	    installment.setRemarks(
	            request.getRemarks()
	    );



	    // Update Installment Status

	    updateInstallmentStatus(installment);



	    // Completion Audit Information

	    if (installment.getInstallmentStatus() == InstallmentStatus.PAID) {

	        installment.setCompletedByUserId(
	                SecurityUtils.getCurrentUserId()
	        );

	        installment.setCompletedBy(
	                SecurityUtils.getCurrentFullName()
	        );

	        installment.setCompletedDate(
	                LocalDateTime.now()
	        );
	    }



	    // Update Parent Payment Details

	    StudentFeePayment payment =
	            installment.getPayment();


	    BigDecimal totalPaidAmount =
	            payment.getPaidAmount()
	                    .add(request.getPaidAmount());


	    payment.setPaidAmount(
	            totalPaidAmount
	    );


	    BigDecimal dueAmount =
	            payment.getFinalPayableAmount()
	                    .subtract(totalPaidAmount);


	    if (dueAmount.compareTo(BigDecimal.ZERO) < 0) {

	        throw new BusinessException(
	                "Paid amount cannot exceed final payable amount."
	        );
	    }


	    payment.setDueAmount(
	            dueAmount
	    );



	    // Update Payment Audit Information

	    payment.setUpdatedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    payment.setUpdatedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Update Installment Audit Information

	    installment.setUpdatedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    installment.setUpdatedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    // Update Payment Status

	    updatePaymentStatus(payment);



	    // Save Payment And Installment

	    paymentRepository.save(payment);

	    StudentFeeInstallment saved =
	            installmentRepository.save(installment);



	    // Return Response

	    return installmentMapper.toResponseDto(saved);

	}
	
	
	
	// Update Installment Status

	private void updateInstallmentStatus(StudentFeeInstallment installment) {

		if (installment.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {

			installment.setInstallmentStatus(InstallmentStatus.PAID);

		} else if (installment.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {

			installment.setInstallmentStatus(InstallmentStatus.PARTIALLY_PAID);

		} else {

			installment.setInstallmentStatus(InstallmentStatus.PENDING);

		}

	}

	// Update Parent Payment Status

	private void updatePaymentStatus(StudentFeePayment payment) {

		if (payment.getDueAmount().compareTo(BigDecimal.ZERO) <= 0) {

			payment.setPaymentStatus(PaymentStatus.PAID);

		} else if (payment.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {

			payment.setPaymentStatus(PaymentStatus.PARTIAL);

		} else {

			payment.setPaymentStatus(PaymentStatus.PENDING);

		}

	}

	@Override
	public StudentFeeInstallmentResponseDto updateInstallment(Long id, StudentFeeInstallmentRequestDto request) {

		// =====================================================
		// Get Existing Installment
		// =====================================================

		StudentFeeInstallment installment = getInstallment(id);

		// =====================================================
		// Paid Validation
		// =====================================================

		if (installment.getInstallmentStatus() == InstallmentStatus.PAID) {

			throw new BusinessException("Paid installment cannot be updated.");

		}

		// =====================================================
		// Duplicate Sequence Validation
		// =====================================================

		validateDuplicateSequence(

				installment.getPayment().getId(),

				request.getInstallmentSequence(),

				id);

		// =====================================================
		// Amount Validation
		// =====================================================

		validateInstallmentAmount(

				installment.getPayment(),

				request.getInstallmentAmount(),

				id);

		// =====================================================
		// Update Allowed Fields
		// =====================================================

		installment.setInstallmentSequence(request.getInstallmentSequence());

		installment.setInstallmentAmount(request.getInstallmentAmount());

		installment.setDueDate(request.getDueDate());
		
		// Validate due date update

		if(request.getDueDate()==null){

			throw new BusinessException(
					"Due date is required."
			);
		}

		if(request.getDueDate().isBefore(LocalDate.now())){

			throw new BusinessException(
					"Due date cannot be in the past."
			);
		}

		installment.setRemarks(request.getRemarks());

		// =====================================================
		// Remaining Recalculate
		// =====================================================

		BigDecimal remainingAmount =

				request.getInstallmentAmount().subtract(installment.getPaidAmount());

		if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {

			throw new BusinessException(

					"Installment amount cannot be less than paid amount."

			);

		}

		installment.setRemainingAmount(remainingAmount);
		
		// ==========================
		// Update Audit Information
		// ==========================

		installment.setUpdatedByUserId(
		        SecurityUtils.getCurrentUserId()
		);


		installment.setUpdatedBy(
		        SecurityUtils.getCurrentFullName()
		);


		// =====================================================
		// Status Refresh
		// =====================================================

		updateInstallmentStatus(installment);

		// =====================================================
		// Save
		// =====================================================

		installment = installmentRepository.save(installment);

		return installmentMapper.toResponseDto(installment);

	}

	// =====================================================
	// Get Installment By Id
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public StudentFeeInstallmentResponseDto getInstallmentById(Long id) {

		StudentFeeInstallment installment = getInstallment(id);

		return installmentMapper.toResponseDto(installment);

	}

	// =====================================================
	// Get All Installments
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentFeeInstallmentResponseDto> getAllInstallments(int page, int size, String sortBy,
			String sortDirection) {

		Pageable pageable = buildPageable(page, size, sortBy, sortDirection);

		Page<StudentFeeInstallment> installments = installmentRepository.findByStatusNot(RecordStatus.DELETED,
				pageable);

		Page<StudentFeeInstallmentResponseDto> dtoPage = installments.map(installmentMapper::toResponseDto);

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
	public PageResponse<StudentFeeInstallmentResponseDto> searchInstallments(
			StudentFeeInstallmentSearchRequestDto request) {

		// =====================================================
		// Pageable
		// =====================================================

		Pageable pageable = buildPageable(request.getPage(), request.getSize(), request.getSortBy(),
				request.getSortDirection());

		// =====================================================
		// Specification
		// =====================================================

		Specification<StudentFeeInstallment> specification = StudentFeeInstallmentSpecification.search(request);

		// =====================================================
		// Soft Delete Condition
		// =====================================================

		specification = specification.and((root, query, cb) -> cb.notEqual(root.get("status"), RecordStatus.DELETED));

		// =====================================================
		// Fetch Data
		// =====================================================

		Page<StudentFeeInstallment> pageResult = installmentRepository.findAll(specification, pageable);

		// =====================================================
		// Entity -> DTO
		// =====================================================

		Page<StudentFeeInstallmentResponseDto> dtoPage = pageResult.map(installmentMapper::toResponseDto);

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
	// SOFT DELETE INSTALLMENT
	// =====================================================

	@Override
	public void deleteInstallment(Long id) {

	    StudentFeeInstallment installment = getInstallment(id);


	    // Validate Paid Installment

	    if (installment.getInstallmentStatus() == InstallmentStatus.PAID) {

	        throw new BusinessException(
	                "Paid installment cannot be deleted."
	        );
	    }
	    
	 // Validate paid installment

	    if (installment.getInstallmentStatus() == InstallmentStatus.PAID) {

	    	throw new BusinessException(
	    			"Paid installment cannot be deleted."
	    	);
	    }


	    // Validate partially paid installment

	    if (installment.getPaidAmount()
	    		.compareTo(BigDecimal.ZERO) > 0) {

	    	throw new BusinessException(
	    			"Partially paid installment cannot be deleted."
	    	);
	    }


	    // Set Delete Audit Information

	    installment.setDeletedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );


	    installment.setDeletedBy(
	            SecurityUtils.getCurrentFullName()
	    );


	    installment.setDeletedDate(
	            LocalDateTime.now()
	    );


	    // Perform Soft Delete

	    installment.setStatus(
	            RecordStatus.DELETED
	    );


	    installmentRepository.save(installment);

	}

	// =====================================================
	// Build Pageable
	// =====================================================

	private Pageable buildPageable(int page, int size, String sortBy, String sortDirection) {

		if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {

			sortBy = "id";

		}

		Sort sort;

		if ("desc".equalsIgnoreCase(sortDirection)) {

			sort = Sort.by(sortBy).descending();

		} else {

			sort = Sort.by(sortBy).ascending();

		}

		return PageRequest.of(page, size, sort);

	}

}