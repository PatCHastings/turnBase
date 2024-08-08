package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.entity.Monster;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.MonsterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnemyService {

    @Autowired
    private EnemyRepository enemyRepository;

    @Autowired
    private MonsterRepository monsterRepository;

    public Enemy createEnemyFromMonster(String monsterIndex) {
        Optional<Monster> optionalMonster = monsterRepository.findByIndex(monsterIndex);
        if (optionalMonster.isPresent()) {
            Monster monster = optionalMonster.get();
            Enemy enemy = new Enemy();
            enemy.setEnemyName(monster.getName());
            enemy.setEnemyHealth(monster.getHitPoints());
            enemy.setEnemyType(monster.getType());
            enemy.setSize(monster.getSize());
            enemy.setType(monster.getType());
            enemy.setAlignment(monster.getAlignment());
            enemy.setArmorClass(monster.getArmorClass());
            enemy.setSpeed(monster.getSpeed());
            enemy.setStrength(monster.getStrength());
            enemy.setDexterity(monster.getDexterity());
            enemy.setConstitution(monster.getConstitution());
            enemy.setIntelligence(monster.getIntelligence());
            enemy.setWisdom(monster.getWisdom());
            enemy.setCharisma(monster.getCharisma());
            enemy.setChallengeRating(monster.getChallengeRating());
            enemy.setSpecialAbilities(monster.getSpecialAbilities());
            enemy.setActions(monster.getActions());

            return enemyRepository.save(enemy);
        }
        throw new IllegalArgumentException("Monster not found with index: " + monsterIndex);
    }
}
