package com.sms.security.account.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="erp_account_status_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountStatusHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="target_user_id",nullable=false) private Long targetUserId;
 @Column(name="changed_by_user_id",nullable=false) private Long changedByUserId;
 @Column(name="old_status",nullable=false,length=30) private String oldStatus;
 @Column(name="new_status",nullable=false,length=30) private String newStatus;
 @Column(nullable=false,length=500) private String reason;
 @Column(name="changed_at",nullable=false) private LocalDateTime changedAt;
}
