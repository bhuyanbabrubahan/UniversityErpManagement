package com.sms.erp.library.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LibrarySearchDto {
    private String code;
    private String status;
    private String keyword;
}
