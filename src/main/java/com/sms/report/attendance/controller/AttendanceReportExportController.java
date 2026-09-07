package com.sms.report.attendance.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import com.sms.report.attendance.service.AttendanceReportExportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance-reports")
public class AttendanceReportExportController {
    private final AttendanceReportExportService service;

    @GetMapping("/session/{sessionId}/export")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','FACULTY')")
    public ResponseEntity<byte[]> exportSession(@PathVariable Long sessionId, @RequestParam(defaultValue="pdf") String format){
        byte[] body=service.exportSession(sessionId, format);
        String extension="pdf".equalsIgnoreCase(format)?"pdf":"xlsx";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(service.contentType(format)));
        headers.setContentDisposition(ContentDisposition.attachment().filename("attendance-session-"+sessionId+"."+extension).build());
        return ResponseEntity.ok().headers(headers).body(body);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam(defaultValue="xlsx") String format){
        byte[] body=service.export(format);
        String extension="pdf".equalsIgnoreCase(format)?"pdf":"xlsx";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(service.contentType(format)));
        headers.setContentDisposition(ContentDisposition.attachment().filename("attendance-report."+extension).build());
        return ResponseEntity.ok().headers(headers).body(body);
    }
}
