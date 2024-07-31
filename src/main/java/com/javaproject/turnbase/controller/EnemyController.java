package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.repository.EnemyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/enemies")
public class EnemyController {

    @Autowired
    private EnemyRepository enemyRepository;

    private static final String[] ENEMY_NAMES = {"Goblin", "Orc", "Troll", "Dragon"};

    @GetMapping
    public List<Enemy> getAllEnemies() {
        return enemyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Enemy getEnemyById(@PathVariable Long id) {
        return enemyRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Enemy createEnemy() {
        Enemy enemy = new Enemy();
        enemy.setEnemyName(generateRandomEnemyName());
        enemy.setEnemyHealth(100); // Default health value
        return enemyRepository.save(enemy);
    }

    @PutMapping("/{id}")
    public Enemy updateEnemy(@PathVariable Long id, @RequestBody Enemy enemyDetails) {
        Enemy enemy = enemyRepository.findById(id).orElse(null);
        if (enemy != null) {
            enemy.setEnemyName(enemyDetails.getEnemyName());
            enemy.setEnemyHealth(enemyDetails.getEnemyHealth());
            return enemyRepository.save(enemy);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEnemy(@PathVariable Long id) {
        enemyRepository.deleteById(id);
    }

    private String generateRandomEnemyName() {
        Random random = new Random();
        return ENEMY_NAMES[random.nextInt(ENEMY_NAMES.length)];
    }
}
