package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "rebels_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rebel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String gender;

    @OneToMany(mappedBy = "rebel", cascade = CascadeType.ALL)
    List<Equipment> equipments = new ArrayList<>();

    public static Rebel of(RebelRequest rebelRequest){
        Rebel rebel = new Rebel();
        BeanUtils.copyProperties(rebelRequest,rebel);
        return rebel;
    }

    public Rebel(RebelRequest rebelRequest){
        name = rebelRequest.getName();
        age = rebelRequest.getAge();
        gender = rebelRequest.getGender();
        equipments = rebelRequest.getEquipmentsRequest()
                .stream().map(equipmentRequest -> new Equipment(equipmentRequest,this))
                .collect(Collectors.toList());
    }
}
