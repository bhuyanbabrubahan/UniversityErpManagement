package com.sms.entity;

import java.util.ArrayList;
import java.util.List;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
public class Department extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "department_name",
            nullable = false,
            unique = true,
            length = 100)
    private String departmentName;

    @Column(
            name = "department_code",
            nullable = false,
            unique = true,
            length = 10)
    private String departmentCode;

    @Column(length = 500)
    private String description;
    
    
    
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordStatus status;
    
    

    
    @ToString.Exclude
    @OneToMany(
            mappedBy = "department",
            fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();
    
    
    @OneToMany(
            mappedBy = "department",
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private List<Faculty> faculties = new ArrayList<>();
    
    
}