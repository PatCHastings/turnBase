package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.CombatActionResult;

import java.util.List;

public class CombatResult {
    private int playerHealth;
    private int enemyHealth;
    private List<CombatActionResult> log;
    private boolean playerTurn;


    public CombatResult(int playerHealth, int enemyHealth, List<CombatActionResult> log, boolean playerTurn) {
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
        this.log = log;
        this.playerTurn = playerTurn;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public List<CombatActionResult> getLog() {
        return log;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }
}

