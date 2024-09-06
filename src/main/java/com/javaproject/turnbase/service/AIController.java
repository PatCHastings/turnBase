package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.CombatActionResult;
import com.javaproject.turnbase.entity.Enemy;
import com.javaproject.turnbase.entity.GameCharacter;
import com.javaproject.turnbase.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AIController {

    public static List<CombatActionResult> performAIAction(Enemy enemy, CombatService combatService) {
        List<CombatActionResult> combatLog = new ArrayList<>();
        Player target = combatService.getCurrentPlayer(); // Assuming the enemy targets the player

        if (target != null && target.getHealth() > 0) {
            // For now, the enemy just attacks
            CombatActionResult attackResult = combatService.performAttack(enemy, target, combatLog);
            combatLog.add(attackResult);
        }

        return combatLog;
    }
}