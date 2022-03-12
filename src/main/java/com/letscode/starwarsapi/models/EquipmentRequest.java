package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentRequest {

    @NotBlank(message = "nome equipment ")
    private String name;

    @NotNull(message = "quantidade equipment ")
    @Min(value = 1,message = "a quantidade de itens tem que ser pelo menos 1")
    private Integer quantity;

}
