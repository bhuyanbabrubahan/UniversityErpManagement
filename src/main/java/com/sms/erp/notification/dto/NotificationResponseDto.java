package com.sms.erp.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
	private Long id;
	private String code;
	private String title;
	private String message;
	private String targetRole;
	private String channel;
	private LocalDateTime publishedAt;
	private String status;
	private String description;
	private Long createdByUserId;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedByUserId;
    private String updatedBy;
}
