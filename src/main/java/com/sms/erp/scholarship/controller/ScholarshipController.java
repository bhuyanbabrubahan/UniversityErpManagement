package com.sms.erp.scholarship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.scholarship.dto.*;
import com.sms.erp.scholarship.service.ScholarshipService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','ACCOUNTANT','REGISTRAR')")
@RequestMapping("/api/erp/scholarship")
@RequiredArgsConstructor
public class ScholarshipController {
    private final ScholarshipService service;
    @GetMapping public Page<ScholarshipResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<ScholarshipResponseDto> search(@RequestBody(required=false) ScholarshipSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<ScholarshipResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<ScholarshipResponseDto> create(@RequestBody ScholarshipRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<ScholarshipResponseDto> update(@PathVariable Long id,@RequestBody ScholarshipRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
