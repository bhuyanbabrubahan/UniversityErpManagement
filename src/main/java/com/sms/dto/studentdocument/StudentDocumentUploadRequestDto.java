package com.sms.dto.studentdocument;


import com.sms.enums.DocumentType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
        name = "StudentDocumentUploadRequest",
        description = "Request DTO for uploading student documents"
)
public class StudentDocumentUploadRequestDto {


    @Schema(
            description = "Student document type",
            example = "TEN_MARKSHEET"
    )
    private DocumentType documentType;

}