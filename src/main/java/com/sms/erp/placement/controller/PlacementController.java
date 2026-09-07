package com.sms.erp.placement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.placement.dto.*;
import com.sms.erp.placement.service.PlacementService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','PLACEMENT_OFFICER')")
@RequestMapping("/api/erp/placement")
@RequiredArgsConstructor
public class PlacementController {
    private final PlacementService service;
    @GetMapping public Page<PlacementResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<PlacementResponseDto> search(@RequestBody(required=false) PlacementSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<PlacementResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<PlacementResponseDto> create(@RequestBody PlacementRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<PlacementResponseDto> update(@PathVariable Long id,@RequestBody PlacementRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
