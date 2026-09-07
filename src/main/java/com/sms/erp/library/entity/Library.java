package com.sms.erp.library.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name="erp_library")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Library {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String code;
    private String bookCode;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
    private String status;
    @Column(nullable=false)
    private String description;
    private String createdBy;
    private String updatedBy;
}
