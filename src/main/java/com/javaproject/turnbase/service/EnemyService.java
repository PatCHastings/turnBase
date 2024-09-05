package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.Action;
import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.entity.Monster;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.MonsterRepository;
import com.javaproject.turnbase.util.AbilityScoreGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            enemy.setName(monster.getName());
            enemy.setHealth(monster.getHealth());
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
            // Deep copy actions list to avoid shared reference issue
            List<Action> actionsCopy = new ArrayList<>();
            for (Action action : monster.getActions()) {
                Action actionCopy = new Action();
                actionCopy.setName(action.getName());
                actionCopy.setDesc(action.getDesc());
                actionCopy.setAttackBonus(action.getAttackBonus());
                actionCopy.setCount(action.getCount());
                actionCopy.setDamage(new ArrayList<>(action.getDamage()));  // Deep copy damage list
                actionsCopy.add(actionCopy);
            }
            enemy.setActions(actionsCopy);

            // probably dumb to do it this way. fix later
            enemy.setStrengthModifier(AbilityScoreGenerator.calculateModifier(monster.getStrength()));
            enemy.setDexterityModifier(AbilityScoreGenerator.calculateModifier(monster.getDexterity()));
            enemy.setConstitutionModifier(AbilityScoreGenerator.calculateModifier(monster.getConstitution()));
            enemy.setIntelligenceModifier(AbilityScoreGenerator.calculateModifier(monster.getIntelligence()));
            enemy.setWisdomModifier(AbilityScoreGenerator.calculateModifier(monster.getWisdom()));
            enemy.setCharismaModifier(AbilityScoreGenerator.calculateModifier(monster.getCharisma()));

            return enemyRepository.save(enemy);
        }
        throw new IllegalArgumentException("Monster not found with index: " + monsterIndex);
    }

}
