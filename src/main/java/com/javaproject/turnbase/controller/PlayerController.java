package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Class;
import com.javaproject.turnbase.entity.Player;
import com.javaproject.turnbase.repository.ClassRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import com.javaproject.turnbase.util.AbilityScoreGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:5174")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ClassRepository classRepository;

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        // Ensure the class is correctly set
        if (player.getCharacterClass() != null && player.getCharacterClass().getIndex() != null) {
            Class characterClass = classRepository.findById(player.getCharacterClass().getIndex()).orElse(null);
            player.setCharacterClass(characterClass);

            // Generate Constitution score and calculate modifier
            int constitution = AbilityScoreGenerator.roll4d6DropLowest();
            player.setConstitution(constitution);
            int constitutionModifier = AbilityScoreGenerator.calculateModifier(constitution);
            player.setConstitution(constitution);
            player.setConstitutionModifier(constitutionModifier);

            // Calculate starting HP
            int startingHP = AbilityScoreGenerator.calculateStartingHP(characterClass, player.getLevel(), constitutionModifier);
            player.setHealth(startingHP);
        }
        return playerRepository.save(player);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            player.setName(playerDetails.getName());
            player.setHealth(playerDetails.getHealth());
            player.setCharacterClass(playerDetails.getCharacterClass());
            return playerRepository.save(player);
        } else {
            return null;
        }
    }

}
