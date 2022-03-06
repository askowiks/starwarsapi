package com.letscode.starwarsapi.models;


import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;

    public static Equipment of(EquipmentRequest equipmentRequest){
        Equipment equipment = new Equipment();
        BeanUtils.copyProperties(equipmentRequest,equipment);
        return equipment;
    }
}
