package com.javaproject.turnbase.service;

import com.javaproject.turnbase.controller.CombatController;
import com.javaproject.turnbase.entity.*;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CombatService {

    private final EnemyRepository enemyRepository;
    private final PlayerRepository playerRepository;

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    @Autowired
    public CombatService(EnemyRepository enemyRepository, PlayerRepository playerRepository) {
        this.enemyRepository = enemyRepository;
        this.playerRepository = playerRepository;
    }

    public CombatResult startCombat(GameCharacter player, GameCharacter enemy) {
        // Initialize combatants and roll initiative
        List<Combatant> combatants = initializeCombatants(player, enemy);
        List<String> combatLog = new ArrayList<>();

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            for (Combatant combatant : combatants) {
                if (combatant.getCharacter().getHealth() > 0) {
                    if (combatant.isPlayer()) {
                        combatLog.addAll(executePlayerTurn(combatant.getCharacter(), enemy));
                    } else {
                        combatLog.addAll(executeEnemyTurn(player, combatant.getCharacter()));
                    }
                }
                if (player.getHealth() <= 0 || enemy.getHealth() <= 0) {
                    break;
                }
            }
        }

        String winner = determineWinner(player, enemy);
        combatLog.add(winner);

        return new CombatResult(player.getHealth(), enemy.getHealth(), combatLog);
    }

    private List<Combatant> initializeCombatants(GameCharacter player, GameCharacter enemy) {
        List<Combatant> combatants = new ArrayList<>();
        // Roll initiative and add both player and enemy to the combatant list
        combatants.add(new Combatant(player, player.rollInitiative(), true));
        combatants.add(new Combatant(enemy, enemy.rollInitiative(), false));

        // Sort combatants by initiative in descending order
        Collections.sort(combatants, (c1, c2) -> Integer.compare(c2.getInitiative(), c1.getInitiative()));

        return combatants;
    }

    private List<String> executePlayerTurn(GameCharacter player, GameCharacter enemy) {
        List<String> log = new ArrayList<>();
        CombatAction playerAction = getPlayerAction(); // Get player's action
        log.add(playerAction.execute(player, enemy));
        return log;
    }

    private List<String> executeEnemyTurn(GameCharacter player, GameCharacter enemy) {
        List<String> log = new ArrayList<>();
        AttackAction attackAction = new AttackAction(enemyRepository, playerRepository);
        String result = attackAction.execute(enemy, player);
        log.add(result);
        logger.info(result);
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
        return new AttackAction(enemyRepository, playerRepository); // Replace this with actual logic for selecting player's action
    }

    private static class Combatant {
        private final GameCharacter character;
        private final int initiative;
        private final boolean isPlayer;

        public Combatant(GameCharacter character, int initiative, boolean isPlayer) {
            this.character = character;
            this.initiative = initiative;
            this.isPlayer = isPlayer;
        }

        public GameCharacter getCharacter() {
            return character;
        }

        public int getInitiative() {
            return initiative;
        }

        public boolean isPlayer() {
            return isPlayer;
        }
    }
}
