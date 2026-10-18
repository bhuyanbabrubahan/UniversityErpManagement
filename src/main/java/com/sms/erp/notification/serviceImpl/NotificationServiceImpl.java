package com.sms.erp.notification.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.erp.notification.dto.NotificationRequestDto;
import com.sms.erp.notification.dto.NotificationResponseDto;
import com.sms.erp.notification.dto.NotificationSearchDto;
import com.sms.erp.notification.entity.Notification;
import com.sms.erp.notification.mapper.NotificationMapper;
import com.sms.erp.notification.repository.NotificationRepository;
import com.sms.erp.notification.service.NotificationService;
import com.sms.erp.notification.specification.NotificationSpecification;
import com.sms.security.util.SecurityUtils;
import com.sms.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service("erpNotificationServiceImpl")
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> findAll(
            int page,
            int size) {

        PageRequest pageable = PageRequest.of(
                Math.max(0, page), Math.max(1, Math.min(size, 100)),
                Sort.by("id").descending());
        if (isAdministrator()) {
            return repository.findAll(pageable).map(mapper::toResponse);
        }
        return repository.findAll(
                NotificationSpecification.visibleToRole(SecurityUtils.getCurrentRole()),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> search(
            NotificationSearchDto search,
            int page,
            int size) {

        PageRequest pageable = PageRequest.of(
                Math.max(0, page), Math.max(1, Math.min(size, 100)),
                Sort.by("id").descending());
        if (isAdministrator()) {
            return repository.findAll(
                    NotificationSpecification.bySearch(search), pageable)
                    .map(mapper::toResponse);
        }
        return repository.findAll(
                NotificationSpecification.bySearch(search)
                        .and(NotificationSpecification.visibleToRole(SecurityUtils.getCurrentRole())),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDto findById(Long id) {

        Notification notification = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found: " + id));

        if (!isAdministrator() && !isVisibleToCurrentRole(notification)) {
            throw new ResourceNotFoundException("Notification not found: " + id);
        }
        return mapper.toResponse(notification);
    }

    @Override
    public NotificationResponseDto create(
            NotificationRequestDto request) {

        Notification notification = mapper.toEntity(request);

        return mapper.toResponse(
                repository.save(notification)
        );
    }

    @Override
    public NotificationResponseDto update(
            Long id,
            NotificationRequestDto request) {

        Notification current = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Notification not found: " + id
                        )
                );

        mapper.updateEntity(request, current);

        return mapper.toResponse(
                repository.save(current)
        );
    }

    @Override
    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Notification not found: " + id
            );
        }

        repository.deleteById(id);
    }

    private boolean isAdministrator() {
        String role = SecurityUtils.getCurrentRole();
        return "SUPER_ADMIN".equals(role) || "ADMIN".equals(role)
                || "REGISTRAR".equals(role);
    }

    private boolean isVisibleToCurrentRole(Notification notification) {
        String role = SecurityUtils.getCurrentRole();
        String targetRole = notification.getTargetRole();
        boolean targetMatches = targetRole == null || targetRole.isBlank()
                || "ALL".equalsIgnoreCase(targetRole)
                || (role != null && role.equalsIgnoreCase(targetRole));
        String status = notification.getStatus();
        boolean published = status != null
                && ("PUBLISHED".equalsIgnoreCase(status)
                    || "ACTIVE".equalsIgnoreCase(status));
        boolean timeReached = notification.getPublishedAt() == null
                || !notification.getPublishedAt().isAfter(java.time.LocalDateTime.now());
        return targetMatches && published && timeReached;
    }

}