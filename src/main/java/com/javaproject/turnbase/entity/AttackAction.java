package com.javaproject.turnbase.entity;

public class AttackAction implements CombatAction {
    @Override
    public void execute(GameCharacter attacker, GameCharacter defender) {
        int attackRoll = rollDice(20) + attacker.getStrengthModifier();
        int defenderArmorClass = getArmorClass(defender);

        if (attackRoll >= defenderArmorClass) {
            int damage = calculateDamage(attacker, defender);
            defender.setHealth(defender.getHealth() - damage);
            System.out.println(attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage!");
        } else {
            System.out.println(attacker.getName() + " misses " + defender.getName() + "!");
        }
    }

    private int rollDice(int sides) {
        return (int) (Math.random() * sides + 1);
    }

    private int calculateDamage(GameCharacter attacker, GameCharacter defender) {
        int damage = rollDice(attacker.getStrengthModifier());
        // Consider resistances, vulnerabilities, etc.
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
