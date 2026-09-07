package com.sms.controller;


import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.dto.studentdocument.StudentDocumentResponseDto;
import com.sms.dto.studentdocument.StudentDocumentSearchRequestDto;
import com.sms.enums.DocumentType;
import com.sms.payload.ApiResponseDto;
import com.sms.service.StudentDocumentService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/student-documents")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Student Document API",
        description = "APIs for uploading and managing student documents"
)
public class StudentDocumentController {



    private final StudentDocumentService studentDocumentService;



    // =====================================
    // UPLOAD DOCUMENT
    // =====================================


    @Operation(
            summary = "Upload Student Document",
            description =
            "Uploads student documents like marksheet, photo, certificate etc."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Document uploaded successfully"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid file or duplicate document"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Student not found"
            )

    })
    @AuditLog(
            action = AuditAction.CREATE,
            module = "STUDENT_DOCUMENT",
            description = "Upload student document"
    )
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponseDto<StudentDocumentResponseDto>> uploadDocument(


            @Parameter(
                    description = "Student Id",
                    required = true
            )
            @RequestParam
            Long studentId,



            @Parameter(
                    description = "Document Type",
                    required = true
            )
            @RequestParam
            DocumentType documentType,



            @Parameter(
                    description = "Upload File",
                    required = true,
                    content =
                    @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE
                    )
            )
            @RequestParam
            MultipartFile file


    ) {



        log.info(
                "Upload document request received for student {}",
                studentId
        );



        StudentDocumentResponseDto response =

                studentDocumentService
                .uploadDocument(
                        studentId,
                        documentType,
                        file
                );



        return ResponseBuilder.success(

                response,

                "Document uploaded successfully",

                HttpStatus.OK

        );

    }

    
    
    
    @GetMapping("/download/{id}")
    @Operation(
            summary = "Download Student Document",
            description = "Downloads uploaded student document"
    )
    public ResponseEntity<Resource> downloadDocument(

            @PathVariable Long id

    ) {


        return studentDocumentService
                .downloadDocument(id);

    }
    
    
    
	 // =====================================
	 // GET DOCUMENT BY ID
	 // =====================================
	
	
	 @GetMapping("/{id}")
	 @Operation(
	         summary = "Get Document By Id",
	         description = "Fetches student document details by document id."
	 )
	 @ApiResponses({
	
	         @ApiResponse(
	                 responseCode = "200",
	                 description = "Document fetched successfully"
	         ),
	
	         @ApiResponse(
	                 responseCode = "404",
	                 description = "Document not found"
	         )
	
	 })
	 public ResponseEntity<ApiResponseDto<StudentDocumentResponseDto>> getDocumentById(
	
	         @PathVariable Long id
	
	 ) {
	
	
	     log.info(
	             "Get document request received : {}",
	             id
	     );
	
	
	     StudentDocumentResponseDto response =
	
	             studentDocumentService
	             .getDocumentById(id);
	
	
	
	     return ResponseBuilder.success(
	
	             response,
	
	             "Document fetched successfully",
	
	             HttpStatus.OK
	
	     );
	
	 }
	 
	 
	 	@AuditLog(
		        action = AuditAction.UPDATE,
		        module = "STUDENT_DOCUMENT",
		        description = "Update student document"
		)
	 	@PutMapping(
		        value = "/{id}",
		        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
		)
		@Operation(
		        summary = "Update Student Document",
		        description = "Replaces an existing uploaded document with a new file."
		)
		@ApiResponses({

		        @ApiResponse(
		                responseCode = "200",
		                description = "Document updated successfully"
		        ),

		        @ApiResponse(
		                responseCode = "404",
		                description = "Document not found"
		        )

		})
		public ResponseEntity<ApiResponseDto<StudentDocumentResponseDto>> updateDocument(

				@PathVariable Long id,

				@RequestPart MultipartFile file

		) {

			StudentDocumentResponseDto response =

					studentDocumentService.updateDocument(

							id,

							file

					);

			return ResponseBuilder.success(

					response,

					"Document updated successfully.",

					HttpStatus.OK

			);

		}
	 
	 
	    
	// =====================================
	// DELETE DOCUMENT
	// =====================================

	 @AuditLog(
	 	       action = AuditAction.DELETE,
	 	       module = "STUDENT_DOCUMENT",
	 	       description = "Delete student document"
	 )
	@DeleteMapping("/{id}")
	@Operation(
	        summary = "Delete Student Document",
	        description = "Soft deletes student document."
	)
	@ApiResponses({

	        @ApiResponse(
	                responseCode = "200",
	                description = "Document deleted successfully"
	        ),

	        @ApiResponse(
	                responseCode = "404",
	                description = "Document not found"
	        )

	})
	public ResponseEntity<ApiResponseDto<Void>> deleteDocument(

	        @PathVariable Long id

	) {


	    log.info(
	            "Delete document request received : {}",
	            id
	    );



	    studentDocumentService
	            .deleteDocument(id);



	    return ResponseBuilder.success(

	            null,

	            "Document deleted successfully",

	            HttpStatus.OK

	    );

	}

	
	
	
	
	 // =====================================================
    // GET ALL DOCUMENTS
    // PAGINATION + SORTING
    // =====================================================


    @GetMapping
    @Operation(
            summary = "Get All Student Documents",
            description =
            "Fetch all student documents with pagination and sorting."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Documents fetched successfully"
            )

    })
    public ResponseEntity<ApiResponseDto<PageResponse<StudentDocumentResponseDto>>> getAllDocuments(


            @Parameter(
                    description = "Page number starting from 0",
                    example = "0"
            )
            @RequestParam(
                    defaultValue = "0"
            )
            int page,



            @Parameter(
                    description = "Number of records per page",
                    example = "10"
            )
            @RequestParam(
                    defaultValue = "10"
            )
            int size,



            @Parameter(
                    description = "Field name for sorting",
                    example = "uploadedAt"
            )
            @RequestParam(
                    defaultValue = "uploadedAt"
            )
            String sortBy,



            @Parameter(
                    description = "Sorting direction ASC/DESC",
                    example = "DESC"
            )
            @RequestParam(
                    defaultValue = "DESC"
            )
            String direction


    ) {



        log.info(
                "Fetching documents page={}, size={}",
                page,
                size
        );



        Sort sort =


                direction.equalsIgnoreCase("ASC")

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



        PageResponse<StudentDocumentResponseDto> response =

                studentDocumentService
                .getAllDocuments(
                        pageable
                );



        return ResponseBuilder.success(

                response,

                "Documents fetched successfully",

                HttpStatus.OK

        );

    }





    // =====================================================
    // SEARCH DOCUMENTS
    // DYNAMIC SPECIFICATION
    // =====================================================



    @PostMapping("/search")
    @Operation(
            summary = "Search Student Documents",
            description =
            "Dynamic search of documents using filters with pagination and sorting."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search request"
            )

    })
    public ResponseEntity<ApiResponseDto<PageResponse<StudentDocumentResponseDto>>> searchDocuments(


            @Valid
            @RequestBody
            StudentDocumentSearchRequestDto request,



            @RequestParam(
                    defaultValue = "0"
            )
            int page,



            @RequestParam(
                    defaultValue = "10"
            )
            int size,



            @RequestParam(
                    defaultValue = "uploadedAt"
            )
            String sortBy,



            @RequestParam(
                    defaultValue = "DESC"
            )
            String direction


    ) {



        log.info(
                "Searching student documents"
        );



        Sort sort =


                direction.equalsIgnoreCase("ASC")

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



        PageResponse<StudentDocumentResponseDto> response =


                studentDocumentService
                .searchDocuments(

                        request,

                        pageable

                );



        return ResponseBuilder.success(

                response,

                "Documents search completed successfully",

                HttpStatus.OK

        );

    }
}