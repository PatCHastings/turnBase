package com.javaproject.turnbase.entity;

public interface CombatAction {
    String execute(GameCharacter attacker, GameCharacter defender);
}

