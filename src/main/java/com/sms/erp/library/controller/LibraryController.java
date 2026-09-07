package com.sms.erp.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.library.dto.*;
import com.sms.erp.library.service.LibraryService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','LIBRARIAN')")
@RequestMapping("/api/erp/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService service;
    @GetMapping public Page<LibraryResponseDto> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.findAll(page,size); }
    @PostMapping("/search") public Page<LibraryResponseDto> search(@RequestBody(required=false) LibrarySearchDto search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.search(search,page,size); }
    @GetMapping("/{id}") public ResponseEntity<LibraryResponseDto> get(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }
    @PostMapping public ResponseEntity<LibraryResponseDto> create(@RequestBody LibraryRequestDto request) { return ResponseEntity.ok(service.create(request)); }
    @PutMapping("/{id}") public ResponseEntity<LibraryResponseDto> update(@PathVariable Long id,@RequestBody LibraryRequestDto request) { return ResponseEntity.ok(service.update(id,request)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
