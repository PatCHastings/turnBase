package com.javaproject.turnbase.util;

import com.javaproject.turnbase.entity.Class;

import java.util.Arrays;
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
}