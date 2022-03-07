package com.letscode.starwarsapi.models;

import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer latitude;
    private Integer longitude;
    private String name;

    @ManyToOne
    @JoinColumn(name = "rebel_id", referencedColumnName = "id")
    private Rebel rebelLocalization;

    public static Localization of(LocalizationRequest localizationRequest){
        Localization localization = new Localization();
        BeanUtils.copyProperties(localizationRequest,localization);
        return localization;
    }

    public Localization(LocalizationRequest localizationRequest, Rebel rebel){
        latitude = localizationRequest.getLatitude();
        longitude = localizationRequest.getLongitude();
        name = localizationRequest.getName();
        rebelLocalization = rebel;
    }
}
