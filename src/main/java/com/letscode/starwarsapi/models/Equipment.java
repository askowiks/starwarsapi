package com.letscode.starwarsapi.models;


import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "rebel_id",referencedColumnName = "id")
    private Rebel rebel;

    public static Equipment of(EquipmentRequest equipmentRequest){
        Equipment equipment = new Equipment();
        BeanUtils.copyProperties(equipmentRequest,equipment);
        return equipment;
    }

    public Equipment(EquipmentRequest equipmentRequest, Rebel rebel){
        name = equipmentRequest.getName();
        quantity = equipmentRequest.getQuantity();
        this.rebel = rebel;
    }
}
