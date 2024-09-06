package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.CombatActionResult;

import java.util.List;

public class CombatResult {
    private int playerHealth;
    private int enemyHealth;
    private List<CombatActionResult> log;
    private boolean playerTurn;
    private int playerExperience;
    private int playerLevel;


    public CombatResult(int playerHealth, int enemyHealth, List<CombatActionResult> log, boolean playerTurn, int playerExperience, int playerlevel) {
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
        this.log = log;
        this.playerTurn = playerTurn;
        this.playerExperience = playerExperience;
        this.playerLevel = playerlevel;
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

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void setEnemyHealth(int enemyHealth) {
        this.enemyHealth = enemyHealth;
    }

    public void setLog(List<CombatActionResult> log) {
        this.log = log;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getPlayerExperience() {
        return playerExperience;
    }

    public void setPlayerExperience(int playerExperience) {
        this.playerExperience = playerExperience;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }
}

