package com.sms.scholarship.serviceImpl;


import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.sms.fee.repository.FeeStructureRepository;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.repository.StudentRepository;
import com.sms.scholarship.dto.StudentScholarshipRequestDto;
import com.sms.scholarship.dto.StudentScholarshipResponseDto;
import com.sms.scholarship.dto.StudentScholarshipSearchRequest;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;
import com.sms.scholarship.mapper.StudentScholarshipMapper;
import com.sms.scholarship.repository.StudentScholarshipRepository;
import com.sms.scholarship.service.StudentScholarshipService;
import com.sms.scholarship.specification.StudentScholarshipSpecification;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional
public class StudentScholarshipServiceImpl implements StudentScholarshipService {

	private final StudentScholarshipRepository scholarshipRepository;

	private final StudentRepository studentRepository;

	private final AdmissionRepository admissionRepository;

	private final FeeStructureRepository feeStructureRepository;
	
	private final StudentFeePaymentRepository studentFeePaymentRepository;

	private final StudentScholarshipMapper mapper;

	// =====================================================
	// CREATE SCHOLARSHIP
	// =====================================================

	@Override
	public StudentScholarshipResponseDto createScholarship(StudentScholarshipRequestDto dto) {

		// =====================================================
		// Validate Student
		// =====================================================

		Student student = studentRepository.findByIdAndStatusNot(dto.getStudentId(), RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		// =====================================================
		// Duplicate Check
		// =====================================================

		boolean exists = scholarshipRepository.existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNot(
				dto.getStudentId(), dto.getAdmissionId(), dto.getAcademicYear(), RecordStatus.DELETED);

		if (exists) {

			throw new BusinessException("Scholarship already exists for this academic year");
		}

		// =====================================================
		// Validate Admission
		// =====================================================

		Admission admission = admissionRepository
				.findByIdAndAdmissionStatus(dto.getAdmissionId(), AdmissionStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Active admission not found"));

		// Validate Admission belongs to Student

		if (!admission.getStudent().getId().equals(student.getId())) {

			throw new BusinessException("Admission does not belong to the selected student");
		}
		
		if (!admission.getAcademicYear().equals(dto.getAcademicYear())) {

		    throw new BusinessException(
		            "Academic year does not match admission"
		    );
		}

		// =====================================================
		// Calculate Course Fee
		// =====================================================

		BigDecimal totalFee = feeStructureRepository.findTotalFeeByCourseAndSemester(admission.getCourse().getId(),
				admission.getSemester().getId(), dto.getAcademicYear());

		if (totalFee == null || totalFee.compareTo(BigDecimal.ZERO) == 0) {

			throw new BusinessException("Fee structure not configured");
		}

		// =====================================================
		// Calculate Approved Scholarship
		// =====================================================

		BigDecimal approvedAmount = calculateApprovedAmount(totalFee, dto.getScholarshipType(),
				dto.getScholarshipValue());

		// =====================================================
		// DTO -> ENTITY
		// =====================================================

		StudentScholarship scholarship = mapper.toEntity(dto);

		scholarship.setStudent(student);

		scholarship.setAdmission(admission);

		scholarship.setRequestedAmount(approvedAmount);

		scholarship.setApprovedAmount(null);

		scholarship.setScholarshipStatus(ScholarshipStatus.PENDING);

		scholarship.setStatus(RecordStatus.ACTIVE);

		// Create Information
		scholarship.setCreatedByUserId(SecurityUtils.getCurrentUserId());
		scholarship.setCreatedBy(SecurityUtils.getCurrentFullName());

		// =====================================================
		// Save
		// =====================================================

		StudentScholarship saved = scholarshipRepository.save(scholarship);

		return mapper.toDto(saved);

	}

	// =====================================================
	// GET BY ID
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public StudentScholarshipResponseDto getScholarshipById(Long id) {

		StudentScholarship scholarship = scholarshipRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Scholarship not found with id : " + id));

		return mapper.toDto(scholarship);

	}

	// =====================================================
	// UPDATE SCHOLARSHIP
	// =====================================================

	@Override
	public StudentScholarshipResponseDto updateScholarship(Long id, StudentScholarshipRequestDto dto) {

		// =====================================================
		// Validate Scholarship
		// =====================================================

		StudentScholarship scholarship = scholarshipRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Scholarship not found"));

		// =====================================================
		// Approved Scholarship Cannot Be Updated
		// =====================================================

		if (scholarship.getScholarshipStatus() == ScholarshipStatus.APPROVED) {

			throw new BusinessException("Approved scholarship cannot be updated");
		}
		
		// =====================================================
		// Scholarship Already Used In Fee Payment
		// =====================================================

		boolean paymentExists =
		        studentFeePaymentRepository.existsByScholarshipIdAndStatusNot(
		                id,
		                RecordStatus.DELETED
		        );

		if (paymentExists) {

		    throw new BusinessException(
		            "Scholarship is already used in fee payment"
		    );

		}

		// =====================================================
		// Duplicate Check
		// =====================================================

		boolean exists = scholarshipRepository.existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNotAndIdNot(
				dto.getStudentId(), dto.getAdmissionId(), dto.getAcademicYear(), RecordStatus.DELETED, id);

		if (exists) {

			throw new BusinessException("Scholarship already exists for this academic year");
		}

		// =====================================================
		// Validate Student
		// =====================================================

		Student student = studentRepository.findByIdAndStatusNot(dto.getStudentId(), RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		// =====================================================
		// Validate Admission
		// =====================================================

		Admission admission = admissionRepository
				.findByIdAndAdmissionStatus(dto.getAdmissionId(), AdmissionStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Active admission not found"));

		// =====================================================
		// Validate Admission Belongs To Student
		// =====================================================

		if (!admission.getStudent().getId().equals(student.getId())) {

			throw new BusinessException("Admission does not belong to the selected student");
		}

		// =====================================================
		// Validate Academic Year
		// =====================================================

		if (!admission.getAcademicYear().equals(dto.getAcademicYear())) {

			throw new BusinessException("Academic year does not match admission");
		}

		// =====================================================
		// Calculate Scholarship Amount
		// =====================================================

		BigDecimal totalFee = feeStructureRepository.findTotalFeeByCourseAndSemester(admission.getCourse().getId(),
				admission.getSemester().getId(), dto.getAcademicYear());

		if (totalFee == null || totalFee.compareTo(BigDecimal.ZERO) <= 0) {

			throw new BusinessException("Fee structure not configured");
		}

		BigDecimal requestedAmount = calculateApprovedAmount(totalFee, dto.getScholarshipType(),
				dto.getScholarshipValue());

		// =====================================================
		// Update Entity
		// =====================================================

		mapper.updateEntity(dto, scholarship);

		scholarship.setStudent(student);

		scholarship.setAdmission(admission);

		scholarship.setRequestedAmount(requestedAmount);

		// Reset Approval And Rejection Workflow

		scholarship.setApprovedAmount(null);

		scholarship.setScholarshipStatus(
		        ScholarshipStatus.PENDING
		);

		scholarship.setApprovedByUserId(null);

		scholarship.setApprovedBy(null);

		scholarship.setApprovalDate(null);

		scholarship.setRejectedByUserId(null);

		scholarship.setRejectedBy(null);

		scholarship.setRejectedDate(null);

		scholarship.setRejectionReason(null);
		
		scholarship.setStatus(RecordStatus.ACTIVE);

		// Update Information
		scholarship.setUpdatedByUserId(SecurityUtils.getCurrentUserId());
		scholarship.setUpdatedBy(SecurityUtils.getCurrentFullName());

		// =====================================================
		// Save
		// =====================================================

		StudentScholarship updated = scholarshipRepository.save(scholarship);

		return mapper.toDto(updated);
	}

	// =====================================================
	// SOFT DELETE
	// =====================================================

	@Override
	public void deleteScholarship(Long id) {

		StudentScholarship scholarship = scholarshipRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Scholarship not found"));
		
		if(scholarship.getScholarshipStatus()==ScholarshipStatus.APPROVED){

		    throw new BusinessException(
		            "Approved scholarship cannot be deleted"
		    );

		}
		
		boolean used =
				studentFeePaymentRepository
				.existsByScholarshipIdAndStatusNot(
				        id,
				        RecordStatus.DELETED
				);

				if(used){

				    throw new BusinessException(
				        "Scholarship is already used in fee payment"
				    );

				}

		scholarship.setStatus(RecordStatus.DELETED);
		
		// =====================================================
		// Delete Information
		// =====================================================

		scholarship.setDeletedByUserId(
		        SecurityUtils.getCurrentUserId()
		);

		scholarship.setDeletedBy(
		        SecurityUtils.getCurrentFullName()
		);

		scholarship.setDeletedDate(
		        LocalDateTime.now()
		);

		scholarshipRepository.save(scholarship);

	}

	// =====================================================
	// GET ALL PAGINATION
	// =====================================================

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentScholarshipResponseDto> getAllScholarships(int page, int size, String sortBy,
			String direction) {

		Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<StudentScholarship> result = scholarshipRepository.findByStatusNot(RecordStatus.DELETED, pageable);

		List<StudentScholarshipResponseDto> content = result.getContent().stream().map(mapper::toDto).toList();

		PageResponse<StudentScholarshipResponseDto> response = new PageResponse<>();

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
	public PageResponse<StudentScholarshipResponseDto> searchScholarships(StudentScholarshipSearchRequest request,
			int page, int size, String sortBy, String direction) {

		Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<StudentScholarship> specification = StudentScholarshipSpecification.getSpecification(
				request.getStudentId(), request.getAdmissionId(), request.getAcademicYear(),
				request.getScholarshipName(), request.getScholarshipType(), request.getMinApprovedAmount(),
				request.getMaxApprovedAmount(), request.getStatus(), request.getScholarshipStatus());

		Page<StudentScholarship> result = scholarshipRepository.findAll(specification, pageable);

		List<StudentScholarshipResponseDto> content = result.getContent().stream().map(mapper::toDto).toList();

		PageResponse<StudentScholarshipResponseDto> response = new PageResponse<>();

		response.setContent(content);

		response.setPageNumber(result.getNumber());

		response.setPageSize(result.getSize());

		response.setTotalElements(result.getTotalElements());

		response.setTotalPages(result.getTotalPages());

		response.setLast(result.isLast());

		return response;

	}

	// =====================================================
	// CALCULATE APPROVED AMOUNT
	// =====================================================

	private BigDecimal calculateApprovedAmount(
			BigDecimal totalFee, ScholarshipType type, BigDecimal value) {

		if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {

			throw new BusinessException("Invalid scholarship value");
		}

		// Percentage Based

		if (type == ScholarshipType.PERCENTAGE) {

			if (value.compareTo(BigDecimal.valueOf(100)) > 0) {

				throw new BusinessException("Percentage cannot exceed 100");
			}

			return totalFee.multiply(value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

		}

		// Fixed Amount Based

		if (type == ScholarshipType.FIXED_AMOUNT) {

			if (value.compareTo(totalFee) > 0) {

				throw new BusinessException("Scholarship amount cannot exceed total fee");
			}

			return value.setScale(2, RoundingMode.HALF_UP);
		}

		throw new BusinessException("Invalid scholarship type");

	}

	@Override
	public StudentScholarshipResponseDto approveScholarship(Long id) {

		StudentScholarship scholarship = scholarshipRepository.findByIdAndStatusNot(id, RecordStatus.DELETED)
				.orElseThrow(() -> new ResourceNotFoundException("Scholarship not found"));

		// =====================================================
		// Only Pending Scholarship Can Be Approved
		// =====================================================

		if (scholarship.getScholarshipStatus() != ScholarshipStatus.PENDING) {

		    throw new BusinessException(
		            "Only pending scholarship can be approved"
		    );

		}

		// =====================================================
		// Approval Information
		// =====================================================

		scholarship.setApprovedAmount(
		        scholarship.getRequestedAmount()
		);

		scholarship.setScholarshipStatus(
		        ScholarshipStatus.APPROVED
		);

		// Clear Previous Rejection Reason
		scholarship.setRejectionReason(null);

		scholarship.setApprovedByUserId(
		        SecurityUtils.getCurrentUserId()
		);

		scholarship.setApprovedBy(
		        SecurityUtils.getCurrentFullName()
		);

		scholarship.setApprovalDate(
		        LocalDateTime.now()
		);

		StudentScholarship updated = scholarshipRepository.save(scholarship);

		return mapper.toDto(updated);
	}

	@Override
	public StudentScholarshipResponseDto rejectScholarship(Long id, String reason) {

	    StudentScholarship scholarship = scholarshipRepository
	            .findByIdAndStatusNot(id, RecordStatus.DELETED)
	            .orElseThrow(() -> new ResourceNotFoundException("Scholarship not found"));


	    // Validate Reject Request

	    if (scholarship.getScholarshipStatus() == ScholarshipStatus.REJECTED) {

	        throw new BusinessException(
	                "Scholarship already rejected"
	        );
	    }


	    if (scholarship.getScholarshipStatus() == ScholarshipStatus.APPROVED) {

	        throw new BusinessException(
	                "Approved scholarship cannot be rejected"
	        );
	    }


	    if (reason == null || reason.trim().isEmpty()) {

	        throw new BusinessException(
	                "Rejection reason is required"
	        );
	    }


	    // Update Rejection Information

	    scholarship.setScholarshipStatus(
	            ScholarshipStatus.REJECTED
	    );

	    scholarship.setRejectionReason(
	            reason.trim()
	    );


	    // Clear Approval Information

	    scholarship.setApprovedAmount(null);

	    scholarship.setApprovedByUserId(null);

	    scholarship.setApprovedBy(null);

	    scholarship.setApprovalDate(null);


	    // Set Reject Audit Information

	    scholarship.setRejectedByUserId(
	            SecurityUtils.getCurrentUserId()
	    );

	    scholarship.setRejectedBy(
	            SecurityUtils.getCurrentFullName()
	    );

	    scholarship.setRejectedDate(
	            LocalDateTime.now()
	    );


	    StudentScholarship updated =
	            scholarshipRepository.save(scholarship);


	    return mapper.toDto(updated);
	}
}
