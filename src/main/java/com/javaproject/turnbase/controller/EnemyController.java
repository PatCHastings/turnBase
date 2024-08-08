package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.service.EnemyService;
import com.javaproject.turnbase.service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/enemies")
@CrossOrigin(origins = "http://localhost:5174")
public class EnemyController {

    @Autowired
    private EnemyRepository enemyRepository;

    @Autowired
    private EnemyService enemyService;



    @GetMapping
    public List<Enemy> getAllEnemies() {
        return enemyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Enemy getEnemyById(@PathVariable Long id) {
        return enemyRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Enemy createEnemy(@RequestBody Enemy enemy) {
        return enemyRepository.save(enemy);
    }

    @PutMapping("/{id}")
    public Enemy updateEnemy(@PathVariable Long id, @RequestBody Enemy enemyDetails) {
        Enemy enemy = enemyRepository.findById(id).orElse(null);
        if (enemy != null) {
            enemy.setEnemyName(enemyDetails.getEnemyName());
            enemy.setEnemyHealth(enemyDetails.getEnemyHealth());
            enemy.setEnemyType(enemyDetails.getEnemyType());
            enemy.setSize(enemyDetails.getSize());
            enemy.setType(enemyDetails.getType());
            enemy.setAlignment(enemyDetails.getAlignment());
            enemy.setArmorClass(enemyDetails.getArmorClass());
            enemy.setSpeed(enemyDetails.getSpeed());
            enemy.setStrength(enemyDetails.getStrength());
            enemy.setDexterity(enemyDetails.getDexterity());
            enemy.setConstitution(enemyDetails.getConstitution());
            enemy.setIntelligence(enemyDetails.getIntelligence());
            enemy.setWisdom(enemyDetails.getWisdom());
            enemy.setCharisma(enemyDetails.getCharisma());
            enemy.setChallengeRating(enemyDetails.getChallengeRating());
            enemy.setSpecialAbilities(enemyDetails.getSpecialAbilities());
            return enemyRepository.save(enemy);
        } else {
            return null;
        }
    }

    @PostMapping("/generate/{monsterIndex}")
    public Enemy createEnemyFromMonster(@PathVariable String monsterIndex) {
        return enemyService.createEnemyFromMonster(monsterIndex);
    }

    @DeleteMapping("/{id}")
    public void deleteEnemy(@PathVariable Long id) {
        enemyRepository.deleteById(id);
    }




    // New methods for D&D API integration.
    // Created MonsterController, separate for now.

//    @GetMapping("/external/monsters")
//    public String getAllMonsters() {
//        return monsterService.getMonsters();
//    }
//
//    @GetMapping("/external/monsters/{url}")
//    public String getMonsterDetails(@PathVariable String url) {
//        return monsterService.getMonsterDetails(url);
//    }
}
