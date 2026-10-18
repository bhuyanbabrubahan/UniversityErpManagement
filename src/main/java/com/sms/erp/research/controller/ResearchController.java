package com.sms.erp.research.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.research.dto.*;
import com.sms.erp.research.service.ResearchService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DEAN','FACULTY')")
@RequestMapping("/api/erp/research")
@RequiredArgsConstructor
public class ResearchController {
	private final ResearchService service;

	@GetMapping
	public Page<ResearchResponseDto> all(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return service.findAll(page, size);
	}

	@PostMapping("/search")
	public Page<ResearchResponseDto> search(@RequestBody(required = false) ResearchSearchDto search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		return service.search(search, page, size);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResearchResponseDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<ResearchResponseDto> create(@RequestBody ResearchRequestDto request) {
		return ResponseEntity.ok(service.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResearchResponseDto> update(@PathVariable Long id, @RequestBody ResearchRequestDto request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
