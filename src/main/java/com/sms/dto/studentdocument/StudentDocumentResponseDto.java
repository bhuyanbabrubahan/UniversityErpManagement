package com.sms.dto.studentdocument;

import java.time.LocalDateTime;

import com.sms.enums.DocumentStatus;
import com.sms.enums.DocumentType;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "StudentDocumentResponse",
    description = "Response DTO for student uploaded document"
)
public class StudentDocumentResponseDto {

    // =====================================================
    // Document Information
    // =====================================================

    @Schema(description = "Unique document id", example = "1")
    private Long id;

    @Schema(
        description = "Student id who uploaded the document",
        example = "101"
    )
    private Long studentId;

    @Schema(
        description = "Student name",
        example = "Rahul Kumar"
    )
    private String studentName;

    @Schema(
        description = "Type of uploaded document",
        example = "PROFILE_PHOTO"
    )
    private DocumentType documentType;

    @Schema(
        description = "Original file name uploaded by student",
        example = "aadhar_card.pdf"
    )
    private String originalFileName;

    @Schema(
        description = "Generated unique stored file name",
        example = "8c7f1c8a-2d3b-4f1e-a123.pdf"
    )
    private String storedFileName;

    @Schema(
        description = "Uploaded file content type",
        example = "application/pdf"
    )
    private String contentType;

    @Schema(
        description = "Size of uploaded file in bytes",
        example = "204800"
    )
    private Long fileSize;

    @Schema(
        description = "Physical file path",
        example = "/uploads/student-documents/8c7f1c8a.pdf"
    )
    private String filePath;

    @Schema(
        description = "Document upload timestamp",
        example = "2026-07-19T10:30:00"
    )
    private LocalDateTime uploadedAt;

    @Schema(
        description = "Document status",
        example = "PENDING"
    )
    private DocumentStatus documentStatus;
    
    @Schema(
            description = "Record status",
            example = "ACTIVE"
        )
        private RecordStatus status;

    // =====================================================
    // Base Information
    // =====================================================

    @Schema(
        description = "Record creation timestamp",
        example = "2026-07-24T10:30:00"
    )
    private LocalDateTime createdAt;

    @Schema(
        description = "Record last update timestamp",
        example = "2026-07-24T11:30:00"
    )
    private LocalDateTime updatedAt;

    // =====================================================
    // Creation Information
    // =====================================================

    @Schema(
        description = "Created By User ID",
        example = "101"
    )
    private Long createdByUserId;

    @Schema(
        description = "Created By",
        example = "Admin"
    )
    private String createdBy;

    // =====================================================
    // Update Information
    // =====================================================

    @Schema(
        description = "Updated By User ID",
        example = "102"
    )
    private Long updatedByUserId;

    @Schema(
        description = "Updated By",
        example = "Admin"
    )
    private String updatedBy;
    
	 // =====================================================
	 // Document Review Information
	 // =====================================================
	
    @Schema(
            description = "Admin user id who reviewed the document",
            example = "1"
    )
    private Long reviewedByUserId;

    @Schema(
            description = "Admin who reviewed the document",
            example = "Admin User"
    )
    private String reviewedBy;

    @Schema(
            description = "Document review timestamp",
            example = "2026-08-17T22:30:00"
    )
    private LocalDateTime reviewedAt;

}