package com.letscode.starwarsapi.services;

import com.letscode.starwarsapi.dto.*;
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

    public String changeEquipments(TradeEquipmentsDTO tradeEquipmentsDTO){
        // tem o rebelde
        Long idRebel1 = tradeEquipmentsDTO.getRebel1().getId();
        Long idRebel2 = tradeEquipmentsDTO.getRebel2().getId();
        Rebel rebel1ById = findRebelById(idRebel1);
        Rebel rebel2ById = findRebelById(idRebel2);
        if(rebel1ById==null || rebel2ById == null) return "Algum dos rebeldes nao existe";

        // tem traidor
        if(rebel1ById.getIsTraitor()== true || rebel2ById.getIsTraitor()== true){
            return "pelo menos um dos rebeldes é um traidor";
        }

        // se tem o equipamento
        boolean rebels1ContainsEquipment = false;
        List<String> nomesEquipamentosTroca = tradeEquipmentsDTO.getRebel1().getEquipmentRequestDTOList().stream()
                .map(equipmentRequestDTO -> equipmentRequestDTO.getName()).collect(Collectors.toList());
        List<String> listaNomeEquipamentosRebelde1 = rebel1ById.getEquipments().stream()
                .map(Equipment::getName).collect(Collectors.toList());
        for (String nome : nomesEquipamentosTroca) {
            rebels1ContainsEquipment = listaNomeEquipamentosRebelde1.contains(nome);
        }

        boolean rebels2ContainsEquipment = false;
        List<String> nomesEquipamentosTroca2 = tradeEquipmentsDTO.getRebel2().getEquipmentRequestDTOList().stream()
                .map(equipmentRequestDTO -> equipmentRequestDTO.getName()).collect(Collectors.toList());
        List<String> listaNomeEquipamentosRebelde2 = rebel2ById.getEquipments().stream()
                .map(Equipment::getName).collect(Collectors.toList());
        for (String nome : nomesEquipamentosTroca2) {
            rebels2ContainsEquipment = listaNomeEquipamentosRebelde2.contains(nome);
        }

       String resposta = " ";
        String resposta2 = " ";
        if (rebels1ContainsEquipment == true && rebels2ContainsEquipment == true){
            resposta = "deu certo";
        }else{
            resposta = "Deu ruim";
        }
//        return resposta;

        // se tem a quantidade
            //equipamento do rebelde >= equipamento qu quer ser trocaddo

        // pontuacao
        int pointsRebel1 = tradeEquipmentsDTO.getRebel1().getEquipmentRequestDTOList().stream().map(equipmentRequestDTO -> equipmentRequestDTO.toTrade()).collect(Collectors.toList()).stream().mapToInt(equipmentToTrade -> equipmentToTrade.getPoints()).sum();
        int pointsRebel2 = tradeEquipmentsDTO.getRebel2().getEquipmentRequestDTOList().stream().map(equipmentRequestDTO -> equipmentRequestDTO.toTrade()).collect(Collectors.toList()).stream().mapToInt(equipmentToTrade -> equipmentToTrade.getPoints()).sum();
        int diferenca = pointsRebel1 - pointsRebel2;

        if(diferenca==0){
            resposta2 = "a troca pode ser feita";
        }else{
            resposta2 = "a diferenca de pontuacao eh " + diferenca;
        }

        return resposta+resposta2;
    }


}
