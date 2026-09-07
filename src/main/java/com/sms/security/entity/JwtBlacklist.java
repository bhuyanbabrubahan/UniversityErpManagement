package com.sms.security.entity;


import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "jwt_blacklist",
        indexes = {
                @Index(
                        name = "idx_blacklist_token",
                        columnList = "token"
                ),
                @Index(
                        name = "idx_blacklist_expiry",
                        columnList = "expiryDate"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtBlacklist extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            nullable = false,
            unique = true,
            length = 500
    )
    private String token;


    @Column(
            nullable = false
    )
    private LocalDateTime expiryDate;

}