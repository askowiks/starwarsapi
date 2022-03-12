package com.letscode.starwarsapi.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.letscode.starwarsapi.models.dto.EquipmentDTO;
import com.letscode.starwarsapi.models.EquipmentRequestDTO;
import com.letscode.starwarsapi.enums.EquipmentsEnum;
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
    private Integer points;      //So esta sendo calculado quando utilizado o construtor

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "rebel_id",referencedColumnName = "id")
    private Rebel rebel;

    public static Equipment of(EquipmentRequestDTO equipmentRequest){
        Equipment equipment = new Equipment();
        BeanUtils.copyProperties(equipmentRequest,equipment);
        return equipment;
    }

    public EquipmentDTO toDto(){
        return EquipmentDTO.builder()
                .id(id)
                .name(name)
                .quantity(quantity)
                .points(points)
                .build();
    }

    public Equipment(EquipmentRequestDTO equipmentRequest, Rebel rebel){
        name = EquipmentsEnum.valueOf(equipmentRequest.getName().toUpperCase()).getName();   // Caso não exista o enum vai ter que tratar um IllegalArgumentException
        quantity = equipmentRequest.getQuantity();
        this.rebel = rebel;
        points = quantity* EquipmentsEnum.valueOf(equipmentRequest.getName().toUpperCase()).getEquipmentPoints();
    }
}
