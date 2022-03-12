package com.letscode.starwarsapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizationDTO {

    private Long id;
    private Integer latitude;
    private Integer longitude;
    private String name;
}
