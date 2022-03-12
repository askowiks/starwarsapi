package com.letscode.starwarsapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EquipmentsEnum {
    ARMA("Weapon",4),
    MUNICAO("Ammunition", 3),
    AGUA("Water", 2),
    COMIDA("Food", 1);

    private String name;
    private Integer equipmentPoints;
}
