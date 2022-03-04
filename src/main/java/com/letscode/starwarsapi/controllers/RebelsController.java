package com.letscode.starwarsapi.controllers;

import com.letscode.starwarsapi.models.Rebel;
import com.letscode.starwarsapi.models.RebelRequest;
import com.letscode.starwarsapi.services.RebelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rebels")
public class RebelsController {

    @Autowired
    private RebelsService rebelsService;

    @GetMapping
    public List<Rebel> listRebels(){
        List<Rebel> rebels = new ArrayList<>();
        rebels = rebelsService.getAllRebels();
        return rebels;
    }

    @PostMapping
    public Rebel createRebel(@RequestBody RebelRequest rebelRequest){
        Rebel rebel = rebelsService.createRebel(rebelRequest);
        return rebel;
    }

    @GetMapping(value = "/{id}")
    public Rebel findRebelById(@PathVariable("id") Long id){
        Rebel rebel = rebelsService.findRebelById(id);
        return rebel;
    }

    @DeleteMapping(value = "/{id}")
    public void deleteRebel(@PathVariable("id") Long id){
        rebelsService.deleteRebel(id);
    }

    //Metodo Patch
}
