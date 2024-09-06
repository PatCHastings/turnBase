package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.entity.Player;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerExperienceService {

private final PlayerRepository playerRepository;

@Autowired
    public PlayerExperienceService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
}


public void grantExperience(Player player, Enemy enemy) {
    int baseExperience;
if (enemy.getChallengeRating() == 0) {
    baseExperience = 10;
    if (enemy.getHealth() > 10) {
        int extraHealth = enemy.getHealth() - 10;
        int bonusExperience = extraHealth / 2; // 1 exp for every extra 2 health above 10
        baseExperience += bonusExperience;
    }
} else {
    baseExperience = enemy.getChallengeRating() * 100;
}
    player.addExperience(baseExperience);
    checkAndHandleLevelUp(player);
    playerRepository.save(player);
}
    public void checkAndHandleLevelUp(Player player) {
        int experienceForNextLevel = getExperienceForNextLevel(player.getLevel());

        if (player.getExperience() >= experienceForNextLevel) {
            player.setLevel(player.getLevel() + 1);
            player.setExperience(player.getExperience() - experienceForNextLevel);
            System.out.println("Player leveled up! New level: " + player.getLevel());
        }
    }

    public int getExperienceForNextLevel(int currentLevel) {
        if (currentLevel == 0) {
            return 50;
        } else {
            return 100 * currentLevel;
        }
    }
}
