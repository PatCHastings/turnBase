package com.javaproject.turnbase.controller;

//import com.javaproject.turnbase.entity.AttackAction;
import com.javaproject.turnbase.entity.AttackAction;
import com.javaproject.turnbase.entity.CombatAction;
import com.javaproject.turnbase.entity.GameCharacter;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import com.javaproject.turnbase.service.CombatResult;
import com.javaproject.turnbase.service.CombatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/combat")
@CrossOrigin(origins = "http://localhost:5174")
public class CombatController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EnemyRepository enemyRepository;
    @Autowired
    private CombatService combatService;

    private static final Logger logger = Logger.getLogger(CombatController.class.getName());

    @PostMapping("/start")
    public ResponseEntity<CombatResult> startCombat(@RequestParam Long playerId, @RequestParam Long enemyId) {
        GameCharacter player = playerRepository.findById(playerId).orElseThrow();
        GameCharacter enemy = enemyRepository.findById(enemyId).orElseThrow();

        CombatResult result = combatService.startCombat(player, enemy);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/action")
    public ResponseEntity<CombatResult> performAction(@RequestParam Long playerId, @RequestParam Long enemyId, @RequestParam String actionType) {
        logger.info("Performing action: " + actionType + " against enemy: " + enemyId);
        GameCharacter player = playerRepository.findById(playerId).orElseThrow();
        GameCharacter enemy = enemyRepository.findById(enemyId).orElseThrow();

        CombatAction action;
        String combatLogEntry;

        switch (actionType.toLowerCase()) {
            case "attack":
                action = new AttackAction(enemyRepository, playerRepository);
                combatLogEntry = action.execute(player, enemy); // Assuming execute returns a string log entry
                break;
            case "skill":
                // action = new SkillAction();
                throw new UnsupportedOperationException("Skill action not yet implemented");
            case "item":
                // action = new UseItemAction();
                throw new UnsupportedOperationException("Item action not yet implemented");
            default:
                return ResponseEntity.badRequest().body(null);
        }

        // Create the CombatResult object with updated health and combat log entry
        List<String> combatLog = new ArrayList<>();
        combatLog.add(combatLogEntry);
        CombatResult result = new CombatResult(player.getHealth(), enemy.getHealth(), combatLog);

        return ResponseEntity.ok(result);
    }
}

