package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.service.EnemyService;
import com.javaproject.turnbase.service.MonsterService;
import com.javaproject.turnbase.util.AbilityScoreGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/enemies")
@CrossOrigin(origins = "http://localhost:5174")
public class EnemyController {

    @Autowired
    private EnemyRepository enemyRepository;

    @Autowired
    private EnemyService enemyService;

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());


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
            enemy.setName(enemyDetails.getName());
            enemy.setHealth(enemyDetails.getHealth());
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
            enemy.setActions(enemyDetails.getActions());

            logger.info("Saving enemy: " + enemy.getName() + " with updated ability modifiers.");

            return enemyRepository.save(enemy);
        } else {
            return null;
        }
    }

    @PutMapping("/{id}/update-scores")
    public ResponseEntity<Enemy> updateEnemyAbilityScores(@PathVariable Long id, @RequestBody Map<String, Integer> abilityScores) {
        Enemy existingEnemy = enemyRepository.findById(id).orElse(null);
        if (existingEnemy == null) {
            return ResponseEntity.notFound().build();
        }

        int strength = abilityScores.getOrDefault("strength", existingEnemy.getStrength());
        int dexterity = abilityScores.getOrDefault("dexterity", existingEnemy.getDexterity());
        int constitution = abilityScores.getOrDefault("constitution", existingEnemy.getConstitution());
        int intelligence = abilityScores.getOrDefault("intelligence", existingEnemy.getIntelligence());
        int wisdom = abilityScores.getOrDefault("wisdom", existingEnemy.getWisdom());
        int charisma = abilityScores.getOrDefault("charisma", existingEnemy.getCharisma());

        existingEnemy.setStrength(strength);
        existingEnemy.setStrengthModifier(AbilityScoreGenerator.calculateModifier(strength));

        existingEnemy.setDexterity(dexterity);
        existingEnemy.setDexterityModifier(AbilityScoreGenerator.calculateModifier(dexterity));

        existingEnemy.setConstitution(constitution);
        existingEnemy.setConstitutionModifier(AbilityScoreGenerator.calculateModifier(constitution));

        existingEnemy.setIntelligence(intelligence);
        existingEnemy.setIntelligenceModifier(AbilityScoreGenerator.calculateModifier(intelligence));

        existingEnemy.setWisdom(wisdom);
        existingEnemy.setWisdomModifier(AbilityScoreGenerator.calculateModifier(wisdom));

        existingEnemy.setCharisma(charisma);
        existingEnemy.setCharismaModifier(AbilityScoreGenerator.calculateModifier(charisma));

        // Save the updated enemy back to the database
        Enemy updatedEnemy = enemyRepository.save(existingEnemy);
        return ResponseEntity.ok(updatedEnemy);
    }

    @PostMapping("/generate/{monsterIndex}")
    public Enemy createEnemyFromMonster(@PathVariable String monsterIndex) {
        return enemyService.createEnemyFromMonster(monsterIndex);
    }

    @DeleteMapping("/{id}")
    public void deleteEnemy(@PathVariable Long id) {
        enemyRepository.deleteById(id);
    }

}
