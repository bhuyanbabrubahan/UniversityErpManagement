package com.sms.erp.transport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.transport.dto.*;
import com.sms.erp.transport.service.TransportService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','TRANSPORT_MANAGER')")
@RequestMapping("/api/erp/transport")
@RequiredArgsConstructor
public class TransportController {
    private final TransportService service;
    @GetMapping public Page<TransportResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<TransportResponseDto> search(@RequestBody(required=false) TransportSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<TransportResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<TransportResponseDto> create(@RequestBody TransportRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<TransportResponseDto> update(@PathVariable Long id,@RequestBody TransportRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
