package com.sms.fee.report.export.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sms.fee.report.export.service.FeeExportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-reports/export")
public class FeeExportController {
    private final FeeExportService exportService;

    @GetMapping
    public ResponseEntity<byte[]> export(@RequestParam String report, @RequestParam(defaultValue = "xlsx") String format) {
        byte[] body = exportService.export(report, format);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(exportService.contentType(format)));
        headers.setContentDisposition(ContentDisposition.attachment().filename(exportService.fileName(report, format)).build());
        return ResponseEntity.ok().headers(headers).body(body);
    }
}
