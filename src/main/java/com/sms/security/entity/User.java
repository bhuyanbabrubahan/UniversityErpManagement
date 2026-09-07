package com.sms.security.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sms.common.entity.BaseEntity;
import com.sms.entity.Faculty;
import com.sms.entity.Student;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    // ==================== PRIMARY KEY ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==================== LOGIN INFORMATION ====================

    @Column(
        nullable = false,
        unique = true,
        length = 150
    )
    private String email;

    @Column(
        nullable = false
    )
    private String password;

    // ==================== AUTHORIZATION ====================

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 30
    )
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 30
    )
    private UserStatus status;

    // ==================== MOBILE AUTHENTICATION ====================

    @Column(
        name = "mobile_number",
        nullable = false,
        unique = true,
        length = 10
    )
    private String mobileNumber;

    @Column(
        name = "mobile_verified",
        nullable = false
    )
    private Boolean mobileVerified = false;

    @Column(
        name = "email_verified",
        nullable = false
    )
    private Boolean emailVerified = false;

    // ==================== LOGIN SECURITY ====================

    @Column(
        name = "failed_login_attempts",
        nullable = false
    )
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    // ==================== PROFILE RELATIONS ====================

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private Student student;

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private Faculty faculty;

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private Admin admin;

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private SuperAdmin superAdmin;

    // ==================== SPRING SECURITY ====================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
            new SimpleGrantedAuthority(
                "ROLE_" + role.name()
            )
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        if (accountLockedUntil == null) {
            return true;
        }

        return accountLockedUntil.isBefore(
            LocalDateTime.now()
        );
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}