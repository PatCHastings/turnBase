package com.javaproject.turnbase.entity;

import com.javaproject.turnbase.controller.CombatController;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;

import java.util.logging.Logger;

public class AttackAction implements CombatAction {
    private final EnemyRepository enemyRepository;
    private final PlayerRepository playerRepository;

    public AttackAction(EnemyRepository enemyRepository, PlayerRepository playerRepository) {
        this.enemyRepository = enemyRepository;
        this.playerRepository = playerRepository;
    }

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    @Override
    public String execute(GameCharacter attacker, GameCharacter defender) {
        int attackRoll = rollDice(20) + attacker.getStrengthModifier();
        int defenderArmorClass = getArmorClass(defender);

        logger.info("attackRoll: " + attackRoll + " defenderArmorClass: " + defenderArmorClass);

        if (attackRoll >= defenderArmorClass) {
            int damage = calculateDamage(attacker, defender);
            int newHealth = defender.getHealth() - damage;
            if (newHealth < 0) {
                newHealth = 0; // Health should not drop below 0
            }
            defender.setHealth(newHealth); // Update the defender's health
            logger.info(attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage! health: " + newHealth);

            // Persist the updated health to the database
            if (defender instanceof Enemy) {
                enemyRepository.save((Enemy) defender);
            } else if (defender instanceof Player) {
                playerRepository.save((Player) defender);
            }

            return attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage!";
        } else {
            logger.info(attacker.getName() + " misses " + defender.getName() + "!");
            return attacker.getName() + " misses " + defender.getName() + "!";
        }
    }

    private int rollDice(int sides) {
        return (int) (Math.random() * sides + 1);
    }

    private int calculateDamage(GameCharacter attacker, GameCharacter defender) {
        int damage = rollDice(8) + attacker.getStrengthModifier();
        if (damage < 1) {
            damage = 1;
            // add resistances, vulnerabilities, etc. later
        }
        return damage;
    }

    private int getArmorClass(GameCharacter character) {
        if (character instanceof Player) {
            return ((Player) character).getArmorClass();
        } else if (character instanceof Enemy) {
            return ((Enemy) character).calculateArmorClass(); // Or whatever method you use for Enemy
        }
        return 10; // Default value if the character type is unknown
    }
}
