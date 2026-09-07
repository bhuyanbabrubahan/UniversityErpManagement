package com.location.village.dto;


import com.location.tehsil.dto.TehsilResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "VillageResponseDto",
        description = "Village and its administrative hierarchy."
)
public class VillageResponseDto {

    private Long id;

    private String villageName;

    private String pincode;

    private TehsilResponseDto tehsil;
}