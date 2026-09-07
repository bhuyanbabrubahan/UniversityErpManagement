package com.sms.erp.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.staff.dto.*;
import com.sms.erp.staff.service.StaffService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','HR','STAFF')")
@RequestMapping("/api/erp/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService service;
    @GetMapping public Page<StaffResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<StaffResponseDto> search(@RequestBody(required=false) StaffSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<StaffResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<StaffResponseDto> create(@RequestBody StaffRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<StaffResponseDto> update(@PathVariable Long id,@RequestBody StaffRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
