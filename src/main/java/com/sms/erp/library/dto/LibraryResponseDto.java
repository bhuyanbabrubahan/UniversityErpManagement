package com.sms.erp.library.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LibraryResponseDto {
    private Long id;
    private String code;
    private String bookCode;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
