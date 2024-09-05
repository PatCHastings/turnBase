package com.javaproject.turnbase.service;

import com.javaproject.turnbase.controller.CombatController;
import com.javaproject.turnbase.entity.*;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.hibernate.Hibernate;
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
    private List<GameCharacter> combatants;
    private int currentTurnIndex;
    private final AttackAction attackAction;
    private boolean combatOngoing;

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    @Autowired
    public CombatService(EnemyRepository enemyRepository, PlayerRepository playerRepository) {
        this.enemyRepository = enemyRepository;
        this.playerRepository = playerRepository;
        this.attackAction = new AttackAction(enemyRepository, playerRepository);
    }


    public CombatResult startCombat(Long playerId, Long enemyId) {
        Player player = playerRepository.findById(playerId).orElseThrow();

        // Force initialization of the equipment collection
        Hibernate.initialize(player.getEquipment());

        Enemy enemy = enemyRepository.findById(enemyId).orElseThrow();

        this.combatants = initializeCombatants(player, enemy);
        this.currentTurnIndex = 0;
        this.combatOngoing = true;

        return processTurn();
    }

    private List<GameCharacter> initializeCombatants(Player player, Enemy enemy) {
        List<GameCharacter> combatants = new ArrayList<>();
        combatants.add(player);
        combatants.add(enemy);

        combatants.sort((c1, c2) -> Integer.compare(c2.rollInitiative(), c1.rollInitiative()));
        return combatants;
    }

    public CombatResult processTurn() {
        GameCharacter currentCombatant = combatants.get(currentTurnIndex);
        List<CombatActionResult> combatLog = new ArrayList<>();

        if (currentCombatant instanceof Player) {
            combatLog.add(new CombatActionResult(null, null, 0, "Waiting for player action..."));
        } else {
            combatLog.addAll(executeEnemyTurn((Enemy) currentCombatant));
        }

        if (checkCombatEnd()) {
            combatOngoing = false;
            return resolveCombat(combatLog);
        }

        advanceTurn();

        boolean isPlayerTurn = combatants.get(currentTurnIndex) instanceof Player;

        return new CombatResult(
                getCurrentPlayer().getHealth(),
                getCurrentEnemy().getHealth(),
                combatLog,
                isPlayerTurn
        );
    }

    public CombatResult performAction(Long playerId, Long enemyId, String actionType) {
        GameCharacter player = playerRepository.findById(playerId).orElseThrow();
        GameCharacter enemy = enemyRepository.findById(enemyId).orElseThrow();
        List<CombatActionResult> combatLog = new ArrayList<>();

        switch (actionType.toLowerCase()) {
            case "attack":
                combatLog.add(performAttack(player, enemy));
                break;
            case "defend":
                //combatLog.add(performDefend(player));
                break;
            case "use_item":
                //combatLog.add(useItem(player, enemy));
                break;
            case "use_skill":
                //combatLog.add(useSkill(player, enemy));
                break;
            case "enemyaction":
                // Add handling for enemy action
                combatLog.addAll(executeEnemyTurn((Enemy) enemy));
                break;
            default:
                throw new IllegalArgumentException("Unknown action type: " + actionType);
        }

        // Check if the combat ends after this action
        if (checkCombatEnd()) {
            return resolveCombat(combatLog);
        }

        // Determine the next turn
        boolean nextTurnIsPlayer = actionType.equalsIgnoreCase("enemyaction");
        advanceTurn();

        // Log the health before returning the result
        logger.info("Player Health after action: " + player.getHealth());
        logger.info("Enemy Health after action: " + enemy.getHealth());

        return new CombatResult(player.getHealth(), enemy.getHealth(), combatLog, nextTurnIsPlayer);
    }

    CombatActionResult performAttack(GameCharacter attacker, GameCharacter defender) {
        int attackRoll = attackAction.rollDice(20) + attacker.getStrengthModifier();
        int defenderArmorClass = getArmorClass(defender);

        Long attackerId = null;
        Long defenderId = null;

        // Get attacker ID
        if (attacker instanceof Player) {
            attackerId = ((Player) attacker).getId();
        } else if (attacker instanceof Enemy) {
            attackerId = ((Enemy) attacker).getId();
        }

        // Get defender ID
        if (defender instanceof Player) {
            defenderId = ((Player) defender).getId();
        } else if (defender instanceof Enemy) {
            defenderId = ((Enemy) defender).getId();
        }

        if (attackRoll >= defenderArmorClass) {
            int damage = attackAction.calculateDamage(attacker);
            defender.setHealth(Math.max(defender.getHealth() - damage, 0));
            saveCharacter(defender);

            return new CombatActionResult(
                    attackerId,
                    defenderId,
                    damage,
                    attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage!"
            );
        } else {
            return new CombatActionResult(
                    attackerId,
                    defenderId,
                    0,
                    attacker.getName() + " misses " + defender.getName() + "!"
            );
        }
    }

    private int getArmorClass(GameCharacter character) {
        if (character instanceof Player) {
            return ((Player) character).getArmorClass();
        } else if (character instanceof Enemy) {
            return ((Enemy) character).calculateArmorClass();
        }
        return 10; // Default value if the character type is unknown
    }

    private void saveCharacter(GameCharacter character) {
        if (character instanceof Enemy) {
            enemyRepository.save((Enemy) character);
        } else if (character instanceof Player) {
            playerRepository.save((Player) character);
        }
    }

    private List<CombatActionResult> executeEnemyTurn(Enemy enemy) {
        List<CombatActionResult> combatLog = new ArrayList<>();

        // For now, enemy only attacks player
        // future implementation: add AI logic to choose different actions.

        Player player = getCurrentPlayer();
        if (player != null && player.getHealth() > 0) {
            CombatActionResult attackResult = performAttack(enemy, player);
            combatLog.add(attackResult);
        }

        return combatLog;
    }

    // Implement performDefend, useItem, useSkill methods similarly

    private void advanceTurn() {
        do {
            currentTurnIndex = (currentTurnIndex + 1) % combatants.size();
        } while (combatants.get(currentTurnIndex).getHealth() <= 0);
    }

    private boolean checkCombatEnd() {
        boolean allPlayersDefeated = combatants.stream()
                .filter(c -> c instanceof Player)
                .allMatch(c -> c.getHealth() <= 0);

        boolean allEnemiesDefeated = combatants.stream()
                .filter(c -> c instanceof Enemy)
                .allMatch(c -> c.getHealth() <= 0);

        return allPlayersDefeated || allEnemiesDefeated;
    }

    private CombatResult resolveCombat(List<CombatActionResult> combatLog) {
        String result = combatants.stream()
                .anyMatch(c -> c instanceof Player && c.getHealth() > 0)
                ? "Player wins!"
                : "Enemy wins!";

        combatLog.add(new CombatActionResult(null, null, 0, result));
        return new CombatResult(
                getCurrentPlayer().getHealth(),
                getCurrentEnemy().getHealth(),
                combatLog,
                false
        );
    }

    Player getCurrentPlayer() {
        return (Player) combatants.stream()
                .filter(c -> c instanceof Player)
                .findFirst()
                .orElse(null);
    }

    Enemy getCurrentEnemy() {
        return (Enemy) combatants.stream()
                .filter(c -> c instanceof Enemy)
                .findFirst()
                .orElse(null);
    }

    // Roll dice, calculate damage, and saveCharacter methods would be implemented here
}

