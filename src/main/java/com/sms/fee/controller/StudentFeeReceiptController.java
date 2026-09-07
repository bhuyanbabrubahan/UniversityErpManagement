package com.sms.fee.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.StudentFeeReceiptRequestDto;
import com.sms.fee.dto.StudentFeeReceiptResponseDto;
import com.sms.fee.dto.StudentFeeReceiptSearchRequest;
import com.sms.fee.service.StudentFeeReceiptService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-receipts")
@Validated
@Tag(
        name = "Student Fee Receipt",
        description = "APIs for generating, fetching, searching and managing student fee receipts"
)
public class StudentFeeReceiptController {


    private final StudentFeeReceiptService service;


    // =====================================================
    // Generate Receipt
    // =====================================================
    
    @PostMapping(
    		"/payments/{paymentId}/receipt"
    		)
    @Operation(
            summary = "Generate Fee Receipt",
            description =
            "Generate receipt from successful student fee payment"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeReceiptResponseDto>> generateReceipt(

            @Parameter(
                    description = "Student Fee Payment ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long paymentId,


            @Valid
            @RequestBody StudentFeeReceiptRequestDto dto
    ){

        return ResponseBuilder.success(

                service.generateReceipt(
                        paymentId,
                        dto
                ),

                "Receipt generated successfully",

                HttpStatus.CREATED
        );
    }

    
    
    
    
    
    // =====================================================
    // Get By Id
    // =====================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Receipt By Id",
            description =
            "Fetch receipt details using receipt id"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeReceiptResponseDto>> getReceiptById(

            @PathVariable Long id
    ){

        return ResponseBuilder.success(

                service.getReceiptById(id),

                "Receipt fetched successfully",

                HttpStatus.OK
        );
    }
    
    
    
 
    
    
    
	//=====================================================
	//Get All Receipts
	//=====================================================
	
	@GetMapping
	@Operation(
	      summary = "Get All Fee Receipts",
	      description =
	      "Fetch paginated fee receipts with sorting support"
	)
	public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeReceiptResponseDto>>> getAllReceipts(
	
	
	      @Parameter(
	              description = "Page number starting from 0",
	              example = "0"
	      )
	      @RequestParam(
	              defaultValue = "0"
	      )
	      int page,
	
	
	      @Parameter(
	              description = "Records per page",
	              example = "10"
	      )
	      @RequestParam(
	              defaultValue = "10"
	      )
	      int size,
	
	
	      @Parameter(
	              description = "Sort field",
	              example = "id"
	      )
	      @RequestParam(
	              defaultValue = "id"
	      )
	      String sortBy,
	
	
	      @Parameter(
	              description = "Sorting direction",
	              example = "desc"
	      )
	      @RequestParam(
	              defaultValue = "desc"
	      )
	      String direction
	
	){
	
	
	  PageResponse<StudentFeeReceiptResponseDto> response =
	          service.getAllReceipts(
	                  page,
	                  size,
	                  sortBy,
	                  direction
	          );
	
	
	  return ResponseBuilder.success(
	
	          response,
	
	          "Receipt list fetched successfully",
	
	          HttpStatus.OK
	  );
	}

    
    
    
	// =====================================================
	// Search Receipts
	// =====================================================

	@PostMapping("/search")
	@Operation(
	        summary = "Search Fee Receipts",
	        description =
	        "Search receipts using receipt number, student, admission, payment status, payment mode and date filters"
	)
	public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeReceiptResponseDto>>> searchReceipts(


	        @Valid
	        @RequestBody
	        StudentFeeReceiptSearchRequest request,


	        @Parameter(
	                description = "Page number starting from 0",
	                example = "0"
	        )
	        @RequestParam(
	                defaultValue = "0"
	        )
	        int page,


	        @Parameter(
	                description = "Records per page",
	                example = "10"
	        )
	        @RequestParam(
	                defaultValue = "10"
	        )
	        int size,


	        @Parameter(
	                description = "Sort field",
	                example = "id"
	        )
	        @RequestParam(
	                defaultValue = "id"
	        )
	        String sortBy,


	        @Parameter(
	                description = "Sorting direction",
	                example = "desc"
	        )
	        @RequestParam(
	                defaultValue = "desc"
	        )
	        String direction

	){


	    PageResponse<StudentFeeReceiptResponseDto> response =
	            service.searchReceipts(
	                    request,
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            );


	    return ResponseBuilder.success(

	            response,

	            "Receipt search completed successfully",

	            HttpStatus.OK
	    );
	}
    
    
    

	// =====================================================
	// Delete Receipt
	// =====================================================
	
	@DeleteMapping("/{id}")
	@Operation(
	        summary = "Delete Fee Receipt",
	        description =
	        "Soft delete receipt using receipt id"
	)
	public ResponseEntity<ApiResponseDto<Void>> deleteReceipt(


	        @Parameter(
	                description = "Student Fee Receipt Id",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id

	){


	    service.deleteReceipt(id);


	    return ResponseBuilder.success(

	            null,

	            "Receipt deleted successfully",

	            HttpStatus.OK
	    );
	}
    
	
	
	// =====================================================
	// Print / Download Receipt PDF
	// =====================================================

	@GetMapping("/{id}/download")
	@Operation(
	        summary = "Print Fee Receipt",
	        description =
	        "Generate PDF receipt for student fee payment"
	)
	public ResponseEntity<byte[]> downloadReceipt(

	        @Parameter(
	                description = "Student Fee Receipt Id",
	                example = "1",
	                required = true
	        )
	        @PathVariable Long id

	){


	    byte[] pdf =
	            service.printReceipt(id);



	    return ResponseEntity
	            .ok()
	            .header(
	                    HttpHeaders.CONTENT_DISPOSITION,
	                    "inline; filename=fee-receipt-" + id + ".pdf"
	            )
	            .contentType(
	                    MediaType.APPLICATION_PDF
	            )
	            .body(pdf);

	}
	
    
    
    
}