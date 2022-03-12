package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizationRequestDTO {

    @NotNull(message = "latitude ")
    private Integer latitude;
    @NotNull(message = "longitude ")
    private Integer longitude;
    @NotBlank(message = "nome do local ")
    private String name;
}
