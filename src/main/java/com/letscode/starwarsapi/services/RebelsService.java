package com.letscode.starwarsapi.services;

import com.letscode.starwarsapi.models.*;
import com.letscode.starwarsapi.repositories.RebelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class RebelsService {

    @Autowired
    private RebelsRepository rebelsRepository;

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

    public Rebel update(Long id, LocalizationRequest localizationRequest){
        Rebel rebelById = findRebelById(id);
        List<Localization> localizations = rebelById.getLocalizations();
        Localization newLocalizatio = new Localization(localizationRequest,rebelById);
        // ...
        return rebelById;
    }
}
