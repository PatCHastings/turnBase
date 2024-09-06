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
    private PlayerExperienceService playerExperienceService;

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    @Autowired
    public CombatService(EnemyRepository enemyRepository, PlayerRepository playerRepository, PlayerExperienceService playerExperienceService) {
        this.enemyRepository = enemyRepository;
        this.playerRepository = playerRepository;
        this.attackAction = new AttackAction(enemyRepository, playerRepository);
        this.playerExperienceService = playerExperienceService;
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

        logger.info("Checking if combat ended...");
        if (checkCombatEnd()) {
            logger.info("Combat has ended. Resolving combat.");
            combatOngoing = false;
            return resolveCombat(combatLog);
        }

        advanceTurn();

        boolean isPlayerTurn = combatants.get(currentTurnIndex) instanceof Player;
        Player player = getCurrentPlayer();

        logger.info("Processing next turn. Player turn: " + isPlayerTurn);
        return new CombatResult(
                getCurrentPlayer().getHealth(),
                getCurrentEnemy().getHealth(),
                combatLog,
                isPlayerTurn,
                player.getExperience(),
                player.getLevel()
        );
    }

    public CombatResult performAction(Long playerId, Long enemyId, String actionType) {
        GameCharacter player = playerRepository.findById(playerId).orElseThrow();
        GameCharacter enemy = enemyRepository.findById(enemyId).orElseThrow();
        List<CombatActionResult> combatLog = new ArrayList<>();

        if (!(player instanceof Player)) {
            throw new IllegalArgumentException("The provided playerId does not refer to a Player.");
        }
        Player actualPlayer = (Player) player;

        switch (actionType.toLowerCase()) {
            case "attack":
                combatLog.add(performAttack(player, enemy, combatLog));  // Pass combatLog here
                break;
            case "defend":
                // Add defend logic
                break;
            case "use_item":
                // Add use item logic
                break;
            case "use_skill":
                // Add use skill logic
                break;
            case "enemyaction":
                combatLog.addAll(executeEnemyTurn((Enemy) enemy));
                break;
            default:
                throw new IllegalArgumentException("Unknown action type: " + actionType);
        }

        // Log the health after the action
        logger.info("Player Health after action: " + player.getHealth());
        logger.info("Enemy Health after action: " + enemy.getHealth());

        // Check if combat has ended after the action
        if (checkCombatEnd()) {
            return resolveCombat(combatLog);  // Resolving the combat if it has ended
        }

        advanceTurn();
        boolean nextTurnIsPlayer = actionType.equalsIgnoreCase("enemyaction");
        Player currentPlayer = getCurrentPlayer(); // Use getCurrentPlayer()

        return new CombatResult(currentPlayer.getHealth(), enemy.getHealth(), combatLog, nextTurnIsPlayer, currentPlayer.getExperience(), actualPlayer.getLevel());
    }

    private void updateCombatantHealth(GameCharacter combatant) {
        for (int i = 0; i < combatants.size(); i++) {
            GameCharacter existingCombatant = combatants.get(i);

            // Check if the combatant is a Player or an Enemy and compare the IDs
            if ((existingCombatant instanceof Player && combatant instanceof Player &&
                    ((Player) existingCombatant).getId().equals(((Player) combatant).getId())) ||
                    (existingCombatant instanceof Enemy && combatant instanceof Enemy &&
                            ((Enemy) existingCombatant).getId().equals(((Enemy) combatant).getId()))) {
                // Replace the combatant in the list with the updated one
                combatants.set(i, combatant);
                break;
            }
        }
    }

    CombatActionResult performAttack(GameCharacter attacker, GameCharacter defender, List<CombatActionResult> combatLog) {
        int attackRoll = attackAction.rollDice(20) + attacker.getStrengthModifier();
        int defenderArmorClass = getArmorClass(defender);

        Long attackerId = null;
        Long defenderId = null;

        // Get attacker and defender IDs
        if (attacker instanceof Player) {
            attackerId = ((Player) attacker).getId();
        } else if (attacker instanceof Enemy) {
            attackerId = ((Enemy) attacker).getId();
        }

        if (defender instanceof Player) {
            defenderId = ((Player) defender).getId();
        } else if (defender instanceof Enemy) {
            defenderId = ((Enemy) defender).getId();
        }

        if (attackRoll >= defenderArmorClass) {
            int damage = attackAction.calculateDamage(attacker);
            defender.setHealth(Math.max(defender.getHealth() - damage, 0));

            // Ensure health changes are reflected in the combatants list
            updateCombatantHealth(defender);

            // Check if defender is an enemy and update accordingly
            if (defender instanceof Enemy) {
                Enemy enemy = (Enemy) defender;

                // Ensure enemy's health does not go below 0
                if (enemy.getHealth() <= 0) {
                    enemy.setHealth(0);
                    enemyRepository.save(enemy);  // Persist the update

                    // Log enemy defeat and end combat if necessary
                    logger.info("Enemy defeated: " + enemy.getName() + " has 0 health.");

                    // Add defeat result to combat log
                    combatLog.add(new CombatActionResult(attackerId, defenderId, damage, attacker.getName() + " has defeated " + defender.getName() + "!"));

                    return new CombatActionResult(
                            attackerId,
                            defenderId,
                            damage,
                            attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage!"
                    );
                }
            }

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
            CombatActionResult attackResult = performAttack(enemy, player, combatLog);
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
        // Log all combatants and their health
        logger.info("Logging all combatants' health:");

        combatants.forEach(c -> {
            String type = c instanceof Player ? "Player" : "Enemy";
            logger.info(type + " health: " + c.getHealth());
        });

        // Check if all players are defeated (health <= 0)
        boolean allPlayersDefeated = combatants.stream()
                .filter(c -> c instanceof Player)
                .allMatch(c -> {
                    logger.info("Checking player health: " + c.getHealth());
                    return c.getHealth() <= 0;
                });

        // Check if all enemies are defeated (health <= 0)
        boolean allEnemiesDefeated = combatants.stream()
                .filter(c -> c instanceof Enemy)
                .allMatch(c -> {
                    logger.info("Checking enemy health: " + c.getHealth());
                    return c.getHealth() <= 0;
                });

        logger.info("All players defeated: " + allPlayersDefeated);
        logger.info("All enemies defeated: " + allEnemiesDefeated);

        // Combat ends if all players or all enemies are defeated
        return allPlayersDefeated || allEnemiesDefeated;
    }

    private CombatResult resolveCombat(List<CombatActionResult> combatLog) {
        Player player = getCurrentPlayer();
        Enemy enemy = getCurrentEnemy();

        logger.info("Player health: " + player.getHealth());
        logger.info("Enemy health: " + enemy.getHealth());

        boolean playerWins = combatants.stream()
                .anyMatch(c -> c instanceof Enemy && c.getHealth() <= 0);

        boolean enemyWins = combatants.stream()
                .anyMatch(c -> c instanceof Player && c.getHealth() <= 0);

        String result;
        if (playerWins) {
            result = "Player wins!";
            playerExperienceService.grantExperience(player, enemy);  // Grant experience if player wins
            logger.info("exp gained: " + player.getExperience());
        } else if (enemyWins) {
            result = "Enemy wins!";
        } else {
            result = "Ongoing";
        }

        combatLog.add(new CombatActionResult(null, null, 0, result));
        logger.info("Combat result: " + result);

        return new CombatResult(
                player.getHealth(),
                enemy.getHealth(),
                combatLog,
                false,
                player.getExperience(),// Combat has ended
                player.getLevel() // Pass player's experience here
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

