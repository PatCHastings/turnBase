package com.javaproject.turnbase.service;

import java.util.List;

public class CombatResult {
    private int playerHealth;
    private int enemyHealth;
    private List<String> log;

    public CombatResult(int playerHealth, int enemyHealth, List<String> log) {
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
        this.log = log;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public List<String> getLog() {
        return log;
    }
}

