package com.javaproject.turnbase.controller;

//import com.javaproject.turnbase.entity.AttackAction;
import com.javaproject.turnbase.entity.CombatAction;
import com.javaproject.turnbase.entity.GameCharacter;
import com.javaproject.turnbase.repository.EnemyRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import com.javaproject.turnbase.service.CombatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/combat")
public class CombatController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EnemyRepository enemyRepository;
    @Autowired
    private CombatService combatService;

    @PostMapping("/start")
    public ResponseEntity<String> startCombat(@RequestParam Long playerId, @RequestParam Long enemyId) {
        GameCharacter player = playerRepository.findById(playerId).orElseThrow();
        GameCharacter enemy = enemyRepository.findById(enemyId).orElseThrow();

        String result = combatService.startCombat(player, enemy);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/action")
    public ResponseEntity<String> performAction(@RequestParam Long playerId, @RequestParam Long enemyId, @RequestParam String actionType) {
        GameCharacter player = playerRepository.findById(playerId).orElseThrow();
        GameCharacter enemy = enemyRepository.findById(enemyId).orElseThrow();

        CombatAction action;
        switch (actionType.toLowerCase()) {
            case "attack":
                //action = new AttackAction();
                break;
            case "skill":
                // action = new SkillAction();
                throw new UnsupportedOperationException("Skill action not yet implemented");
            case "item":
                // action = new UseItemAction();
                throw new UnsupportedOperationException("Item action not yet implemented");
            default:
                return ResponseEntity.badRequest().body("Invalid action type");
        }

        //action.execute(player, enemy);
        return ResponseEntity.ok("Action performed");
    }
}

