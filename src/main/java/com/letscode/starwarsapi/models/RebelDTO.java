package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RebelDTO {

    private Long id;
    private String name;
    private Integer age;
    private String gender;

    private Integer qntAccusation;
    private Boolean isTraitor;

    List<EquipmentDTO> equipmentList = new ArrayList<>();

    private LocalizationDTO lastLocalization;

    private Integer points;

}
