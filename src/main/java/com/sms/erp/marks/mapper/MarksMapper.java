package com.sms.erp.marks.mapper;

import org.mapstruct.*;
import com.sms.erp.marks.entity.Marks;
import com.sms.erp.marks.dto.MarksRequestDto;
import com.sms.erp.marks.dto.MarksResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MarksMapper {
    Marks toEntity(MarksRequestDto request);
    MarksResponseDto toResponse(Marks entity);
    void updateEntity(MarksRequestDto request, @MappingTarget Marks entity);
}
