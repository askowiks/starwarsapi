package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizationRequest {

    private Integer latitude;
    private Integer longitude;
    private String name;
}
