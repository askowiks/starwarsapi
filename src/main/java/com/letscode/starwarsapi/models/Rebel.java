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
//@Table(name = "rebels_db")
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

    @OneToMany(mappedBy = "rebelLocalization", cascade = CascadeType.ALL)
    List<Localization> localizations;

    public static Rebel of(RebelRequest rebelRequest){
        Rebel rebel = new Rebel();
        BeanUtils.copyProperties(rebelRequest,rebel);
        return rebel;
    }

    public RebelDTO toDto(){
        return RebelDTO.builder()
                .id(id)
                .name(name)
                .age(age)
                .gender(gender)
                .equipmentDTOList(equipments.stream()
                        .map(equipment -> equipment.toDto()).collect(Collectors.toList()))
                .lastLocalization(localizations.get(localizations.size()-1).toDto()) //Repensar a obtencao do indice
                .build();
    }

    public Rebel(RebelRequest rebelRequest){
        name = rebelRequest.getName();
        age = rebelRequest.getAge();
        gender = rebelRequest.getGender();
        equipments = rebelRequest.getEquipmentsRequest()
                .stream().map(equipmentRequest -> new Equipment(equipmentRequest,this))
                .collect(Collectors.toList());
        localizations = rebelRequest.getLocalizationRequestList()
                .stream().map(localization -> new Localization(localization,this))
                .collect(Collectors.toList());
    }
}
