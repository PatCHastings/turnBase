package com.javaproject.turnbase.util;

import com.javaproject.turnbase.entity.Class;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AbilityScoreGenerator {

    public static int roll4d6DropLowest() {
        Random random = new Random();
        int[] rolls = new int[4];

        for (int i = 0; i < 4; i++) {
            rolls[i] = random.nextInt(6) + 1;
        }

        Arrays.sort(rolls);
        return rolls[1] + rolls[2] + rolls[3];
    }

    public static int[] generateRandomAbilityScores() {
        int[] scores = new int[6];
        for (int i = 0; i < 6; i++) {
            scores[i] = roll4d6DropLowest();
        }
        return scores;
    }

    public static int calculateModifier(int score) {
        return (score - 10) / 2;
    }

    public static int calculateStartingHP(Class characterClass, int level, int constitutionModifier) {
        int hitDie = characterClass.getHitDie();
        int startingHP = hitDie + constitutionModifier;

        for (int i = 2; i <= level; i++) {
            startingHP += (hitDie / 2) + 1 + constitutionModifier;
        }

        return startingHP;
    }

    // generate all 6 ability scores
    public static Map<String, Integer> generateAbilityScores() {
        Map<String, Integer> abilityScores = new HashMap<>();
        abilityScores.put("Strength", roll4d6DropLowest());
        abilityScores.put("Dexterity", roll4d6DropLowest());
        abilityScores.put("Constitution", roll4d6DropLowest());
        abilityScores.put("Intelligence", roll4d6DropLowest());
        abilityScores.put("Wisdom", roll4d6DropLowest());
        abilityScores.put("Charisma", roll4d6DropLowest());

        return abilityScores;
    }
}