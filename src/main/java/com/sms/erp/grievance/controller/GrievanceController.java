package com.sms.erp.grievance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.grievance.dto.*;
import com.sms.erp.grievance.service.GrievanceService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','REGISTRAR','DEAN','HOD','FACULTY','STAFF','STUDENT','PARENT')")
@RequestMapping("/api/erp/grievance")
@RequiredArgsConstructor
public class GrievanceController {
    private final GrievanceService service;
    @GetMapping public Page<GrievanceResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<GrievanceResponseDto> search(@RequestBody(required=false) GrievanceSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<GrievanceResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<GrievanceResponseDto> create(@RequestBody GrievanceRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<GrievanceResponseDto> update(@PathVariable Long id,@RequestBody GrievanceRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
