package com.sms.common.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity {

    // =========================================================
    // CREATED AT
    // =========================================================

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;


    // =========================================================
    // UPDATED AT
    // =========================================================

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // =========================================================
    // CREATED BY
    // =========================================================

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_by")
    private String createdBy;


    // =========================================================
    // UPDATED BY
    // =========================================================

    @Column(name = "updated_by_user_id")
    private Long updatedByUserId;

    @Column(name = "updated_by")
    private String updatedBy;


    // =========================================================
    // CREATE
    // =========================================================

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        // DO NOT SET updatedAt HERE.
        // It must remain NULL on first INSERT.
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}
