package com.letscode.starwarsapi.services;

import com.letscode.starwarsapi.dto.*;
import com.letscode.starwarsapi.enums.EquipmentsEnum;
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
import java.util.stream.Collectors;

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
                if(equipment.getName()=="Arma"){
                    quantityWeapon += equipment.getQuantity();
                }
                if(equipment.getName()=="Municao"){
                    quantityAmmo += equipment.getQuantity();
                }
                if(equipment.getName()=="Agua"){
                    quantityWater += equipment.getQuantity();
                }
                if(equipment.getName()=="Comida"){
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

    public Equipment findEquipment(List<Equipment> listEquipment, String name){
        for (Equipment equipment : listEquipment) {
            if (equipment.getName() == name) return equipment;
        }
        return null;
    }


    public boolean verifyNameAndQuantity(List<EquipmentToTrade> equipmentToTradeList, List<Equipment> equipmentListRebelToGive){
        boolean PossuiEquipamento = false;
        boolean PossuiQuantidade = true;

        for (EquipmentToTrade equipmentToTrade : equipmentToTradeList) {
            Equipment equipment = findEquipment(equipmentListRebelToGive, equipmentToTrade.getName());

            if(equipment == null){
                PossuiEquipamento = false;
            }else{
                PossuiEquipamento = true;

                int newQuantity = equipment.getQuantity() - equipmentToTrade.getQuantity();
                if (newQuantity < 0) PossuiQuantidade = false;
            }
        }

        if(PossuiQuantidade == false || PossuiEquipamento==false) return false;
        return true;
    }

    public int verifyPoints(List<EquipmentToTrade> equipmentToTradeList1, List<EquipmentToTrade> equipmentToTradeList2){
        int pointsRebel1 = equipmentToTradeList1.stream()
                .mapToInt(equipmentToTrade -> equipmentToTrade.getPoints()).sum();

        int pointsRebel2 = equipmentToTradeList2.stream()
                .mapToInt(equipmentToTrade -> equipmentToTrade.getPoints()).sum();

        return pointsRebel1 - pointsRebel2;
    }

    public void tradeEquipment(Rebel rebelToReceive, List<EquipmentToTrade> equipmentToTradeList,
                                 List<Equipment> equipmentListRebelToGive){

        for (EquipmentToTrade equipmentToTrade : equipmentToTradeList) {
            Equipment equipment = findEquipment(equipmentListRebelToGive, equipmentToTrade.getName());

            int newQuantity = equipment.getQuantity() - equipmentToTrade.getQuantity();
            int newPoints = newQuantity * EquipmentsEnum.getPoints(equipment.getName());

            equipment.setQuantity(newQuantity);
            equipment.setPoints(newPoints);

            Equipment newEquipmentRebel2 = new Equipment(equipmentToTrade,rebelToReceive);

            equipmentRepository.save(equipment);
            equipmentRepository.save(newEquipmentRebel2);
        }
    }

    public String verifyConditions(Rebel rebel1,Rebel rebel2,
                                 List<Equipment> equipmentsRebel1,
                                 List<Equipment> equipmentsRebel2 ,
                                 List<EquipmentToTrade> equipmentRequestToChangeRebel1,
                                 List<EquipmentToTrade> equipmentRequestToChangeRebel2 ){
        // tem o rebelde
        if(rebel1==null || rebel2 == null) return "Algum dos rebeldes nao existe";

        // tem traidor
        if(rebel1.getIsTraitor()== true || rebel2.getIsTraitor()== true){
            return "pelo menos um dos rebeldes é um traidor";
        }

        // se tem o equipamento e a quantiddade
        boolean verifyConditions1 = verifyNameAndQuantity(equipmentRequestToChangeRebel1, equipmentsRebel1);
        boolean verifyConditions2 = verifyNameAndQuantity(equipmentRequestToChangeRebel2, equipmentsRebel2);
        if(verifyConditions1==false || verifyConditions2==false){
            return "Não é possível realizar a troca! Os rebeldes não possuem o item selecionado ou quantidade suficiente deles.";
        }

        // pontuacao
        int difference = verifyPoints(equipmentRequestToChangeRebel1,equipmentRequestToChangeRebel2);
        if(difference != 0){
            return "A troca não respeita a igualdade de pontuação. A diferença de pontos é: " + difference;
        }

        return "A troca atende a todos os requisitos.";
    }

    public String changeEquipments(TradeEquipmentsDTO tradeEquipmentsDTO){

        List<EquipmentToTrade> equipmentRequestToChangeRebel1 = tradeEquipmentsDTO.getRebel1().getEquipmentRequestDTOList().stream().map(equipmentRequest -> equipmentRequest.toTrade()).collect(Collectors.toList());
        List<EquipmentToTrade> equipmentRequestToChangeRebel2 = tradeEquipmentsDTO.getRebel2().getEquipmentRequestDTOList().stream().map(equipmentRequest -> equipmentRequest.toTrade()).collect(Collectors.toList());

        Long idRebel1 = tradeEquipmentsDTO.getRebel1().getId();
        Long idRebel2 = tradeEquipmentsDTO.getRebel2().getId();

        Rebel rebel1 = findRebelById(idRebel1);
        Rebel rebel2 = findRebelById(idRebel2);

        List<Equipment> equipmentsRebel1 = rebel1.getEquipments();
        List<Equipment> equipmentsRebel2 = rebel2.getEquipments();

        String returnVerification = verifyConditions(rebel1, rebel2, equipmentsRebel1, equipmentsRebel2,
                equipmentRequestToChangeRebel1, equipmentRequestToChangeRebel2);
//
//        // tem o rebelde
//        if(rebel1==null || rebel2 == null) return "Algum dos rebeldes nao existe";
//
//        // tem traidor
//        if(rebel1.getIsTraitor()== true || rebel2.getIsTraitor()== true){
//            return "pelo menos um dos rebeldes é um traidor";
//        }
//
//        // se tem o equipamento e a quantiddade
//        boolean verifyConditions1 = verifyNameAndQuantity(equipmentRequestToChangeRebel1, equipmentsRebel1);
//        boolean verifyConditions2 = verifyNameAndQuantity(equipmentRequestToChangeRebel2, equipmentsRebel2);
//        if(verifyConditions1==false || verifyConditions2==false){
//            return "Não é possível realizar a troca! Os rebeldes não possuem o item selecionado ou quantidade suficiente deles.";
//        }
//
//        // pontuacao
//        int difference = verifyPoints(equipmentRequestToChangeRebel1,equipmentRequestToChangeRebel2);
//        if(difference != 0){
//            return "A troca não respeita a igualdade de pontuação. A diferença de pontos é: " + difference;
//        }

        String answer = "";

        if(returnVerification == "A troca atende a todos os requisitos."){
            // Fazer a troca
            tradeEquipment(rebel2,equipmentRequestToChangeRebel1,equipmentsRebel1);
            tradeEquipment(rebel1,equipmentRequestToChangeRebel2,equipmentsRebel2);
            answer = "A troca foi realizada com sucesso!!!";
        }else{
            answer = returnVerification;
        }

        return answer;
    }


}
