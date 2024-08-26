package com.javaproject.turnbase.entity;

public interface CombatAction {
    void execute(GameCharacter attacker, GameCharacter defender);
}

