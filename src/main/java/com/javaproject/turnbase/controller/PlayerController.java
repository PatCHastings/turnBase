package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Class;
import com.javaproject.turnbase.entity.Player;
import com.javaproject.turnbase.repository.ClassRepository;
import com.javaproject.turnbase.repository.PlayerRepository;
import com.javaproject.turnbase.util.AbilityScoreGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/id/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @GetMapping("/check-name")
    public Map<String, Object> checkPlayerName(@RequestParam String name) {
        boolean exists = playerRepository.existsByName(name);
        long totalPlayers = playerRepository.count();
        return Map.of("exists", exists, "totalPlayers", totalPlayers);
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

    @PutMapping("/id/{id}")
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

    @PutMapping("/id/{id}/update-scores")
    public Player updatePlayerAbilityScores(@PathVariable Long id, @RequestBody Map<String, Integer> abilityScores) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            // Handle updates for ability scores other than Constitution
            int strength = abilityScores.getOrDefault("strength", 0) != null ? abilityScores.get("strength") : 0;
            int dexterity = abilityScores.getOrDefault("dexterity", 0) != null ? abilityScores.get("dexterity") : 0;
            int constitution = abilityScores.getOrDefault("constitution", 0) != null ? abilityScores.get("constitution") : 0;
            int intelligence = abilityScores.getOrDefault("intelligence", 0) != null ? abilityScores.get("intelligence") : 0;
            int wisdom = abilityScores.getOrDefault("wisdom", 0) != null ? abilityScores.get("wisdom") : 0;
            int charisma = abilityScores.getOrDefault("charisma", 0) != null ? abilityScores.get("charisma") : 0;

            // Set the player's ability scores n modifiers
            player.setStrength(strength);
            int strengthModifier = AbilityScoreGenerator.calculateModifier(strength);
            player.setStrengthModifier(strengthModifier);

            player.setDexterity(dexterity);
            int dexterityModifier = AbilityScoreGenerator.calculateModifier(dexterity);
            player.setDexterityModifier(dexterityModifier);

            player.setConstitution(constitution);
            int constitutionModifier = AbilityScoreGenerator.calculateModifier(constitution);
            player.setConstitutionModifier(constitutionModifier);

            player.setIntelligence(intelligence);
            int intelligenceModifier = AbilityScoreGenerator.calculateModifier(intelligence);
            player.setIntelligenceModifier(intelligenceModifier);

            player.setWisdom(wisdom);
            int wisdomModifier = AbilityScoreGenerator.calculateModifier(wisdom);
            player.setWisdomModifier(wisdomModifier);

            player.setCharisma(charisma);
            int charismaModifier = AbilityScoreGenerator.calculateModifier(charisma);
            player.setCharismaModifier(charismaModifier);

            // Save the updated player
            return playerRepository.save(player);
        } else {
            throw new NullPointerException("Player not found with id: " + id);
        }
    }

    @GetMapping("/generate")
    public int[] generateAbilityScores() {
        return AbilityScoreGenerator.generateRandomAbilityScores();
    }
}
