package com.letscode.starwarsapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EquipmentsEnum {
    ARMA("arma",4),
    MUNICAO("municao", 3),
    AGUA("agua", 2),
    COMIDA("comida", 1);

    private String name;
    private Integer equipmentPoints;
}
