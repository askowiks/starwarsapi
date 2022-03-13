package com.letscode.starwarsapi.controllers;

import com.letscode.starwarsapi.dto.*;
import com.letscode.starwarsapi.models.entities.Rebel;
import com.letscode.starwarsapi.services.RebelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rebels")
public class RebelsController {

    @Autowired
    private RebelsService rebelsService;

    @GetMapping
    public List<RebelDTO> listRebels(){
        List<RebelDTO> rebels = rebelsService.getAllRebels().stream()
                .map(rebel -> rebel.toDto()).collect(Collectors.toList());
        return rebels;
    }

    @PostMapping
    public RebelDTO createRebel(@Valid @RequestBody RebelRequestDTO rebelRequest){
        Rebel rebel = rebelsService.createRebel(rebelRequest);
        RebelDTO rebelDTO = rebel.toDto();
        return rebelDTO;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findRebelById(@PathVariable("id") Long id){
        Rebel rebel = rebelsService.findRebelById(id);
        if (rebel==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não existe rebelde com esse id "+ id);
        }
        RebelDTO rebelDTO = rebel.toDto();
        return ResponseEntity.ok(rebelDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteRebel(@PathVariable("id") Long id){
        rebelsService.deleteRebel(id);
    }

    @PatchMapping(value = "/{id}/accusation")
    public String accuseRebel(@PathVariable("id") Long id){
        RebelDTO rebelDTO = rebelsService.accuseTraitor(id);
        String mensageIsTraitor;
        if(rebelDTO.getIsTraitor() == true){
            mensageIsTraitor = " é um traidor!!!";
        }else{
            mensageIsTraitor = " não é um traidor!";
        }
        String mensage = "O rebelde " + rebelDTO.getName() + " recebeu uma acusação de ser traidor, agora ele possui "
                + rebelDTO.getQntAccusation() +" acusações. O rebelde " + rebelDTO.getName() + mensageIsTraitor;
        return mensage;
    }

    @PutMapping (value = "/{id}/update")
    public RebelDTO updateLocalization(@PathVariable Long id,@Valid @RequestBody LocalizationRequestDTO localizationRequest){
        Rebel updateRebel = rebelsService.updateLocalization(id,localizationRequest);
        return updateRebel.toDto();
    }
    //Metodo Patch

    @GetMapping(value = "/report")
    public ReportResponseDTO report(){
        return rebelsService.createReport();
    }

    @PutMapping(value = "/trade")
    public String trade(@RequestBody TradeEquipmentsDTO tradeEquipmentsDTO){
        return rebelsService.changeEquipments(tradeEquipmentsDTO);
    }
}
