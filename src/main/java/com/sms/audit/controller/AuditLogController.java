package com.sms.audit.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.sms.audit.dto.AuditLogResponseDto;
import com.sms.audit.dto.AuditLogSearchRequestDto;
import com.sms.audit.service.AuditLogService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;



@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Audit Log Management",
        description = "APIs for tracking system activities"
)
public class AuditLogController {


    private final AuditLogService auditLogService;



    // =====================================
    // GET BY ID
    // =====================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get audit log by id"
    )
    public ResponseEntity<ApiResponseDto<AuditLogResponseDto>> getById(

            @Parameter(
                    description = "Audit Log ID"
            )
            @PathVariable Long id

    ){


        log.info(
                "Fetching audit log with id : {}",
                id
        );


        AuditLogResponseDto response =
                auditLogService.getAuditLogById(id);



        return ResponseBuilder.success(
                response,
                "Audit log fetched successfully",
                HttpStatus.OK
        );

    }





    // =====================================
    // GET ALL
    // =====================================


    @GetMapping
    @Operation(
            summary = "Get all audit logs"
    )
    public ResponseEntity<ApiResponseDto<Page<AuditLogResponseDto>>> getAll(

            Pageable pageable

    ){


        log.info(
                "Fetching all audit logs"
        );


        Page<AuditLogResponseDto> response =
                auditLogService.getAllAuditLogs(pageable);



        return ResponseBuilder.success(
                response,
                "Audit logs fetched successfully",
                HttpStatus.OK
        );

    }





    // =====================================
    // SEARCH
    // =====================================


    @PostMapping("/search")
    @Operation(
            summary = "Search audit logs dynamically"
    )
    public ResponseEntity<ApiResponseDto<Page<AuditLogResponseDto>>> search(


            @RequestBody AuditLogSearchRequestDto request,

            Pageable pageable

    ){


        log.info(
                "Searching audit logs"
        );



        Page<AuditLogResponseDto> response =
                auditLogService.searchAuditLogs(
                        request,
                        pageable
                );



        return ResponseBuilder.success(
                response,
                "Audit logs search completed",
                HttpStatus.OK
        );

    }





    // =====================================
    // COUNT
    // =====================================


    @GetMapping("/count")
    @Operation(
            summary = "Count active audit logs"
    )
    public ResponseEntity<ApiResponseDto<Long>> count(){


        log.info(
                "Counting audit logs"
        );


        long count =
                auditLogService.countAuditLogs();



        return ResponseBuilder.success(
                count,
                "Audit log count fetched",
                HttpStatus.OK
        );

    }



}