package com.letscode.starwarsapi.services;

import com.letscode.starwarsapi.models.*;
import com.letscode.starwarsapi.models.dto.RebelDTO;
import com.letscode.starwarsapi.models.entities.Equipment;
import com.letscode.starwarsapi.models.entities.Localization;
import com.letscode.starwarsapi.models.entities.Rebel;
import com.letscode.starwarsapi.repositories.EquipmentRepository;
import com.letscode.starwarsapi.repositories.LocalizationRepository;
import com.letscode.starwarsapi.repositories.RebelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class RebelsService {

    @Autowired
    private RebelsRepository rebelsRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private LocalizationRepository localizationRepository;

    public List<Rebel> getAllRebels(){
        return rebelsRepository.findAll();
    }

    public Rebel createRebel(RebelRequestDTO rebelRequest){
        Rebel rebel = new Rebel(rebelRequest);
        rebelsRepository.save(rebel);
        return rebel;
    }


    public Rebel findRebelById(Long id){
        Rebel rebel = rebelsRepository.findById(id).orElse(null);    // Como o retorno é um optional, precisa do get. Tratar isso melhor depois
        return rebel;
    }

    public void deleteRebel(Long id) {
        rebelsRepository.deleteById(id);
    }

    public RebelDTO accuseTraitor(Long id){
        Rebel rebel = findRebelById(id);
        int newQntAccusation = rebel.getQntAccusation() + 1;
        if (newQntAccusation >= 3) rebel.setIsTraitor(true);
        rebel.setQntAccusation(newQntAccusation);
        rebelsRepository.save(rebel);
        RebelDTO rebelDTO = rebel.toDto();
        return rebelDTO;
    }

    public Rebel updateLocalization(Long id, LocalizationRequestDTO localizationRequest){
//        -> funciona alterando a localização atual do rebelde, mas não funciona com listra de posição;
//        Rebel rebelById = findRebelById(id);
//        Localization localizationById = localizationRepository.findById(id).get();
//        List<Localization> localizations = rebelById.getLocalizations();
//        Localization newLocalization = new Localization(localizationRequest,rebelById);
//        rebelById.setLocalizations(localizations);
//        localizationById.setLongitude(newLocalization.getLongitude());
//        localizationById.setLatitude(newLocalization.getLatitude());
//        localizationById.setName(newLocalization.getName());
//        localizationRepository.save(localizationById);
//        RebelDTO rebelDTO = rebelById.toDto();
//        return rebelById;

//        - alternativa
        Rebel rebel = findRebelById(id);
        Localization newLocalizatio = new Localization(localizationRequest,rebel);
        localizationRepository.save(newLocalizatio);
        return rebel;
    }

    public ReportResponseDTO createReport(){
        List<Rebel> traitors = new ArrayList<>();
        List<Rebel> rebels = new ArrayList<>();

        List<Rebel> allRebels = getAllRebels();
        for (Rebel rebel : allRebels) {
            if(rebel.getIsTraitor()==false){
                rebels.add(rebel);
            }else{
                traitors.add(rebel);
            }
        }

        double total = allRebels.size();
        double totalRebels = rebels.size();
        double totalTraitors = traitors.size();

        double percentRebels = (totalRebels / total) * 100;
        double percentTraitors = (totalTraitors / total) * 100;

        int totalPoints = allRebels.stream().mapToInt(Rebel::getRebelPoints).sum();
        int totalPointsTraitor = traitors.stream().mapToInt(Rebel::getRebelPoints).sum();

        int quantityWater = 0;
        int quantityFood = 0;
        int quantityWeapon = 0;
        int quantityAmmo = 0;

        for (Rebel rebel : rebels) {
            for (Equipment equipment : rebel.getEquipments()) {
                if(equipment.getName()=="Weapon"){
                    quantityWeapon += equipment.getQuantity();
                }
                if(equipment.getName()=="Ammunition"){
                    quantityAmmo += equipment.getQuantity();
                }
                if(equipment.getName()=="Water"){
                    quantityWater += equipment.getQuantity();
                }
                if(equipment.getName()=="Food"){
                    quantityFood += equipment.getQuantity();
                }
            }
        }

        double averageWeapon = quantityWeapon/totalRebels;
        double averageAmmo = quantityAmmo/totalRebels;
        double averageWater = quantityWater/totalRebels;
        double averageFood = quantityFood/totalRebels;

        ResourceAverageResponseDTO resourceAverageResponseDTO = ResourceAverageResponseDTO.builder()
                .averageAmmo(averageAmmo)
                .averageFood(averageFood)
                .averageWater(averageWater)
                .averageWeapon(averageWeapon)
                .totalRebels(totalRebels)
                .build();

        ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                .rebelPercentage(percentRebels)
                .traitorsPercentage(percentTraitors)
                .resourceAverage(resourceAverageResponseDTO)
                .lostPoints(totalPointsTraitor)
                .build();

        return reportResponseDTO;
    }
}
