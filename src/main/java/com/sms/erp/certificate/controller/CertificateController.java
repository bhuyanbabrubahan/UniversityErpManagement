package com.sms.erp.certificate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.certificate.dto.*;
import com.sms.erp.certificate.service.CertificateService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','REGISTRAR')")
@RequestMapping("/api/erp/certificate")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService service;
    @GetMapping public Page<CertificateResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<CertificateResponseDto> search(@RequestBody(required=false) CertificateSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<CertificateResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<CertificateResponseDto> create(@RequestBody CertificateRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<CertificateResponseDto> update(@PathVariable Long id,@RequestBody CertificateRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
