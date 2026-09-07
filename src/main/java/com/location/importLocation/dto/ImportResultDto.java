package com.location.importLocation.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportResultDto {

    @Builder.Default
    private Integer totalRows = 0;

    @Builder.Default
    private Integer successRows = 0;

    @Builder.Default
    private Integer failedRows = 0;

    @Builder.Default
    private Long startTime = System.currentTimeMillis();

    private Long endTime;

    private Long durationInSeconds;

    @Builder.Default
    private List<String> errors = new ArrayList<>();

}