package com.sms.fee.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admission.entity.Admission;
import com.admission.enums.AdmissionStatus;
import com.admission.repository.AdmissionRepository;
import com.sms.dto.PageResponse;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.fee.dto.StudentFeePaymentRequestDto;
import com.sms.fee.dto.StudentFeePaymentResponseDto;
import com.sms.fee.dto.StudentFeePaymentSearchRequest;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.mapper.StudentFeePaymentMapper;
import com.sms.fee.repository.FeeStructureRepository;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.fee.service.StudentFeePaymentService;
import com.sms.fee.specification.StudentFeePaymentSpecification;
import com.sms.repository.StudentRepository;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.repository.StudentScholarshipRepository;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentFeePaymentServiceImpl implements StudentFeePaymentService {

	private final StudentFeePaymentRepository studentFeePaymentRepository;

	private final StudentRepository studentRepository;

	private final AdmissionRepository admissionRepository;

	private final FeeStructureRepository feeStructureRepository;

	private final StudentFeePaymentMapper mapper;

	private final StudentScholarshipRepository scholarshipRepository;
	
	private final StudentFeeReceiptRepository studentFeeReceiptRepository;

	@Override
	public StudentFeePaymentResponseDto createPayment(StudentFeePaymentRequestDto dto) {

		// =====================================================
		// 1. Validate Student
		// =====================================================

		Student student = studentRepository.findByIdAndStatusNot(dto.getStudentId(), RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		// =====================================================
		// 2. Validate Admission
		// =====================================================

		Admission admission = admissionRepository
				.findByIdAndAdmissionStatus(dto.getAdmissionId(), AdmissionStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Active admission not found"));

		// =====================================================
		// 3. Validate Student & Admission
		// =====================================================

		if (!admission.getStudent().getId().equals(student.getId())) {

			throw new BusinessException("Admission does not belong to the selected student");
		}

		// =====================================================
		// 4. Validate Academic Year
		// =====================================================

		if (!admission.getAcademicYear().equals(dto.getAcademicYear())) {

			throw new BusinessException("Academic year does not match admission");
		}

		// =====================================================
		// 5. Duplicate Payment Validation
		// =====================================================

		boolean exists = studentFeePaymentRepository.existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNot(
				dto.getStudentId(), dto.getAdmissionId(), dto.getAcademicYear(), RecordStatus.DELETED);

		if (exists) {

			throw new BusinessException("Fee payment already exists for this academic year");
		}

		// =====================================================
		// 6. Calculate Total Fee
		// =====================================================

		BigDecimal totalFee = feeStructureRepository.findTotalFeeByCourseAndSemester(admission.getCourse().getId(),
				admission.getSemester().getId(), dto.getAcademicYear());

		if (totalFee == null || totalFee.compareTo(BigDecimal.ZERO) <= 0) {

			throw new BusinessException("Fee structure not configured");
		}

		// =====================================================
		// 7. Scholarship Validation
		// =====================================================

		StudentScholarship scholarship = null;

		BigDecimal scholarshipAmount = BigDecimal.ZERO;

		if (dto.getScholarshipId() != null) {

			scholarship = scholarshipRepository.findByIdAndStatusNot(dto.getScholarshipId(), RecordStatus.DELETED)
					.orElseThrow(() -> new ResourceNotFoundException("Scholarship not found"));

			// =====================================================
			// Validate Scholarship Owner Student
			// =====================================================

			if (!scholarship.getStudent().getId().equals(student.getId())) {

				throw new BusinessException("Scholarship does not belong to the selected student");
			}

			// =====================================================
			// Validate Scholarship Admission
			// =====================================================

			if (!scholarship.getAdmission().getId().equals(admission.getId())) {

				throw new BusinessException("Scholarship does not belong to the selected admission");
			}

			// =====================================================
			// Validate Academic Year
			// =====================================================

			if (!scholarship.getAcademicYear().equals(dto.getAcademicYear())) {

				throw new BusinessException("Scholarship academic year mismatch");
			}

			// =====================================================
			// Validate Scholarship Approval Status
			// =====================================================

			if (scholarship.getScholarshipStatus() == null
					|| scholarship.getScholarshipStatus() != ScholarshipStatus.APPROVED) {

				throw new BusinessException("Only approved scholarship can be used for fee payment");
			}

			// =====================================================
			// Validate Approved Amount
			// =====================================================

			if (scholarship.getApprovedAmount() == null) {

				throw new BusinessException("Scholarship approved amount is missing");
			}

			scholarshipAmount = scholarship.getApprovedAmount();

			// =====================================================
			// Validate Scholarship Amount
			// =====================================================

			if (scholarshipAmount.compareTo(totalFee) > 0) {

				throw new BusinessException("Scholarship amount cannot exceed total fee");
			}

		}

		// =====================================================
		// 8. Calculate Final Payable Fee
		// =====================================================

		BigDecimal payableFee = totalFee.subtract(scholarshipAmount);


		if (payableFee.compareTo(BigDecimal.ZERO) < 0) {

			payableFee = BigDecimal.ZERO;
		}

		// =====================================================
		// Validate Paid Amount Against Final Payable Fee
		// =====================================================

		validatePaymentAmount(dto.getPaidAmount(), payableFee);

		// =====================================================
		// 10. Calculate Due Amount
		// =====================================================

		BigDecimal dueAmount = payableFee.subtract(dto.getPaidAmount());

		// =====================================================
		// 11. Calculate Payment Status
		// =====================================================

		PaymentStatus paymentStatus = calculatePaymentStatus(dto.getPaidAmount(), payableFee);

		// =====================================================
		// 12. DTO -> Entity
		// =====================================================

		StudentFeePayment payment = mapper.toEntity(dto);

		payment.setStudent(student);

		payment.setAdmission(admission);

		payment.setScholarship(scholarship);

		payment.setTotalFee(totalFee);

		payment.setFinalPayableAmount(payableFee);

		// =====================================================
		// Scholarship Snapshot
		// =====================================================

		payment.setScholarshipAmount(scholarshipAmount);

		if (scholarship != null) {

			payment.setScholarshipName(scholarship.getScholarshipName());

			payment.setScholarshipType(scholarship.getScholarshipType());

		} else {

			payment.setScholarshipName(null);

			payment.setScholarshipType(null);
		}

		payment.setDueAmount(dueAmount);

		payment.setPaymentStatus(paymentStatus);

		payment.setStatus(RecordStatus.ACTIVE);

		payment.setCreatedByUserId(SecurityUtils.getCurrentUserId());
		payment.setCreatedBy(SecurityUtils.getCurrentFullName());
		payment.setCreatedAt(LocalDateTime.now());

		// =====================================================
		// 13. Save Payment
		// =====================================================

		StudentFeePayment saved = studentFeePaymentRepository.save(payment);

		// =====================================================
		// 14. Return Response
		// =====================================================

		return mapper.toDto(saved);

	}

	// =====================================================
	// GET BY ID
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public StudentFeePaymentResponseDto getPaymentById(Long id) {

		StudentFeePayment payment = studentFeePaymentRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Fee payment not found with id : " + id));

		return mapper.toDto(payment);
	}
	
	

	@Override
	public StudentFeePaymentResponseDto updatePayment(Long id, StudentFeePaymentRequestDto dto) {

		// =====================================================
		// 1. Validate Existing Payment
		// =====================================================

		StudentFeePayment payment = studentFeePaymentRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Fee payment not found"));
		
		// =====================================================
		// 2. Prevent Update After Receipt Generation
		// =====================================================

		boolean receiptExists =
				studentFeeReceiptRepository.existsByPaymentIdAndStatusNot(
		                id,
		                RecordStatus.DELETED
		        );

		if (receiptExists) {

		    throw new BusinessException(
		            "Payment cannot be modified after receipt generation"
		    );

		}

		// =====================================================
		// 2. Validate Student
		// =====================================================

		Student student = studentRepository.findByIdAndStatusNot(dto.getStudentId(), RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		// =====================================================
		// 3. Validate Admission
		// =====================================================

		Admission admission = admissionRepository
				.findByIdAndAdmissionStatus(dto.getAdmissionId(), AdmissionStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Active admission not found"));

		// =====================================================
		// 4. Validate Student & Admission
		// =====================================================

		if (!admission.getStudent().getId().equals(student.getId())) {

			throw new BusinessException("Admission does not belong to the selected student");
		}

		// =====================================================
		// 5. Validate Academic Year
		// =====================================================

		if (!admission.getAcademicYear().equals(dto.getAcademicYear())) {

			throw new BusinessException("Academic year does not match admission");
		}

		// =====================================================
		// 6. Duplicate Payment Validation
		// =====================================================

		boolean exists = studentFeePaymentRepository.existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNotAndIdNot(
				dto.getStudentId(), dto.getAdmissionId(), dto.getAcademicYear(), RecordStatus.DELETED, id);

		if (exists) {

			throw new BusinessException("Fee payment already exists for this academic year");
		}

		// =====================================================
		// 7. Calculate Total Fee
		// =====================================================

		BigDecimal totalFee = feeStructureRepository.findTotalFeeByCourseAndSemester(admission.getCourse().getId(),
				admission.getSemester().getId(), dto.getAcademicYear());

		// =====================================================
		// 8. Validate Fee Structure
		// =====================================================

		if (totalFee == null || totalFee.compareTo(BigDecimal.ZERO) <= 0) {

			throw new BusinessException("Fee structure not configured");
		}

		// =====================================================
		// 9. Scholarship Validation
		// =====================================================

		BigDecimal scholarshipAmount = BigDecimal.ZERO;

		StudentScholarship scholarship = null;

		if (dto.getScholarshipId() != null) {

			scholarship = scholarshipRepository.findByIdAndStatusNot(dto.getScholarshipId(), RecordStatus.DELETED)
					.orElseThrow(() -> new ResourceNotFoundException("Scholarship not found"));

			if (!scholarship.getStudent().getId().equals(student.getId())) {

				throw new BusinessException("Scholarship does not belong to the selected student");
			}

			if (!scholarship.getAdmission().getId().equals(admission.getId())) {

				throw new BusinessException("Scholarship does not belong to the selected admission");
			}

			if (!scholarship.getAcademicYear().equals(dto.getAcademicYear())) {

				throw new BusinessException("Scholarship academic year mismatch");
			}

			if (scholarship.getScholarshipStatus() == null
					|| scholarship.getScholarshipStatus() != ScholarshipStatus.APPROVED) {

				throw new BusinessException("Only approved scholarship can be used for fee payment");
			}

			if (scholarship.getApprovedAmount() == null) {

				throw new BusinessException("Scholarship approved amount is missing");
			}

			scholarshipAmount = scholarship.getApprovedAmount();

			if (scholarshipAmount.compareTo(totalFee) > 0) {

				throw new BusinessException("Scholarship amount cannot exceed total fee");
			}
		}

		// =====================================================
		// 10. Calculate Final Payable Fee
		// =====================================================

		BigDecimal payableFee = totalFee.subtract(scholarshipAmount);

		if (payableFee.compareTo(BigDecimal.ZERO) < 0) {

			payableFee = BigDecimal.ZERO;
		}

		// =====================================================
		// Validate Paid Amount Against Final Payable Fee
		// =====================================================

		validatePaymentAmount(dto.getPaidAmount(), payableFee);

		// =====================================================
		// 12. Calculate Due Amount
		// =====================================================

		BigDecimal dueAmount = payableFee.subtract(dto.getPaidAmount());

		// =====================================================
		// 13. Calculate Payment Status
		// =====================================================

		PaymentStatus paymentStatus = calculatePaymentStatus(dto.getPaidAmount(), payableFee);

		// =====================================================
		// 14. Update Entity
		// =====================================================

		mapper.updateEntity(payment, dto);

		payment.setStudent(student);

		payment.setAdmission(admission);

		payment.setScholarship(scholarship);

		payment.setTotalFee(totalFee);

		payment.setFinalPayableAmount(payableFee);

		// =====================================================
		// Scholarship Snapshot
		// =====================================================

		payment.setScholarshipAmount(scholarshipAmount);

		if (scholarship != null) {

			payment.setScholarshipName(scholarship.getScholarshipName());

			payment.setScholarshipType(scholarship.getScholarshipType());

		} else {

			payment.setScholarshipName(null);

			payment.setScholarshipType(null);
		}

		payment.setDueAmount(dueAmount);

		payment.setPaymentStatus(paymentStatus);

		payment.setStatus(RecordStatus.ACTIVE);

		payment.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
		payment.setUpdatedBy(SecurityUtils.getCurrentFullName());
		payment.setUpdatedAt(LocalDateTime.now());;

		// =====================================================
		// 15. Save
		// =====================================================

		StudentFeePayment updated = studentFeePaymentRepository.save(payment);

		// =====================================================
		// 16. Return Response
		// =====================================================

		return mapper.toDto(updated);
	}

	// =====================================================
	// DELETE (SOFT DELETE)
	// =====================================================

	@Override
	@Transactional
	public void deletePayment(Long id) {

	    StudentFeePayment payment =
	            studentFeePaymentRepository.findByIdAndStatusNot(
	                    id,
	                    RecordStatus.DELETED
	            )
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Fee payment not found")
	            );


	    // =====================================================
	    // Validation 1 : Receipt Exists
	    // =====================================================

	    boolean receiptExists =
	    		studentFeeReceiptRepository.existsByPaymentIdAndStatusNot(
	                    id,
	                    RecordStatus.DELETED
	            );

	    if (receiptExists) {

	        throw new BusinessException(
	                "Cannot delete payment because receipt already exists"
	        );

	    }


	    // =====================================================
	    // Validation 2 : Refund Exists
	    // =====================================================

	    boolean refundExists =
	    		studentFeeReceiptRepository.existsByPaymentIdAndStatusNot(
	                    id,
	                    RecordStatus.DELETED
	            );

	    if (refundExists) {

	        throw new BusinessException(
	                "Cannot delete payment because refund exists"
	        );

	    }


	    // =====================================================
	    // Validation 3 : Paid Payment Cannot Delete
	    // =====================================================

	    if (payment.getPaymentStatus() == PaymentStatus.PAID) {

	        throw new BusinessException(
	                "Paid payment cannot be deleted"
	        );

	    }


	    // =====================================================
	    // Soft Delete
	    // =====================================================

	    payment.setStatus(RecordStatus.DELETED);


	    // =====================================================
	    // Audit Information
	    // =====================================================

	    payment.setDeletedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    payment.setDeletedBy(
	            SecurityUtils.getCurrentFullName()
	    );

	    payment.setDeletedDate(
	            LocalDateTime.now()
	    );


	    studentFeePaymentRepository.save(payment);

	}
	
	

	// =====================================================
	// GET ALL PAGINATION
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentFeePaymentResponseDto> getAllPayments(int page, int size, String sortBy,
			String direction) {

		Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<StudentFeePayment> result = studentFeePaymentRepository.findByStatusNot(RecordStatus.DELETED, pageable);

		List<StudentFeePaymentResponseDto> content = result.getContent().stream().map(mapper::toDto).toList();

		PageResponse<StudentFeePaymentResponseDto> response = new PageResponse<>();

		response.setContent(content);

		response.setPageNumber(result.getNumber());

		response.setPageSize(result.getSize());

		response.setTotalElements(result.getTotalElements());

		response.setTotalPages(result.getTotalPages());

		response.setLast(result.isLast());

		return response;

	}

	// =====================================================
	// SEARCH
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentFeePaymentResponseDto> searchPayments(
	        StudentFeePaymentSearchRequest request,
	        int page,
	        int size,
	        String sortBy,
	        String direction) {


	    // =====================================================
	    // SORT
	    // =====================================================

	    Sort sort =
	            direction.equalsIgnoreCase("asc")
	                    ? Sort.by(sortBy).ascending()
	                    : Sort.by(sortBy).descending();


	    // =====================================================
	    // PAGINATION
	    // =====================================================

	    Pageable pageable =
	            PageRequest.of(
	                    page,
	                    size,
	                    sort
	            );


	    // =====================================================
	    // BUILD SPECIFICATION
	    // =====================================================

	    Specification<StudentFeePayment> specification =
	            StudentFeePaymentSpecification.getSpecification(
	                    request.getStudentId(),
	                    request.getAdmissionId(),
	                    request.getAcademicYear(),
	                    request.getPaymentStatus(),
	                    request.getPaymentMode(),
	                    request.getMinPaidAmount(),
	                    request.getMaxPaidAmount(),
	                    request.getStatus()
	            );


	    // =====================================================
	    // EXECUTE SEARCH
	    // =====================================================

	    Page<StudentFeePayment> result =
	            studentFeePaymentRepository.findAll(
	                    specification,
	                    pageable
	            );


	    // =====================================================
	    // MAP RESPONSE
	    // =====================================================

	    List<StudentFeePaymentResponseDto> content =
	            result.getContent()
	                    .stream()
	                    .map(mapper::toDto)
	                    .toList();


	    // =====================================================
	    // BUILD RESPONSE
	    // =====================================================

	    PageResponse<StudentFeePaymentResponseDto> response =
	            new PageResponse<>();


	    response.setContent(content);

	    response.setPageNumber(
	            result.getNumber()
	    );

	    response.setPageSize(
	            result.getSize()
	    );

	    response.setTotalElements(
	            result.getTotalElements()
	    );

	    response.setTotalPages(
	            result.getTotalPages()
	    );

	    response.setLast(
	            result.isLast()
	    );


	    return response;
	}

	// =====================================================
	// PAYMENT STATUS CALCULATION
	// =====================================================

	private PaymentStatus calculatePaymentStatus(BigDecimal paidAmount, BigDecimal finalPayableAmount) {

		if (paidAmount == null || paidAmount.compareTo(BigDecimal.ZERO) == 0) {

			return PaymentStatus.PENDING;
		}

		if (paidAmount.compareTo(finalPayableAmount) == 0) {

			return PaymentStatus.PAID;
		}

		return PaymentStatus.PARTIAL;

	}

	// =====================================================
	// Validate Payment Amount
	// =====================================================

	private void validatePaymentAmount(BigDecimal paidAmount, BigDecimal finalPayableAmount) {

		if (paidAmount == null) {

			throw new BusinessException("Paid amount is required");

		}

		if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {

			throw new BusinessException("Paid amount cannot be negative");

		}

		if (paidAmount.compareTo(finalPayableAmount) > 0) {

			throw new BusinessException("Paid amount cannot exceed final payable amount");

		}

	}

}
