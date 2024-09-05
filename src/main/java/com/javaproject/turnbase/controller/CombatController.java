package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.AttackAction;
import com.javaproject.turnbase.entity.CombatAction;
import com.javaproject.turnbase.entity.CombatActionResult;
import com.javaproject.turnbase.entity.GameCharacter;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import com.javaproject.turnbase.service.CombatResult;
import com.javaproject.turnbase.service.CombatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/combat")
@CrossOrigin(origins = "http://localhost:5174")
public class CombatController {
    @Autowired
    private CombatService combatService;
    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    @PostMapping("/start")
    public ResponseEntity<CombatResult> startCombat(@RequestParam Long playerId, @RequestParam Long enemyId) {
        try {
            CombatResult result = combatService.startCombat(playerId, enemyId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Log the error with a stack trace for debugging
            logger.severe("Error starting combat with playerId: " + playerId + " and enemyId: " + enemyId);
            e.printStackTrace();

            // Create an error-specific CombatActionResult
            CombatActionResult errorResult = new CombatActionResult(null, null, 0, "Error starting combat: " + e.getMessage());

            // Return a CombatResult with the error-specific CombatActionResult
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CombatResult(0, 0, List.of(errorResult), false));
        }
    }

    @PostMapping("/action")
    public ResponseEntity<CombatResult> performAction(
            @RequestParam Long playerId,
            @RequestParam Long enemyId,
            @RequestParam String actionType,
            @RequestParam(required = false) Long targetId) {

        CombatResult result = combatService.performAction(playerId, enemyId, actionType);
        return ResponseEntity.ok(result);
    }
}