package com.sms.serviceImpl;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sms.dto.PageResponse;
import com.sms.dto.studentdocument.StudentDocumentResponseDto;
import com.sms.dto.studentdocument.StudentDocumentSearchRequestDto;
import com.sms.entity.Student;
import com.sms.entity.StudentDocument;
import com.sms.enums.DocumentStatus;
import com.sms.enums.DocumentType;
import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.StudentDocumentMapper;
import com.sms.repository.StudentDocumentRepository;
import com.sms.repository.StudentRepository;
import com.sms.security.util.SecurityUtils;
import com.sms.service.FileStorageService;
import com.sms.service.StudentDocumentService;
import com.sms.specification.StudentDocumentSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentDocumentServiceImpl 
        implements StudentDocumentService {

    private final StudentRepository studentRepository;
    private final StudentDocumentRepository studentDocumentRepository;
    private final FileStorageService fileStorageService;
    private final StudentDocumentMapper studentDocumentMapper;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

 // =====================================
 // UPLOAD DOCUMENT
 // =====================================

 @Override
 public StudentDocumentResponseDto uploadDocument(

         Long studentId,

         DocumentType documentType,

         MultipartFile file

 ) {

     log.info(
             "Document upload request for student : {}",
             studentId
     );

     // =====================================
     // STEP 1 : Find Student
     // =====================================

     Student student = studentRepository
             .findByIdAndStatus(
                     studentId,
                     RecordStatus.ACTIVE
             )
             .orElseThrow(() ->
                     new ResourceNotFoundException(
                             "Active student not found with id : " + studentId
                     )
             );

     // =====================================
     // STEP 2 : Validate Document Type
     // =====================================

     if (documentType == null) {
         throw new BusinessException(
                 "Document type is required"
         );
     }

     // =====================================
     // STEP 3 : Duplicate Check
     // =====================================

     boolean exists = studentDocumentRepository
             .existsByStudentIdAndDocumentTypeAndStatus(
                     studentId,
                     documentType,
                     RecordStatus.ACTIVE
             );

     if (exists) {
         throw new BusinessException(
                 "Document already uploaded"
         );
     }

     // =====================================
     // STEP 4 : Validate File
     // =====================================

     validateFile(file);

     // =====================================
     // STEP 5 : Store File
     // =====================================

     String storedFileName = fileStorageService.storeFile(file);

     // =====================================
     // STEP 6 : Save Document Metadata
     // =====================================

     StudentDocument document = StudentDocument.builder()

             .student(student)
             
             .documentType(documentType)

             .originalFileName(file.getOriginalFilename())

             .storedFileName(storedFileName)

             .contentType(file.getContentType())

             .fileSize(file.getSize())

             .filePath(
            	        uploadDir + "/" + storedFileName
            	)

             .status(RecordStatus.ACTIVE)
             .documentStatus(DocumentStatus.PENDING)
             .uploadedAt(LocalDateTime.now())

             .build();
     
     // Audit
     document.setCreatedByUserId(SecurityUtils.getCurrentUserId());
     document.setCreatedBy(SecurityUtils.getCurrentFullName());
     document.setCreatedAt(LocalDateTime.now());
     
     StudentDocument saved = studentDocumentRepository.save(document);

     log.info(
             "Document uploaded successfully : {}",
             saved.getId()
     );

     return studentDocumentMapper.convertToDto(saved);
 }




    // =====================================
    // FILE VALIDATION
    // =====================================


    private void validateFile(
            MultipartFile file
    ) {



        if(file == null ||
                file.isEmpty()) {


            throw new BusinessException(
                "File cannot be empty"
            );

        }



        String contentType =
                file.getContentType();



        if(contentType == null ||

          !(contentType.equals("image/jpeg")
          ||
          contentType.equals("image/png")
          ||
          contentType.equals("application/pdf"))) {



            throw new BusinessException(
                "Only JPG, PNG and PDF files are allowed"
            );

        }


        String fileName =
                file.getOriginalFilename();

        if(fileName == null ||
                fileName.isBlank()) {

            throw new BusinessException(
                    "Invalid file name."
            );

        }

        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            throw new BusinessException(
                    "File extension is required."
            );
        }

        String extension = fileName
                .substring(lastDotIndex + 1)
                .toLowerCase();

        if (!extension.equals("jpg")
                && !extension.equals("jpeg")
                && !extension.equals("png")
                && !extension.equals("pdf")) {

            throw new BusinessException(
                    "Invalid file extension."
            );
        }

        long maxSize =
                5 * 1024 * 1024;



        if(file.getSize() > maxSize) {


            throw new BusinessException(
                "File size must be less than 5 MB"
            );

        }

    }









	 // =====================================
	 // GET DOCUMENT BY ID
	 // =====================================
	
	 @Override
	 @Transactional(readOnly = true)
	 public StudentDocumentResponseDto getDocumentById(Long id) {
	
	
	     log.info(
	             "Fetching document by id : {}",
	             id
	     );
	
	
	
	     StudentDocument document =
	
	    		 studentDocumentRepository
	    		 .findByIdAndStatus(

	    		         id,

	    		         RecordStatus.ACTIVE

	    		 )
	
	             .orElseThrow(() ->
	
	                     new ResourceNotFoundException(
	                             "Document not found with id : " + id
	                     )
	
	             );
	
	
	
	     return studentDocumentMapper
	             .convertToDto(document);
	
	 }	




	//=====================================
	//DELETE DOCUMENT
	//=====================================
	
	@Override
	@Transactional
	public void deleteDocument(Long id) {
	
	
	  log.info(
	          "Deleting document : {}",
	          id
	  );
	
	
	
	  StudentDocument document =
	
			  studentDocumentRepository
			  .findByIdAndStatus(

			          id,

			          RecordStatus.ACTIVE

			  )
	
	          .orElseThrow(() ->
	
	                  new ResourceNotFoundException(
	                          "Document not found with id : " + id
	                  )
	
	          );
	
	  fileStorageService.deleteFile(

		        document.getStoredFileName()

		);
	
	  document.setStatus(
	          RecordStatus.DELETED
	  );
	
		document.setCreatedByUserId(SecurityUtils.getCurrentUserId());

		document.setCreatedBy(SecurityUtils.getCurrentFullName());

	
	  studentDocumentRepository.save(document);
	
	
	
	  log.info(
	          "Document deleted successfully : {}",
	          id
	  );
	
	
	}
	




	// =====================================
	// DOWNLOAD DOCUMENT
	// =====================================


	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<Resource> downloadDocument(
	        Long id
	) {


	    log.info(
	            "Download document request : {}",
	            id
	    );



	    // =====================================
	    // STEP 1 : Find Document
	    // =====================================


	    StudentDocument document =

	            studentDocumentRepository
	            .findByIdAndStatus(

	                    id,

	                    RecordStatus.ACTIVE

	            )

	            .orElseThrow(() ->

	                    new ResourceNotFoundException(
	                            "Document not found"
	                    )

	            );



	    // =====================================
	    // STEP 2 : Load File
	    // =====================================


	    Resource resource =

	            fileStorageService
	            .loadFile(
	                    document.getStoredFileName()
	            );



	    // =====================================
	    // STEP 3 : Detect Content Type
	    // =====================================


	    String contentType =

	            document.getContentType();



	    if(contentType == null) {

	        contentType =
	                MediaType.APPLICATION_OCTET_STREAM_VALUE;

	    }



	    // =====================================
	    // STEP 4 : Return File Response
	    // =====================================


	    return ResponseEntity.ok()

	            .contentType(
	                    MediaType.parseMediaType(
	                            contentType
	                    )
	            )

	            .header(

	            		HttpHeaders.CONTENT_DISPOSITION,

	            		"attachment; filename=\""

	            		+ document.getOriginalFileName()

	            		+ "\""

	            		)

	            .body(resource);

	}





	// =====================================
	// GET ALL DOCUMENTS
	// PAGINATION + SORTING
	// =====================================

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentDocumentResponseDto> getAllDocuments(
	        Pageable pageable
	) {


	    log.info(
	            "Fetching all student documents with pagination"
	    );


	    // =====================================================
	    // 1. Fetch Documents
	    // =====================================================

	    Page<StudentDocument> documents =

	            studentDocumentRepository
	            .findAll(

	                    StudentDocumentSpecification.search(

	                            new StudentDocumentSearchRequestDto()

	                    ),

	                    pageable

	            );



	    // =====================================================
	    // 2. Entity -> DTO Mapping
	    // =====================================================

	    List<StudentDocumentResponseDto> content =

	            documents
	            .getContent()
	            .stream()
	            .map(studentDocumentMapper::convertToDto)
	            .toList();



	    // =====================================================
	    // 3. Create Page Response
	    // =====================================================

	    PageResponse<StudentDocumentResponseDto> response =

	            new PageResponse<>();


	    response.setContent(content);


	    response.setPageNumber(
	            documents.getNumber()
	    );


	    response.setPageSize(
	            documents.getSize()
	    );


	    response.setTotalElements(
	            documents.getTotalElements()
	    );


	    response.setTotalPages(
	            documents.getTotalPages()
	    );


	    response.setLast(
	            documents.isLast()
	    );


	    return response;

	}





	// =====================================
	// SEARCH DOCUMENTS
	// DYNAMIC SPECIFICATION
	// =====================================

	@Override
	@Transactional(readOnly = true)
	public PageResponse<StudentDocumentResponseDto> searchDocuments(

	        StudentDocumentSearchRequestDto request,

	        Pageable pageable

	) {


	    log.info(

	            "Searching documents with filters : {}",

	            request

	    );


	    // =====================================================
	    // 1. Fetch Filtered Documents
	    // =====================================================

	    Page<StudentDocument> documents =


	            studentDocumentRepository
	            .findAll(

	                    StudentDocumentSpecification
	                    .search(request),

	                    pageable

	            );



	    // =====================================================
	    // 2. Entity -> DTO Mapping
	    // =====================================================

	    List<StudentDocumentResponseDto> content =


	            documents
	            .getContent()
	            .stream()
	            .map(studentDocumentMapper::convertToDto)
	            .toList();



	    // =====================================================
	    // 3. Create Page Response
	    // =====================================================

	    PageResponse<StudentDocumentResponseDto> response =


	            new PageResponse<>();



	    response.setContent(content);



	    response.setPageNumber(

	            documents.getNumber()

	    );



	    response.setPageSize(

	            documents.getSize()

	    );



	    response.setTotalElements(

	            documents.getTotalElements()

	    );



	    response.setTotalPages(

	            documents.getTotalPages()

	    );



	    response.setLast(

	            documents.isLast()

	    );



	    return response;

	}





	// =====================================
	// UPDATE DOCUMENT
	// =====================================

	@Override
	@Transactional
	public StudentDocumentResponseDto updateDocument(

	        Long id,

	        MultipartFile file

	) {

	    log.info(
	            "Updating document : {}",
	            id
	    );


	    // =====================================
	    // STEP 1 : Find Active Document
	    // =====================================

	    StudentDocument document =

	            studentDocumentRepository
	            .findByIdAndStatus(

	                    id,

	                    RecordStatus.ACTIVE

	            )

	            .orElseThrow(() ->

	                    new ResourceNotFoundException(

	                            "Document not found with id : " + id

	                    )

	            );


	    // =====================================
	    // STEP 2 : Validate New File
	    // =====================================

	    validateFile(file);


	    // =====================================
	    // STEP 3 : Delete Old Physical File
	    // =====================================

	    fileStorageService.deleteFile(

	            document.getStoredFileName()

	    );


	    // =====================================
	    // STEP 4 : Store New Physical File
	    // =====================================

	    String storedFileName =

	            fileStorageService.storeFile(file);


	    // =====================================
	    // STEP 5 : Update File Metadata
	    // =====================================

	    document.setOriginalFileName(

	            file.getOriginalFilename()

	    );

	    document.setStoredFileName(

	            storedFileName

	    );

	    document.setContentType(

	            file.getContentType()

	    );

	    document.setFileSize(

	            file.getSize()

	    );

	    document.setFilePath(

	            uploadDir + "/" + storedFileName

	    );


	    // =====================================
	    // STEP 6 : Reset Document Verification
	    // =====================================

	    /*
	     * New file uploaded.
	     * Therefore old verification is no longer valid.
	     */

	    document.setDocumentStatus(

	            DocumentStatus.PENDING

	    );


	    // =====================================
	    // STEP 7 : Reset Review Audit
	    // =====================================

	    /*
	     * Previous admin review belongs to old file.
	     * So clear review information.
	     */

	    document.setReviewedByUserId(null);

	    document.setReviewedBy(null);

	    document.setReviewedAt(null);


	    // =====================================
	    // STEP 8 : Keep Record Status ACTIVE
	    // =====================================

	    document.setStatus(

	            RecordStatus.ACTIVE

	    );


	    // =====================================
	    // STEP 9 : Update Audit
	    // =====================================

	    document.setUpdatedByUserId(

	            SecurityUtils.getCurrentUserId()

	    );

	    document.setUpdatedBy(

	            SecurityUtils.getCurrentFullName()

	    );

	    document.setUpdatedAt(

	            LocalDateTime.now()

	    );


	    // =====================================
	    // STEP 10 : Save Updated Document
	    // =====================================

	    StudentDocument updated =

	            studentDocumentRepository.save(

	                    document

	            );


	    log.info(

	            "Document updated successfully : {}",

	            updated.getId()

	    );


	    // =====================================
	    // STEP 11 : Return Response DTO
	    // =====================================

	    return studentDocumentMapper

	            .convertToDto(updated);
	}
	
	
	// =====================================
	// REVIEW DOCUMENT
	// APPROVE / REJECT
	// =====================================

	@Override
	@Transactional
	public StudentDocumentResponseDto reviewDocument(

	        Long id,

	        DocumentStatus documentStatus

	) {

	    log.info(
	            "Reviewing document : {}, status : {}",
	            id,
	            documentStatus
	    );


	    // =====================================
	    // STEP 1 : Validate Review Status
	    // =====================================

	    if (documentStatus == null) {

	        throw new BusinessException(
	                "Document status is required"
	        );

	    }

	    if (documentStatus != DocumentStatus.VERIFIED
	            && documentStatus != DocumentStatus.REJECTED) {

	        throw new BusinessException(
	                "Only VERIFIED or REJECTED status is allowed"
	        );

	    }


	    // =====================================
	    // STEP 2 : Find Active Document
	    // =====================================

	    StudentDocument document =

	            studentDocumentRepository
	            .findByIdAndStatus(

	                    id,

	                    RecordStatus.ACTIVE

	            )

	            .orElseThrow(() ->

	                    new ResourceNotFoundException(

	                            "Active document not found with id : "
	                            + id

	                    )

	            );


	    // =====================================
	    // STEP 3 : Update Document Status
	    // =====================================

	    document.setDocumentStatus(

	            documentStatus

	    );


	    // =====================================
	    // STEP 4 : Review Audit
	    // =====================================

	    document.setReviewedByUserId(

	            SecurityUtils.getCurrentUserId()

	    );

	    document.setReviewedBy(

	            SecurityUtils.getCurrentFullName()

	    );

	    document.setReviewedAt(

	            LocalDateTime.now()

	    );


	    // =====================================
	    // STEP 5 : Update General Audit
	    // =====================================

	    document.setUpdatedByUserId(

	            SecurityUtils.getCurrentUserId()

	    );

	    document.setUpdatedBy(

	            SecurityUtils.getCurrentFullName()

	    );

	    document.setUpdatedAt(

	            LocalDateTime.now()

	    );


	    // =====================================
	    // STEP 6 : Save
	    // =====================================

	    StudentDocument updated =

	            studentDocumentRepository.save(

	                    document

	            );


	    log.info(

	            "Document reviewed successfully : {}, status : {}",

	            updated.getId(),

	            updated.getDocumentStatus()

	    );


	    // =====================================
	    // STEP 7 : Return DTO
	    // =====================================

	    return studentDocumentMapper

	            .convertToDto(updated);
	}



}