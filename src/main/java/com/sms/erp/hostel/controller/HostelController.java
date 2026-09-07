package com.sms.erp.hostel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.hostel.dto.*;
import com.sms.erp.hostel.service.HostelService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','HOSTEL_WARDEN')")
@RequestMapping("/api/erp/hostel")
@RequiredArgsConstructor
public class HostelController {
    private final HostelService service;
    @GetMapping public Page<HostelResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<HostelResponseDto> search(@RequestBody(required=false) HostelSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<HostelResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<HostelResponseDto> create(@RequestBody HostelRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<HostelResponseDto> update(@PathVariable Long id,@RequestBody HostelRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
