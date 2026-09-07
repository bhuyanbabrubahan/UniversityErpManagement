package com.sms.security.account.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.sms.security.account.dto.*;
import com.sms.security.account.service.AccountManagementService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/account-management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
public class AccountManagementController {
    private final AccountManagementService service;

    @GetMapping
    public Page<UserAccountResponseDto> search(@RequestParam(defaultValue="0") int page,
                                               @RequestParam(defaultValue="20") int size,
                                               @RequestParam(required=false) String keyword,
                                               @RequestParam(required=false) String role,
                                               @RequestParam(required=false) String status) {
        return service.search(page, size, keyword, role, status);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<UserAccountResponseDto> block(@PathVariable Long id, @Valid @RequestBody AccountStatusRequestDto request) {
        return ResponseEntity.ok(service.block(id, request.getReason()));
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<UserAccountResponseDto> unblock(@PathVariable Long id, @Valid @RequestBody AccountStatusRequestDto request) {
        return ResponseEntity.ok(service.unblock(id, request.getReason()));
    }
}
