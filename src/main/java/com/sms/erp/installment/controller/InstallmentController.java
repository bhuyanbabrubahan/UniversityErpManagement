package com.sms.erp.installment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import com.sms.erp.installment.dto.*;
import com.sms.erp.installment.service.InstallmentService;

@RestController
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','ACCOUNTANT')")
@RequestMapping("/api/erp/installment")
@RequiredArgsConstructor
public class InstallmentController {
	private final InstallmentService service;

	@GetMapping
	public Page<InstallmentResponseDto> all(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return service.findAll(page, size);
	}

	@PostMapping("/search")
	public Page<InstallmentResponseDto> search(@RequestBody(required = false) InstallmentSearchDto search,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		return service.search(search, page, size);
	}

	@GetMapping("/{id}")
	public ResponseEntity<InstallmentResponseDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<InstallmentResponseDto> create(@RequestBody InstallmentRequestDto request) {
		return ResponseEntity.ok(service.create(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<InstallmentResponseDto> update(@PathVariable Long id,
			@RequestBody InstallmentRequestDto request) {
		return ResponseEntity.ok(service.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
