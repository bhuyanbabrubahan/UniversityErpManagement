package com.sms.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique Subject Code
    @Column(nullable = false, unique = true, length = 20)
    private String subjectCode;

    // Subject Name
    @Column(nullable = false, length = 100)
    private String subjectName;

    // Credit Score
    @Column(nullable = false)
    private Integer credits;

    // Theory Marks
    @Column(nullable = false)
    private Integer theoryMarks;

    // Practical Marks
    @Column(nullable = false)
    private Integer practicalMarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordStatus status;

    // Many Subjects -> One Semester
    @ToString.Exclude
    @ManyToOne(
    		fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "semester_id", nullable = false)
    private Semester semester;
    
    @JsonIgnore
    @OneToMany(
            mappedBy = "subject",
            fetch = FetchType.LAZY
    )
    private List<FacultySubjectMapping> facultyMappings =
            new ArrayList<>();
    

}