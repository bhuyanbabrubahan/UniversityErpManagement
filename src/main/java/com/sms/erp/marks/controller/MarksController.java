package com.sms.erp.marks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.marks.dto.*;
import com.sms.erp.marks.service.MarksService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','EXAM_CONTROLLER','FACULTY','HOD')")
@RequestMapping("/api/erp/marks")
@RequiredArgsConstructor
public class MarksController {
    private final MarksService service;
    @GetMapping public Page<MarksResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<MarksResponseDto> search(@RequestBody(required=false) MarksSearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<MarksResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<MarksResponseDto> create(@RequestBody MarksRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<MarksResponseDto> update(@PathVariable Long id,@RequestBody MarksRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
