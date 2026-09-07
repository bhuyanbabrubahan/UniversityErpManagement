package com.sms.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.sms.dto.PageResponse;
import com.sms.dto.studentdocument.StudentDocumentResponseDto;
import com.sms.dto.studentdocument.StudentDocumentSearchRequestDto;
import com.sms.enums.DocumentStatus;
import com.sms.enums.DocumentType;

public interface StudentDocumentService {

	StudentDocumentResponseDto uploadDocument(Long studentId, DocumentType documentType, MultipartFile file);

	StudentDocumentResponseDto updateDocument(Long id, MultipartFile file);

	StudentDocumentResponseDto getDocumentById(Long id);

	ResponseEntity<Resource> downloadDocument(Long id);

	void deleteDocument(Long id);

	PageResponse<StudentDocumentResponseDto> getAllDocuments(Pageable pageable);

	PageResponse<StudentDocumentResponseDto> searchDocuments(StudentDocumentSearchRequestDto request, Pageable pageable);

	StudentDocumentResponseDto reviewDocument(Long id, DocumentStatus documentStatus);

}