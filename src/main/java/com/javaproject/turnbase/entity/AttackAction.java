package com.javaproject.turnbase.entity;

import com.javaproject.turnbase.controller.CombatController;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttackAction implements CombatAction {
    private final EnemyRepository enemyRepository;
    private final PlayerRepository playerRepository;

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    public AttackAction(EnemyRepository enemyRepository, PlayerRepository playerRepository) {
        this.enemyRepository = enemyRepository;
        this.playerRepository = playerRepository;
    }


    @Override
    public String execute(GameCharacter attacker, GameCharacter defender) {
        int attackRoll = rollDice(20) + attacker.getStrengthModifier();
        int defenderArmorClass = getArmorClass(defender);

        logger.info("attackRoll: " + attackRoll + " defenderArmorClass: " + defenderArmorClass);

        if (attackRoll >= defenderArmorClass) {
            int damage = calculateDamage(attacker);
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

    private int getAttackBonus(GameCharacter attacker) {
        if (attacker instanceof Enemy) {
            List<Action> actions = ((Enemy) attacker).getActions();
            if (actions != null && !actions.isEmpty()) {
                return actions.get(0).getAttackBonus();
            }
        }
        return attacker.getStrengthModifier();
    }

    // Calculate damage based on the enemy's damage dice
    private int calculateDamage(GameCharacter attacker) {
        if (attacker instanceof Enemy) {
            List<Action> actions = ((Enemy) attacker).getActions();
            if (actions != null && !actions.isEmpty()) {
                Action action = actions.get(0); // Use the first action for simplicity
                int damage = 0;
                for (Damage damageDetail : action.getDamage()) {
                    int[] parsedDice = parseDice(damageDetail.getDamageDice());
                    int rolledDamage = 0;
                    for (int i = 0; i < parsedDice[0]; i++) {
                        rolledDamage += rollDice(parsedDice[1]);
                    }
                    // debug-Log the rolled damage before any modifiers
                    logger.info("Rolled damage before bonus and modifiers: " + rolledDamage);

                    // Add the bonus from the dice string
                    rolledDamage += parsedDice[2];
                    logger.info("Rolled damage after adding bonus (" + parsedDice[2] + "): " + rolledDamage);

                    // Add the attacker's Strength modifier
                    rolledDamage += attacker.getStrengthModifier();
                    logger.info("Rolled damage after adding Strength modifier (" + attacker.getStrengthModifier() + "): " + rolledDamage);

                    logger.info("Damage dice: " + damageDetail.getDamageDice() +
                            ", Final calculated damage: " + rolledDamage);

                    damage += rolledDamage;
                }
                return damage;
            }
        }
        // Default dmg
        return rollDice(8) + attacker.getStrengthModifier();
    }

    private int getArmorClass(GameCharacter character) {
        if (character instanceof Player) {
            return ((Player) character).getArmorClass();
        } else if (character instanceof Enemy) {
            return ((Enemy) character).calculateArmorClass(); // Enemy calc is different than player
        }
        return 10; // Default value if the character type is unknown
    }

    // Parse the damage dice string
    private int[] parseDice(String dice) {
        if (dice == null || dice.isEmpty()) {
            throw new IllegalArgumentException("Invalid dice format: " + dice);
        }

        Matcher matcher = Pattern.compile("(\\d+)d(\\d+)(?:\\+(\\d+))?").matcher(dice);
        if (matcher.matches()) {
            int numDice = Integer.parseInt(matcher.group(1));
            int numSides = Integer.parseInt(matcher.group(2));
            int bonus = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

            logger.info("Parsed dice: " + numDice + "d" + numSides + "+" + bonus);

            return new int[]{numDice, numSides, bonus};
        } else {
            throw new IllegalArgumentException("Invalid dice format: " + dice);
        }
    }


}
