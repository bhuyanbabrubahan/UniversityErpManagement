package com.sms.dto.studentdocument;

import java.time.LocalDateTime;

import com.sms.enums.DocumentType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "StudentDocumentSearchRequest",
        description = "Request DTO used for dynamic searching of student documents with pagination and filtering."
)
public class StudentDocumentSearchRequestDto {

    @Schema(
            description = "Document ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Student ID",
            example = "101"
    )
    private Long studentId;

    @Schema(
            description = "Document type",
            example = "PROFILE_PHOTO"
    )
    private DocumentType documentType;

    @Schema(
            description = "Original uploaded file name (partial search supported)",
            example = "passport"
    )
    private String originalFileName;

    @Schema(
            description = "Stored file name (partial search supported)",
            example = "9d4e8f4b-2f43-4ef6-bbe1"
    )
    private String storedFileName;

    @Schema(
            description = "File content type",
            example = "image/jpeg"
    )
    private String contentType;

    @Min(value = 0, message = "Minimum file size cannot be negative")
    @Schema(
            description = "Minimum file size in bytes",
            example = "1024"
    )
    private Long minFileSize;

    @Min(value = 0, message = "Maximum file size cannot be negative")
    @Schema(
            description = "Maximum file size in bytes",
            example = "5242880"
    )
    private Long maxFileSize;

    @Schema(
            description = "Uploaded from date & time",
            example = "2026-07-01T00:00:00"
    )
    private LocalDateTime uploadedFrom;

    @Schema(
            description = "Uploaded to date & time",
            example = "2026-07-31T23:59:59"
    )
    private LocalDateTime uploadedTo;
    
    


}