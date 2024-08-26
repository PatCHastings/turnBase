package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.*;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CombatService {

    public String startCombat(GameCharacter player, GameCharacter enemy) {
        boolean playerGoesFirst = player.rollInitiative() >= enemy.rollInitiative();

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            if (playerGoesFirst) {
                CombatAction playerAction = getPlayerAction(); // This is where player input comes in
                playerAction.execute(player, enemy);
                if (enemy.getHealth() <= 0) break;

                executeEnemyTurn(player, enemy);
            } else {
                executeEnemyTurn(player, enemy);
                if (player.getHealth() <= 0) break;

                CombatAction playerAction = getPlayerAction(); // This is where player input comes in
                playerAction.execute(player, enemy);
            }
            playerGoesFirst = !playerGoesFirst; // Switch turns
        }

        return determineWinner(player, enemy);
    }

    private CombatAction getPlayerAction() {
        // For now, return an AttackAction or any other action based on player input
        // This can be extended to include different types of actions (skills, items, etc.)
        return new AttackAction(); // Placeholder, replace with actual player input
    }

    private void executeEnemyTurn(GameCharacter player, GameCharacter enemy) {
        // Basic enemy logic for now, could be expanded to be more complex
        CombatAction enemyAction = new AttackAction(); // Placeholder for now
        enemyAction.execute(enemy, player);
    }

    private String determineWinner(GameCharacter player, GameCharacter enemy) {
        if (player.getHealth() <= 0) {
            return enemy.getName() + " wins!";
        } else if (enemy.getHealth() <= 0) {
            return player.getName() + " wins!";
        } else {
            return "Combat ends in a draw!";
        }
    }
}



