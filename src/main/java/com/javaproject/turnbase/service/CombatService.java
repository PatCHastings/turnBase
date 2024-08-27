package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.*;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CombatService {

    public CombatResult startCombat(GameCharacter player, GameCharacter enemy) {
        boolean playerGoesFirst = player.rollInitiative() >= enemy.rollInitiative();
        List<String> combatLog = new ArrayList<>();

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            if (playerGoesFirst) {
                combatLog.addAll(executePlayerTurn(player, enemy));
                if (enemy.getHealth() <= 0) break;

                combatLog.addAll(executeEnemyTurn(player, enemy));
            } else {
                combatLog.addAll(executeEnemyTurn(player, enemy));
                if (player.getHealth() <= 0) break;

                combatLog.addAll(executePlayerTurn(player, enemy));
            }
            playerGoesFirst = !playerGoesFirst; // Switch turns
        }

        String winner = determineWinner(player, enemy);
        combatLog.add(winner);

        return new CombatResult(player.getHealth(), enemy.getHealth(), combatLog);
    }

    private List<String> executePlayerTurn(GameCharacter player, GameCharacter enemy) {
        List<String> log = new ArrayList<>();
        CombatAction playerAction = getPlayerAction(); // Get player's action
        log.add(playerAction.execute(player, enemy));
        return log;
    }

    private List<String> executeEnemyTurn(GameCharacter player, GameCharacter enemy) {
        List<String> log = new ArrayList<>();
        CombatAction enemyAction = new AttackAction(); // Example action
        log.add(enemyAction.execute(enemy, player));
        return log;
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

    // Define the getPlayerAction method
    private CombatAction getPlayerAction() {
        // For now, return a simple attack action
        return new AttackAction(); // Replace this with actual logic for selecting player's action
    }
}





