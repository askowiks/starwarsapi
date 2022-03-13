package com.letscode.starwarsapi.dto;

import com.letscode.starwarsapi.models.entities.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentToTrade {

    String name;
    Integer quantity;
    Integer points;

}
