package com.sms.scholarship.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.sms.scholarship.dto.StudentScholarshipRequestDto;
import com.sms.scholarship.dto.StudentScholarshipResponseDto;
import com.sms.scholarship.entity.StudentScholarship;


@Mapper(
        componentModel = "spring"
)
public interface StudentScholarshipMapper {


    // =====================================================
    // DTO TO ENTITY
    // =====================================================


    @Mapping(
            target = "id",
            ignore = true
    )

    @Mapping(
            target = "student",
            ignore = true
    )

    @Mapping(
            target = "admission",
            ignore = true
    )


    // Calculated by Service
    @Mapping(
            target = "requestedAmount",
            ignore = true
    )


    @Mapping(
            target = "approvedAmount",
            ignore = true
    )
    

    @Mapping(
            target = "rejectionReason",
            ignore = true
    )


    @Mapping(
            target = "status",
            ignore = true
    )


    // Workflow handled by Service
    @Mapping(
            target = "scholarshipStatus",
            ignore = true
    )

    

    @Mapping(
    		target = "approvedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "approvedBy", 
    		ignore = true
    )
    
    @Mapping(
            target = "approvalDate",
            ignore = true
    )

    @Mapping(
    		target = "rejectedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "rejectedBy", 
    		ignore = true
    )
    
    @Mapping(
    		target = "rejectedDate", 
    		ignore = true
    )
    
    @Mapping(
    		target = "deletedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "deletedBy", 
    		ignore = true
    )
    
    @Mapping(
    		target = "deletedDate", 
    		ignore = true
   	)

    
    StudentScholarship toEntity(
            StudentScholarshipRequestDto dto
    );



    // =====================================================
    // ENTITY TO DTO
    // =====================================================


    @Mapping(
            source = "student.id",
            target = "studentId"
    )


    @Mapping(
            target = "studentName",
            expression =
            "java(entity.getStudent().getFirstName() + \" \" + entity.getStudent().getLastName())"
    )


    @Mapping(
            source = "admission.id",
            target = "admissionId"
    )


    @Mapping(
            source = "admission.admissionNumber",
            target = "admissionNumber"
    )


    StudentScholarshipResponseDto toDto(
            StudentScholarship entity
    );







    // =====================================================
    // UPDATE ENTITY
    // =====================================================


    @Mapping(
            target = "id",
            ignore = true
    )


    @Mapping(
            target = "createdAt",
            ignore = true
    )


    @Mapping(
            target = "updatedAt",
            ignore = true
    )


    @Mapping(
            target = "student",
            ignore = true
    )


    @Mapping(
            target = "admission",
            ignore = true
    )


    // Calculated again in Service
    @Mapping(
            target = "requestedAmount",
            ignore = true
    )


    @Mapping(
            target = "approvedAmount",
            ignore = true
    )



    @Mapping(
            target = "rejectionReason",
            ignore = true
    )


    @Mapping(
            target = "status",
            ignore = true
    )


    // Only approve/reject API changes workflow
    @Mapping(
            target = "scholarshipStatus",
            ignore = true
    )

    @Mapping(
    		target = "createdByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "createdBy", 
    		ignore = true
    )

    @Mapping(
    		target = "updatedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "updatedBy", 
    		ignore = true
    )
    
    @Mapping(
    		target = "approvedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "approvedBy", 
    		ignore = true
    )
    
    @Mapping(
            target = "approvalDate",
            ignore = true
    )

    @Mapping(
    		target = "rejectedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "rejectedBy", 
    		ignore = true
    )
    
    @Mapping(
    		target = "rejectedDate", 
    		ignore = true
    )
    
    @Mapping(
    		target = "deletedByUserId", 
    		ignore = true
    )
    
    @Mapping(
    		target = "deletedBy", 
    		ignore = true
    )
    
    @Mapping(
    		target = "deletedDate", 
    		ignore = true
   	)
    
    void updateEntity(
            StudentScholarshipRequestDto dto,
            @MappingTarget StudentScholarship entity
    );

}