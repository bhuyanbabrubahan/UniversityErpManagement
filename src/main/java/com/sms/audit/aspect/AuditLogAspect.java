package com.sms.audit.aspect;


import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.ZoneId;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.dto.AuditLogResponseDto;
import com.sms.audit.service.AuditLogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAspect {


    private final AuditLogService auditLogService;



    @Around("@annotation(com.sms.audit.annotation.AuditLog)")
    public Object audit(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {


        MethodSignature signature =
                (MethodSignature) joinPoint.getSignature();



        AuditLog auditAnnotation =
                signature
                .getMethod()
                .getAnnotation(AuditLog.class);



        Object response;



        try {


            // Execute actual method

            response =
                    joinPoint.proceed();



            saveAuditLog(
                    auditAnnotation,
                    "SUCCESS"
            );



            return response;



        }
        catch(Exception exception){


            saveAuditLog(
                    auditAnnotation,
                    "FAILED : "
                    + exception.getMessage()
            );


            throw exception;

        }


    }





    private void saveAuditLog(
            AuditLog auditAnnotation,
            String description
    ){


        AuditLogResponseDto audit =
                AuditLogResponseDto.builder()

                .action(
                    auditAnnotation.action()
                )

                .moduleName(
                    auditAnnotation.module()
                )

                .description(
                        auditAnnotation.description()
                        + " - "
                        + description
                )

                .performedBy(
                    getCurrentUser()
                )

                .role(
                    getCurrentRole()
                )

                .ipAddress(
                    getClientIp()
                )

                .requestMethod(
                    getHttpRequest()
                    .getMethod()
                )

                .requestUrl(
                    getHttpRequest()
                    .getRequestURI()
                )

                .timestamp(
                	    LocalDateTime.now(
                	        ZoneId.of("Asia/Kolkata")
                	    )
                	)

                .build();



        try {

            auditLogService.createAuditLog(audit);

        } catch (Exception ex) {

            log.error(
                    "Failed to save audit log for module: {}",
                    auditAnnotation.module(),
                    ex
            );

        }

    }





    private String getCurrentUser(){


        Authentication authentication =
                SecurityContextHolder
                .getContext()
                .getAuthentication();



        if(authentication == null){

            return "SYSTEM";

        }


        return authentication.getName();

    }





    private String getCurrentRole(){


        Authentication authentication =
                SecurityContextHolder
                .getContext()
                .getAuthentication();



        if(authentication == null){

            return "SYSTEM";

        }


        return authentication
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

    }





    private HttpServletRequest getHttpRequest(){


        ServletRequestAttributes attributes =
                (ServletRequestAttributes)
                RequestContextHolder
                .getRequestAttributes();


        return attributes.getRequest();

    }





    private String getClientIp(){

        HttpServletRequest request = getHttpRequest();

        String ip = request.getHeader("X-FORWARDED-FOR");


        if(ip == null || ip.isBlank()) {

            ip = request.getRemoteAddr();

        }


        if("0:0:0:0:0:0:0:1".equals(ip)) {

            ip = "127.0.0.1";

        }


        return ip;
    }


}