package com.letscode.starwarsapi.services;

import com.letscode.starwarsapi.models.*;
import com.letscode.starwarsapi.repositories.EquipmentRepository;
import com.letscode.starwarsapi.repositories.LocalizationRepository;
import com.letscode.starwarsapi.repositories.RebelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public Rebel createRebel(RebelRequest rebelRequest){
        Rebel rebel = new Rebel(rebelRequest);
        rebelsRepository.save(rebel);
        return rebel;
    }


    public Rebel findRebelById(Long id){
        Rebel rebel = rebelsRepository.findById(id).get();    // Como o retorno é um optional, precisa do get. Tratar isso melhor depois
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

    public Rebel updateLocalization(Long id, LocalizationRequest localizationRequest){
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
}
