package com.letscode.starwarsapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.letscode.starwarsapi.models.dto.LocalizationDTO;
import com.letscode.starwarsapi.models.LocalizationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer latitude;
    private Integer longitude;
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "rebel_id", referencedColumnName = "id")
    private Rebel rebelLocalization;

    public static Localization of(LocalizationRequestDTO localizationRequest){
        Localization localization = new Localization();
        BeanUtils.copyProperties(localizationRequest,localization);
        return localization;
    }

    public LocalizationDTO toDto(){
        return LocalizationDTO.builder()
                .id(id)
                .latitude(latitude)
                .longitude(longitude)
                .name(name)
                .build();
    }

    public Localization(LocalizationRequestDTO localizationRequest, Rebel rebel){
        latitude = localizationRequest.getLatitude();
        longitude = localizationRequest.getLongitude();
        name = localizationRequest.getName();
        rebelLocalization = rebel;
    }
}
