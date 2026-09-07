package com.sms.entity;

import java.util.ArrayList;
import java.util.List;

import com.location.address.entity.Address;
import com.sms.enums.Designation;
import com.sms.enums.Qualification;
import com.sms.security.entity.EmployeeBase;
import com.sms.security.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "faculties")
@Getter
@Setter
@NoArgsConstructor
public class Faculty extends EmployeeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "designation", nullable = false, length = 50)
    private Designation designation;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualification", nullable = false, length = 50)
    private Qualification qualification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @ToString.Exclude
    private Department department;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @ToString.Exclude
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    @ToString.Exclude
    private Address address;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FacultySubjectMapping> subjectMappings = new ArrayList<>();
}