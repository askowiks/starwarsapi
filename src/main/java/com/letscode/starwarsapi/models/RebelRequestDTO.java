package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebelRequestDTO {

    @NotBlank(message = "nome rebelde ")
    private String name;
    @NotNull(message = "idade ")
    @Min(value = 1, message = "a idade tem que ser maior que 0")
    private Integer age;
    @NotBlank(message = "genero ")
    private String gender;

    @Valid
    private List<EquipmentRequestDTO> equipmentsRequest = new ArrayList<>();

    @Valid
    @NotNull(message = "eh necessario uma localizacao")
    private List<LocalizationRequestDTO> localizationRequestList;
}
