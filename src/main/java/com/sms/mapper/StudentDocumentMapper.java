package com.sms.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.studentdocument.StudentDocumentResponseDto;
import com.sms.dto.studentdocument.StudentDocumentUploadRequestDto;
import com.sms.entity.StudentDocument;

@Mapper(
        componentModel = "spring"
)
public interface StudentDocumentMapper {

    // =====================================================
    // Upload Request DTO -> Entity
    // =====================================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)

    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "originalFileName", ignore = true)
    @Mapping(target = "storedFileName", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)

    // Managed by service
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "documentStatus", ignore = true)

    // Review audit managed by service
    @Mapping(target = "reviewedByUserId", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)

    StudentDocument toEntity(
            StudentDocumentUploadRequestDto dto
    );


    // =====================================================
    // Entity -> Response DTO
    // =====================================================

    @Mapping(
            source = "student.id",
            target = "studentId"
    )

    @Mapping(
            source = "student.fullName",
            target = "studentName"
    )

    StudentDocumentResponseDto convertToDto(
            StudentDocument document
    );


    // =====================================================
    // List Entity -> List DTO
    // =====================================================

    List<StudentDocumentResponseDto> convertToDtoList(
            List<StudentDocument> documents
    );


    // =====================================================
    // Update Existing Document
    // =====================================================

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)

    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "originalFileName", ignore = true)
    @Mapping(target = "storedFileName", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)

    // Managed by service
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "documentStatus", ignore = true)

    // Review audit managed by service
    @Mapping(target = "reviewedByUserId", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)

    // Base audit
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    void updateEntity(
            @MappingTarget StudentDocument document,
            StudentDocumentUploadRequestDto dto
    );

}