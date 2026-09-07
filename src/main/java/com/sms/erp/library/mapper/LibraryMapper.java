package com.sms.erp.library.mapper;

import org.mapstruct.*;
import com.sms.erp.library.entity.Library;
import com.sms.erp.library.dto.LibraryRequestDto;
import com.sms.erp.library.dto.LibraryResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibraryMapper {
    Library toEntity(LibraryRequestDto request);
    LibraryResponseDto toResponse(Library entity);
    void updateEntity(LibraryRequestDto request, @MappingTarget Library entity);
}
