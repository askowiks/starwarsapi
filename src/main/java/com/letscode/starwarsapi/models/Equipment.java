package com.letscode.starwarsapi.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer quantity;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "rebel_id",referencedColumnName = "id")
    private Rebel rebel;

    public static Equipment of(EquipmentRequest equipmentRequest){
        Equipment equipment = new Equipment();
        BeanUtils.copyProperties(equipmentRequest,equipment);
        return equipment;
    }

    public  EquipmentDTO toDto(){
        return EquipmentDTO.builder()
                .id(id)
                .name(name)
                .quantity(quantity)
                .build();
    }

    public Equipment(EquipmentRequest equipmentRequest, Rebel rebel){
        name = equipmentRequest.getName();
        quantity = equipmentRequest.getQuantity();
        this.rebel = rebel;
    }
}
