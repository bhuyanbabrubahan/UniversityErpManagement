package com.sms.security.account.serviceImpl;

import java.time.LocalDateTime;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sms.security.account.dto.UserAccountResponseDto;
import com.sms.security.account.service.AccountManagementService;
import com.sms.security.account.entity.AccountStatusHistory;
import com.sms.security.account.repository.AccountStatusHistoryRepository;
import com.sms.security.entity.User;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;
import com.sms.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountManagementServiceImpl implements AccountManagementService {
    private final UserRepository userRepository;
    private final AccountStatusHistoryRepository historyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserAccountResponseDto> search(int page, int size, String keyword, String role, String status) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        Specification<User> spec = (root, query, cb) -> {
            var p = cb.conjunction();
            if (!q.isBlank()) {
                String like = "%" + q + "%";
                p = cb.and(p, cb.or(cb.like(cb.lower(root.get("email")), like), cb.like(cb.lower(root.get("mobileNumber")), like)));
            }
            if (role != null && !role.isBlank()) p = cb.and(p, cb.equal(root.get("role"), Role.valueOf(role.toUpperCase())));
            if (status != null && !status.isBlank()) p = cb.and(p, cb.equal(root.get("status"), UserStatus.valueOf(status.toUpperCase())));
            return p;
        };
        return userRepository.findAll(spec, PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "id"))).map(this::toDto);
    }

    @Override
    public UserAccountResponseDto block(Long id, String reason) { return changeStatus(id, UserStatus.BLOCKED, reason); }
    @Override
    public UserAccountResponseDto unblock(Long id, String reason) { return changeStatus(id, UserStatus.ACTIVE, reason); }

    private UserAccountResponseDto changeStatus(Long id, UserStatus target, String reason) {
        User targetUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        User actor = currentUser();
        if (actor.getId().equals(targetUser.getId())) throw new IllegalStateException("You cannot change your own account status.");
        if (actor.getRole() == Role.ADMIN && targetUser.getRole() == Role.SUPER_ADMIN) throw new IllegalStateException("ADMIN cannot block or modify a SUPER_ADMIN account.");
        UserStatus oldStatus = targetUser.getStatus();
        targetUser.setStatus(target);
        if (target == UserStatus.BLOCKED) targetUser.setAccountLockedUntil(LocalDateTime.of(9999,12,31,23,59));
        else targetUser.setAccountLockedUntil(null);
        targetUser.setUpdatedBy(actor.getEmail());
        userRepository.save(targetUser);
        historyRepository.save(AccountStatusHistory.builder().targetUserId(targetUser.getId()).changedByUserId(actor.getId())
            .oldStatus(oldStatus.name()).newStatus(target.name()).reason(reason.trim()).changedAt(LocalDateTime.now()).build());
        return toDto(targetUser);
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) throw new IllegalStateException("Authenticated user not found");
        return userRepository.findByEmail(auth.getName()).orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private UserAccountResponseDto toDto(User u) {
        return UserAccountResponseDto.builder().id(u.getId()).email(u.getEmail()).mobileNumber(u.getMobileNumber())
            .role(u.getRole().name()).status(u.getStatus().name()).emailVerified(Boolean.TRUE.equals(u.getEmailVerified()))
            .mobileVerified(Boolean.TRUE.equals(u.getMobileVerified())).accountLockedUntil(u.getAccountLockedUntil()).build();
    }
}
