package com.sms.erp.report;

import java.util.List;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sms.erp.report.dto.ReportDefinitionDto;
import com.sms.erp.report.service.ErpReportService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/api/erp/reports") @RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','VC','REGISTRAR','PRINCIPAL','DEAN','HOD','ACCOUNTANT','EXAM_CONTROLLER','LIBRARIAN','HOSTEL_WARDEN','TRANSPORT_MANAGER','PLACEMENT_OFFICER','HR','FACULTY')")
public class ErpReportController {
 private final ErpReportService service;
 @GetMapping public List<ReportDefinitionDto> catalogue(){return service.catalogue();}
 @GetMapping("/{code}/data") public ResponseEntity<List<String[]>> data(@PathVariable String code){if(!service.exists(code))return ResponseEntity.notFound().build(); return ResponseEntity.ok(service.data(code));}
 @GetMapping("/{code}/pdf") public ResponseEntity<byte[]> pdf(@PathVariable String code)throws Exception{if(!service.exists(code))return ResponseEntity.notFound().build(); return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+code+".pdf").body(service.pdf(code));}
 @GetMapping("/{code}/excel") public ResponseEntity<byte[]> excel(@PathVariable String code)throws Exception{if(!service.exists(code))return ResponseEntity.notFound().build(); return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+code+".xlsx").body(service.excel(code));}
}
