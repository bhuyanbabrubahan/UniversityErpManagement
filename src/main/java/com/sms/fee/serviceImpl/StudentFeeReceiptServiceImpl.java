package com.sms.fee.serviceImpl;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.fee.dto.StudentFeeReceiptRequestDto;
import com.sms.fee.dto.StudentFeeReceiptResponseDto;
import com.sms.fee.dto.StudentFeeReceiptSearchRequest;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.mapper.StudentFeeReceiptMapper;
import com.sms.fee.refund.enums.RefundStatus;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.fee.repository.StudentFeePaymentRepository;
import com.sms.fee.repository.StudentFeeReceiptRepository;
import com.sms.fee.service.ReceiptPdfService;
import com.sms.fee.service.StudentFeeReceiptService;
import com.sms.fee.specification.StudentFeeReceiptSpecification;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional
public class StudentFeeReceiptServiceImpl
        implements StudentFeeReceiptService {




    private final StudentFeeReceiptRepository receiptRepository;

    private final StudentFeePaymentRepository paymentRepository;

    private final StudentFeeRefundRepository studentFeeRefundRepository;
    
    private final StudentFeeReceiptMapper mapper;
    
    private final ReceiptPdfService receiptPdfService;





	    // =====================================================
	    // Generate Receipt
	    // =====================================================
	
	
	    @AuditLog(
	    	    module = "STUDENT_FEE_RECEIPT",
	    	    action = AuditAction.CREATE,
	    	    description = "Generate fee receipt"
	    	)
	    @Override
	    public StudentFeeReceiptResponseDto generateReceipt(
	            Long paymentId,
	            StudentFeeReceiptRequestDto dto) {
	
	
	
	        // 1. Validate Payment
	
	    	StudentFeePayment payment =
	    	        paymentRepository
	    	        .findByIdWithScholarship(
	    	                paymentId,
	    	                RecordStatus.DELETED
	    	        )
	    	        .orElseThrow(() ->
	    	                new ResourceNotFoundException(
	    	                        "Fee payment not found"
	    	                )
	    	        );
	
	    	if (payment.getPaidAmount() == null
	    	        || payment.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {

	    	    throw new 
	    	    	BusinessException("Receipt cannot be generated for zero payment");
	    	}
	
		     // Check Deleted Receipt Restore
		
		     Optional<StudentFeeReceipt> deletedReceipt =
		    		 receiptRepository.findByPaymentIdAndStatus(
		                     paymentId,
		                     RecordStatus.DELETED
		             );
		     
		     if (payment.getPaymentStatus() == PaymentStatus.PENDING
		    	        || payment.getPaymentStatus() == PaymentStatus.CANCELLED) {

		    	    throw new 
		    	    	BusinessException("Receipt cannot be restored for pending/cancelled payment");
		    	}
		
		
		     if(deletedReceipt.isPresent()) {
		
		         StudentFeeReceipt receipt = deletedReceipt.get();
		
		         receipt.setStatus(
		                 RecordStatus.ACTIVE
		         );
		      // Clear Delete Audit Information

		      receipt.setDeletedByUserId(null);
		      receipt.setDeletedBy(null);
		      receipt.setDeletedDate(null);
		      
		      // Creation Audit Information

		      receipt.setCreatedByUserId(
		              SecurityUtils.getCurrentUserId()
		      );

		      receipt.setCreatedBy(
		              SecurityUtils.getCurrentFullName()
		      );

		      receipt.setReceiptDate(
		                 LocalDate.now()
		      );
		
		      receipt.setGeneratedBy(
		              SecurityUtils.getCurrentUserId()
		      );
		      
		      receipt.setGeneratedByName(
		    	        SecurityUtils.getCurrentFullName()
		    	);
		      
		         
		         StudentFeeReceipt saved = 
		                 receiptRepository.save(receipt);
		
		         return mapper.toDto(saved);
		     }
	
	
	
	        // 2. Duplicate Receipt Check
	
	
	        boolean exists =
	                receiptRepository
	                .existsByPaymentIdAndStatusNot(
	                        paymentId,
	                        RecordStatus.DELETED
	                );
	
	
	
	        if(exists) {
	
	
	            throw new BusinessException(
	                    "Receipt already generated for this payment"
	            );
	
	        }
	
	
	
	        // 3. Payment Status Validation
	
	        if(payment.getPaymentStatus() == PaymentStatus.PENDING
	                ||
	           payment.getPaymentStatus() == PaymentStatus.CANCELLED){
	
	
	            throw new BusinessException(
	                    "Receipt cannot be generated for pending or cancelled payment"
	            );
	
	        }
	
	
	        // 4. DTO -> Entity
	
	        StudentFeeReceipt receipt =
	                mapper.toEntity(dto);
	
	
	        // 5. Set Payment
	
	        receipt.setPayment(
	                payment
	        );
	
	
	       // 6. Populate Receipt Snapshot
	
	       populateSnapshotData(
		            receipt,
		            payment
	       );
	
	
	        // 7. Generate Receipt Number
	
	        receipt.setReceiptNumber(
	                generateReceiptNumber()
	        );
	
	
	
	        if (payment.getScholarship() != null) {

	            receipt.setScholarshipName(
	                    payment.getScholarship().getScholarshipName()
	            );

	            receipt.setScholarshipType(
	                    payment.getScholarship().getScholarshipType()
	            );

	            receipt.setScholarshipAmount(
	                    payment.getScholarship().getApprovedAmount()
	            );
	        }
	
	
	
	        // 8. Default Date
	
	        if(receipt.getReceiptDate()
	                == null) {
	
	
	            receipt.setReceiptDate(
	                    LocalDate.now()
	            );
	
	        }
	
	
	        // 9. Status
	
	
	        receipt.setStatus(
	                RecordStatus.ACTIVE
	        );
	
		     // Creation Audit Information
	
		     receipt.setCreatedByUserId(
		             SecurityUtils.getCurrentUserId()
		     );
	
		     receipt.setCreatedBy(
		             SecurityUtils.getCurrentFullName()
		     );
	
	        // 10. Validate Snapshot
	
	        validateReceiptSnapshot(receipt);
	
	
	        // 11. Save
	
	
	        StudentFeeReceipt saved =
	                receiptRepository.save(
	                        receipt
	                );
	
	
	        // 12. Response
	
	        return mapper.toDto(saved);
	
	    }
    
    
		 // =====================================================
		 // GET RECEIPT BY ID
		 // =====================================================
			
	     @AuditLog(
	            module = "STUDENT_FEE_RECEIPT",
	            action = AuditAction.VIEW,
	            description = "Get fee receipt by id"
	    		)
		 @Override
		 @Transactional(readOnly = true)
		 public StudentFeeReceiptResponseDto getReceiptById(
		         Long id) {
		
		
		
		     StudentFeeReceipt receipt =
		             receiptRepository
		             .findByIdAndStatusNot(
		                     id,
		                     RecordStatus.DELETED
		             )
		             .orElseThrow(() ->
		                     new ResourceNotFoundException(
		                             "Fee receipt not found with id : "
		                             + id
		                     )
		             );
		
		
		
		     return mapper.toDto(receipt);
		
		 }
		
		
		
		
		
	  // =====================================================
	  // DELETE RECEIPT (SOFT DELETE)
	  // =====================================================

	  @AuditLog(
	          module = "STUDENT_FEE_RECEIPT",
	          action = AuditAction.DELETE,
	          description = "Soft delete fee receipt"
	  )
	  @Override
	  @Transactional
	  public void deleteReceipt(Long id) {

	      // Validate Receipt

	      StudentFeeReceipt receipt =
	              receiptRepository
	              .findByIdAndStatusNot(
	                      id,
	                      RecordStatus.DELETED
	              )
	              .orElseThrow(() ->
	                      new ResourceNotFoundException(
	                              "Fee receipt not found with id : " + id
	                      )
	              );


	      // Validate Payment Status

	      if(receipt.getPayment() != null
	              &&
	         receipt.getPayment()
	                .getPaymentStatus()
	                == PaymentStatus.PAID) {

	          throw new BusinessException(
	                  "Paid payment receipt cannot be deleted"
	          );
	      }


	      // Validate Refund Process Status

	      boolean refundExists =
	              studentFeeRefundRepository
	              .existsByPaymentIdAndRefundStatusInAndStatusNot(
	                      receipt.getPayment().getId(),
	                      List.of(
	                              RefundStatus.REQUESTED,
	                              RefundStatus.APPROVED,
	                              RefundStatus.COMPLETED
	                      ),
	                      RecordStatus.DELETED
	              );


	      if(refundExists) {

	          throw new BusinessException(
	                  "Receipt cannot be deleted because refund process already started."
	          );
	      }


	      // Set Delete Audit Information

	      receipt.setDeletedByUserId(
	              SecurityUtils.getCurrentUserId()
	      );

	      receipt.setDeletedBy(
	              SecurityUtils.getCurrentFullName()
	      );

	      receipt.setDeletedDate(
	              LocalDateTime.now()
	      );


	      // Perform Soft Delete

	      receipt.setStatus(
	              RecordStatus.DELETED
	      );


	      receiptRepository.save(receipt);

	  }
		
		
		
		
		
		
		 // =====================================================
		 // GET ALL RECEIPTS PAGINATION
		 // =====================================================
		
		
		 @Override
		 @Transactional(readOnly = true)
		 public PageResponse<StudentFeeReceiptResponseDto> getAllReceipts(
		         int page,
		         int size,
		         String sortBy,
		         String direction) {
		
		
		
		     Sort sort =
		             direction.equalsIgnoreCase("asc")
		             ?
		             Sort.by(sortBy).ascending()
		             :
		             Sort.by(sortBy).descending();
		
		
		
		
		     Pageable pageable =
		             PageRequest.of(
		                     page,
		                     size,
		                     sort
		             );
		
		
		
		
		     Page<StudentFeeReceipt> result =
		             receiptRepository
		             .findByStatusNot(
		                     RecordStatus.DELETED,
		                     pageable
		             );
		
		
		
		
		     List<StudentFeeReceiptResponseDto> content =
		             result.getContent()
		             .stream()
		             .map(mapper::toDto)
		             .toList();
		
		
		
		
		     PageResponse<StudentFeeReceiptResponseDto> response =
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
		 // SEARCH RECEIPTS
		 // =====================================================
		
		
		 @Override
		 @Transactional(readOnly = true)
		 public PageResponse<StudentFeeReceiptResponseDto> searchReceipts(
		         StudentFeeReceiptSearchRequest request,
		         int page,
		         int size,
		         String sortBy,
		         String direction) {
		
		
		
		     Sort sort =
		             direction.equalsIgnoreCase("asc")
		             ?
		             Sort.by(sortBy).ascending()
		             :
		             Sort.by(sortBy).descending();
		
		
		
		
		     Pageable pageable =
		             PageRequest.of(
		                     page,
		                     size,
		                     sort
		             );
		
		
		
		
		
		
		     Specification<StudentFeeReceipt> specification =
		             StudentFeeReceiptSpecification
		             .getSpecification(
		
		            		 request.getReceiptNumber(),

		                     request.getPaymentId(),

		                     request.getStudentId(),
		                     request.getStudentName(),
		                     request.getRollNumber(),

		                     request.getAdmissionId(),
		                     request.getAdmissionNumber(),
		                     request.getAcademicYear(),

		                     request.getCourseName(),
		                     request.getDepartmentName(),
		                     request.getSemesterName(),

		                     request.getScholarshipId(),

		                     request.getPaymentStatus(),
		                     request.getPaymentMode(),

		                     request.getMinPaidAmount(),
		                     request.getMaxPaidAmount(),

		                     request.getMinTotalFee(),
		                     request.getMaxTotalFee(),

		                     request.getMinDueAmount(),
		                     request.getMaxDueAmount(),

		                     request.getMinFinalPayableAmount(),
		                     request.getMaxFinalPayableAmount(),

		                     request.getReceiptDateFrom(),
		                     request.getReceiptDateTo(),

		                     request.getStatus()
		
		             );
		
		
		
		
		
		
		     Page<StudentFeeReceipt> result =
		             receiptRepository
		             .findAll(
		                     specification,
		                     pageable
		             );
		
		
		
		
		
		
		     List<StudentFeeReceiptResponseDto> content =
		             result.getContent()
		             .stream()
		             .map(mapper::toDto)
		             .toList();
		
		
		
		
		
		
		     PageResponse<StudentFeeReceiptResponseDto> response =
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
		// Generate Unique Receipt Number
		// =====================================================
		private String generateReceiptNumber() {

		    try {

		        StudentFeeReceipt lastReceipt =
		                receiptRepository
		                        .findTopByOrderByIdDesc()
		                        .orElse(null);


		        long nextNumber = 1;


		        if (lastReceipt != null && lastReceipt.getId() != null) {

		            nextNumber = lastReceipt.getId() + 1;
		        }


		        return String.format(
		                "SMS-REC-%s-%06d",
		                LocalDate.now()
		                        .format(DateTimeFormatter.BASIC_ISO_DATE),
		                nextNumber
		        );


		    } catch (Exception e) {

		        throw new BusinessException(
		                "Unable to generate fee receipt number"
		        );
		    }
		}
		 
		 
		 
		 
		// =====================================================
		// Populate Receipt Snapshot
		// =====================================================


		private void populateSnapshotData(
		        StudentFeeReceipt receipt,
		        StudentFeePayment payment
		){

		    
		    // ============================
		    // Student Snapshot
		    // ============================

		    if(payment.getStudent() != null){

		        receipt.setStudentId(
		                payment.getStudent().getId()
		        );


		        String firstName =
		                payment.getStudent().getFirstName();


		        String lastName =
		                payment.getStudent().getLastName();



		        receipt.setStudentName(
		                ((firstName != null ? firstName : "")
		                + " "
		                + (lastName != null ? lastName : ""))
		                .trim()
		        );


		        receipt.setRollNumber(
		                payment.getStudent().getRollNumber()
		        );
		    }



		    // ============================
		    // Admission Snapshot
		    // ============================

		    if(payment.getAdmission() != null){

		        receipt.setAdmissionId(
		                payment.getAdmission().getId()
		        );


		        receipt.setAdmissionNumber(
		                payment.getAdmission()
		                .getAdmissionNumber()
		        );



		        if(payment.getAdmission().getCourse() != null){

		            receipt.setCourseId(
		                    payment.getAdmission()
		                    .getCourse()
		                    .getId()
		            );


		            receipt.setCourseName(
		                    payment.getAdmission()
		                    .getCourse()
		                    .getCourseName()
		            );
		        }


		        if(payment.getAdmission().getDepartment() != null){

		            receipt.setDepartmentId(
		                    payment.getAdmission()
		                    .getDepartment()
		                    .getId()
		            );


		            receipt.setDepartmentName(
		                    payment.getAdmission()
		                    .getDepartment()
		                    .getDepartmentName()
		            );
		        }


		        if(payment.getAdmission().getSemester() != null){

		            receipt.setSemesterId(
		                    payment.getAdmission()
		                    .getSemester()
		                    .getId()
		            );


		            receipt.setSemesterName(
		                    payment.getAdmission()
		                    .getSemester()
		                    .getSemesterName()
		            );
		        }

		    }



		    // ============================
		    // Academic Year
		    // ============================

		    receipt.setAcademicYear(
		            payment.getAcademicYear()
		    );



		    // ============================
		    // Fee Snapshot
		    // ============================


		    receipt.setTotalFee(
		            payment.getTotalFee()
		    );


		    receipt.setPaidAmount(
		            payment.getPaidAmount()
		    );


		    receipt.setDueAmount(
		            payment.getDueAmount()
		    );


	
		 // ============================
		 // Scholarship Snapshot
		 // ============================


		 if(payment.getScholarship() != null){


		     StudentScholarship scholarship =
		             payment.getScholarship();



		     receipt.setScholarshipId(
		             scholarship.getId()
		     );



		     receipt.setScholarshipName(
		             scholarship.getScholarshipName()
		     );



		     receipt.setScholarshipType(
		             scholarship.getScholarshipType()
		     );



		     receipt.setScholarshipAmount(
		             scholarship.getApprovedAmount()
		     );



		 }
		 else{


		     receipt.setScholarshipId(null);


		     receipt.setScholarshipName(null);


		     receipt.setScholarshipType(null);


		     receipt.setScholarshipAmount(
		             BigDecimal.ZERO
		     );

		 }



			 // ============================
			 // Final Payable Amount
			 // ============================


		    receipt.setFinalPayableAmount(
		            payment.getFinalPayableAmount()
		    );


		    // ============================
		    // Payment Snapshot
		    // ============================


		    receipt.setPaymentStatus(
		            payment.getPaymentStatus()
		    );


		    receipt.setPaymentMode(
		            payment.getPaymentMode()
		    );


		    receipt.setPaymentDate(
		            payment.getPaymentDate()
		    );


		    receipt.setTransactionReference(
		            payment.getTransactionReference()
		    );

		}
		
		
		
		
		
		// =====================================================
		// Validate Receipt Snapshot
		// =====================================================


		private void validateReceiptSnapshot(
		        StudentFeeReceipt receipt
		){

			if(receipt.getFinalPayableAmount()==null){

			    throw new BusinessException(
			            "Final payable amount missing for receipt"
			    );
			}
			

		    if(receipt.getStudentId()==null){

		        throw new BusinessException(
		                "Student information missing for receipt"
		        );

		    }

		    if(receipt.getReceiptNumber().isBlank()){
		    	
		        throw new BusinessException("Receipt number missing");
		    }

		    if(receipt.getAdmissionId()==null){

		        throw new BusinessException(
		                "Admission information missing for receipt"
		        );

		    }



		    if(receipt.getAcademicYear()==null
		            || receipt.getAcademicYear().isBlank()){


		        throw new BusinessException(
		                "Academic year missing for receipt"
		        );

		    }



		    if(receipt.getTotalFee()==null){

		        throw new BusinessException(
		                "Total fee missing for receipt"
		        );

		    }



		    if(receipt.getPaidAmount()==null){

		        throw new BusinessException(
		                "Paid amount missing for receipt"
		        );

		    }



		    if(receipt.getDueAmount()==null){

		        throw new BusinessException(
		                "Due amount missing for receipt"
		        );

		    }



		    if(receipt.getPaymentStatus()==null){

		        throw new BusinessException(
		                "Payment status missing for receipt"
		        );

		    }
		    
		    if(receipt.getReceiptNumber()==null){
		        throw new BusinessException(
		            "Receipt number missing."
		        );
		    }
		    
		    if(receipt.getReceiptDate()==null){
		        throw new BusinessException(
		            "Receipt date missing."
		        );
		    }
		    
		    if (receipt.getPayment() == null) {

		        throw new BusinessException(
		                "Payment information missing for receipt."
		        );

		    }

		}
		
		

		
		
		// =====================================================
		// PRINT FEE RECEIPT
		// =====================================================

		@Override
		@Transactional
		public byte[] printReceipt(Long id) {

		    // Validate Receipt

		    StudentFeeReceipt receipt =
		            receiptRepository
		            .findByIdAndStatusNot(
		                    id,
		                    RecordStatus.DELETED
		            )
		            .orElseThrow(() ->
		                    new ResourceNotFoundException(
		                            "Fee receipt not found"
		                    )
		            );


		    // Update Print Audit Information

		    receipt.setPrintedByUserId(
		            SecurityUtils.getCurrentUserId()
		    );

		    receipt.setPrintedBy(
		            SecurityUtils.getCurrentFullName()
		    );

		    receipt.setPrintedDate(
		            LocalDateTime.now()
		    );


		    receiptRepository.save(receipt);


		    // Generate Receipt PDF

		    return receiptPdfService
		            .generateReceiptPdf(receipt);
		}

    
}