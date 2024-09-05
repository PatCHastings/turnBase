package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Monster;
import com.javaproject.turnbase.repository.MonsterRepository;
import com.javaproject.turnbase.service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external/monsters")
@CrossOrigin(origins = "http://localhost:5174")
public class MonsterController {

    @Autowired
    private MonsterService monsterService;

    @Autowired
    private MonsterRepository monsterRepository;

    @GetMapping("/fetch")
    public String getAllMonsters() {
        return monsterService.getMonstersFromAPI();
    }
    @GetMapping("/raw")
    public String getAllMonstersRaw() {
        return monsterService.getMonstersFromAPI();
    }

    @GetMapping
    public List<Monster> getAllFetchedMonsters() {
        return monsterRepository.findAll();
    }

    @GetMapping("/{index}")
    public String getMonsterDetails(@PathVariable String index) {
        return monsterService.getMonsterDetails("/api/monsters/" + index);
    }

    @GetMapping("/raw/{index}")
    public String getMonsterDetailsRaw(@PathVariable String index) {
        return monsterService.getMonsterDetailsRaw("/api/monsters/" + index);
    }

    // endpoint to fetch monsters with a challengeRating of 0
    @GetMapping("/challenge-rating/{rating}")
    public List<Monster> getMonstersWithChallengeRating(@PathVariable int rating) {
        return monsterRepository.findByChallengeRating(rating);
    }

    @PostMapping
    public Monster saveMonster(@RequestBody Monster monster) {
        return monsterService.saveMonster(monster);
    }
}
