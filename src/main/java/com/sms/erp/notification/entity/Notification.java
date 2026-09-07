package com.sms.erp.notification.entity;

import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "erp_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    private String title;

    private String message;

    @Column(name = "targetRole")
    private String targetRole;

    private String channel;

    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    private String status;

    @Column(nullable = false)
    private String description;
}