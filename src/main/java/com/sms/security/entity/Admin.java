package com.sms.security.entity;

import com.location.address.entity.Address;
import com.sms.security.enums.AdminDesignation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends EmployeeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "designation", nullable = false, length = 50)
    private AdminDesignation designation;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private Address address;
}