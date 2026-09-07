package com.sms.fee.refund.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.PageResponse;
import com.sms.fee.refund.dto.RefundActionRequestDto;
import com.sms.fee.refund.dto.RefundHistoryResponseDto;
import com.sms.fee.refund.dto.StudentFeeRefundRequestDto;
import com.sms.fee.refund.dto.StudentFeeRefundResponseDto;
import com.sms.fee.refund.dto.StudentFeeRefundSearchRequest;
import com.sms.fee.refund.service.StudentFeeRefundService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/fee-refunds")
@RequiredArgsConstructor
@Tag(
        name = "Student Fee Refund Management",
        description = "APIs for creating, fetching, searching and deleting student fee refunds"
)
public class StudentFeeRefundController {


    private final StudentFeeRefundService refundService;



    // =====================================================
    // Create Refund
    // =====================================================

    @PostMapping
    @Operation(
            summary = "Create fee refund",
            description =
            "Create refund request against a student fee payment"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeRefundResponseDto>> createRefund(

            @Valid
            @RequestBody
            StudentFeeRefundRequestDto request

    ){

        return ResponseBuilder.success(

                refundService.createRefund(request),

                "Refund created successfully",

                HttpStatus.CREATED
        );
    }




    // =====================================================
    // Get Refund By Id
    // =====================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get refund by id",
            description =
            "Fetch student fee refund details using refund id"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeRefundResponseDto>> getById(

            @Parameter(
                    description = "Refund ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id

    ){

        return ResponseBuilder.success(

                refundService.getRefundById(id),

                "Refund fetched successfully",

                HttpStatus.OK
        );
    }




    // =====================================================
    // Get All Refunds
    // =====================================================

    @GetMapping
    @Operation(
            summary = "Get all refunds",
            description =
            "Fetch all student fee refunds with pagination and sorting"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeRefundResponseDto>>> getAll(

            @Parameter(
                    description = "Page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,


            @Parameter(
                    description = "Page size",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,


            @Parameter(
                    description = "Field used for sorting",
                    example = "id"
            )
            @RequestParam(defaultValue = "id")
            String sortBy,


            @Parameter(
                    description = "Sorting direction",
                    example = "desc"
            )
            @RequestParam(defaultValue = "desc")
            String direction

    ){

        return ResponseBuilder.success(

                refundService.getAllRefunds(
                        page,
                        size,
                        sortBy,
                        direction
                ),

                "Refund list fetched successfully",

                HttpStatus.OK
        );
    }





    // =====================================================
    // Search Refund
    // =====================================================

    @PostMapping("/search")
    @Operation(
            summary = "Search refunds",
            description =
            "Search student fee refunds dynamically with filters"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeRefundResponseDto>>> search(

            @Valid
            @RequestBody
            StudentFeeRefundSearchRequest request,


            @Parameter(
                    description = "Page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,


            @Parameter(
                    description = "Page size",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,


            @Parameter(
                    description = "Sorting field",
                    example = "id"
            )
            @RequestParam(defaultValue = "id")
            String sortBy,


            @Parameter(
                    description = "Sorting direction",
                    example = "desc"
            )
            @RequestParam(defaultValue = "desc")
            String direction

    ){

        return ResponseBuilder.success(

                refundService.searchRefunds(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                ),

                "Refund search completed successfully",

                HttpStatus.OK
        );
    }



    
    
	 // =====================================================
	 // Approve Refund
	 // =====================================================
	
	 @PutMapping("/{id}/approve")
	 @Operation(
	         summary = "Approve refund",
	         description = "Approve a requested student fee refund"
	 )
	 public ResponseEntity<ApiResponseDto<StudentFeeRefundResponseDto>>
	 approveRefund(
	
	         @Parameter(
	                 description = "Refund ID",
	                 example = "1",
	                 required = true
	         )
	         @PathVariable Long id
	 ){
	
	     return ResponseBuilder.success(
	
	             refundService.approveRefund(id),
	
	             "Refund approved successfully",
	
	             HttpStatus.OK
	     );
	
	 }


    // =====================================================
    // Delete Refund
    // =====================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete refund",
            description =
            "Delete student fee refund using refund id"
    )
    public ResponseEntity<ApiResponseDto<Void>> delete(

            @Parameter(
                    description = "Refund ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id

    ){

        refundService.deleteRefund(id);


        return ResponseBuilder.success(

                null,

                "Refund deleted successfully",

                HttpStatus.OK
        );
    }

    
    
    
    
	    
		 // =====================================================
		 // Reject Refund
		 // =====================================================
		
		 @PutMapping("/{id}/reject")
		 @Operation(
		         summary = "Reject refund",
		         description = "Reject requested refund with reason."
		 )
		 public ResponseEntity<ApiResponseDto<StudentFeeRefundResponseDto>>
		 rejectRefund(
		
		         @PathVariable Long id,
		
		         @Valid
		         @RequestBody
		         RefundActionRequestDto request
		
		 ){
		
		     return ResponseBuilder.success(
		
		             refundService.rejectRefund(
		                     id,
		                     request
		             ),
		
		             "Refund rejected successfully",
		
		             HttpStatus.OK
		     );
		
		 }
    
	 
	 
	 
	 
		// =====================================================
		// Cancel Refund
		// =====================================================

		@PutMapping("/{id}/cancel")
		@Operation(
		        summary = "Cancel refund",
		        description = "Cancel requested refund with reason."
		)
		public ResponseEntity<ApiResponseDto<StudentFeeRefundResponseDto>>
		cancelRefund(

		        @PathVariable Long id,

		        @Valid
		        @RequestBody
		        RefundActionRequestDto request

		){

		    return ResponseBuilder.success(

		            refundService.cancelRefund(
		                    id,
		                    request
		            ),

		            "Refund cancelled successfully",

		            HttpStatus.OK
		    );

		}
    
	    
	    
	 // =====================================================
	 // Complete Refund
	 // =====================================================
	
	 @PutMapping("/{id}/complete")
	 @Operation(
	         summary = "Complete refund",
	         description = "Complete approved refund and update fee payment."
	 )
	 public ResponseEntity<ApiResponseDto<StudentFeeRefundResponseDto>>
	 completeRefund(
	
	         @PathVariable Long id
	
	 ){
	
	     return ResponseBuilder.success(
	
	             refundService.completeRefund(id),
	
	             "Refund completed successfully",
	
	             HttpStatus.OK
	     );
	
	 }
	 
	 
	 
	 
	 
	 
	 
	// =====================================================
	// Refund History By Payment
	// =====================================================

	@GetMapping("/history/{paymentId}")
	@Operation(
	        summary = "Get refund history by payment id",
	        description =
	        "Fetch all refund history records against a fee payment"
	)
	public ResponseEntity<ApiResponseDto<PageResponse<RefundHistoryResponseDto>>> 
	getRefundHistory(


	        @Parameter(
	                description = "Payment ID",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long paymentId,


	        @Parameter(
	                description = "Page number",
	                example = "0"
	        )
	        @RequestParam(defaultValue = "0")
	        int page,


	        @Parameter(
	                description = "Page size",
	                example = "10"
	        )
	        @RequestParam(defaultValue = "10")
	        int size,


	        @Parameter(
	                description = "Sorting field",
	                example = "id"
	        )
	        @RequestParam(defaultValue = "id")
	        String sortBy,


	        @Parameter(
	                description = "Sorting direction",
	                example = "desc"
	        )
	        @RequestParam(defaultValue = "desc")
	        String direction

	){


	    return ResponseBuilder.success(

	            refundService.getRefundHistory(
	                    paymentId,
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            ),

	            "Refund history fetched successfully",

	            HttpStatus.OK
	    );

	}
	 
	 
    
    
}