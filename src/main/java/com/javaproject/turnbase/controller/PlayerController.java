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
        }
        return playerRepository.save(player);
    }

    @PutMapping("/id/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null && player.getCharacterImage() != null) {
            player.setName(playerDetails.getName());
            player.setHealth(playerDetails.getHealth());
            player.setCharacterClass(playerDetails.getCharacterClass());
            player.setCharacterImage(playerDetails.getCharacterImage());
            return playerRepository.save(player);

        } else {
            return null;
        }
    }

    private void updatePlayerHealth(Player player) {
        Class characterClass = player.getCharacterClass();
        if (characterClass != null) {
            int constitutionModifier = player.getConstitutionModifier();
            int startingHP = AbilityScoreGenerator.calculateStartingHP(characterClass, player.getLevel(), constitutionModifier);
            player.setHealth(startingHP);
        }
    }

    @PutMapping("/id/{id}/update-scores")
    public Player updatePlayerAbilityScores(@PathVariable Long id, @RequestBody Map<String, Integer> abilityScores) {
        Player existingPlayer = playerRepository.findById(id).orElse(null);
        if (existingPlayer == null) {
            throw new NullPointerException("Player not found with id: " + id);
        }

        // Update only the ability scores and related fields
        int strength = abilityScores.getOrDefault("strength", existingPlayer.getStrength());
        int dexterity = abilityScores.getOrDefault("dexterity", existingPlayer.getDexterity());
        int constitution = abilityScores.getOrDefault("constitution", existingPlayer.getConstitution());
        int intelligence = abilityScores.getOrDefault("intelligence", existingPlayer.getIntelligence());
        int wisdom = abilityScores.getOrDefault("wisdom", existingPlayer.getWisdom());
        int charisma = abilityScores.getOrDefault("charisma", existingPlayer.getCharisma());

        existingPlayer.setStrength(strength);
        existingPlayer.setStrengthModifier(AbilityScoreGenerator.calculateModifier(strength));

        existingPlayer.setDexterity(dexterity);
        existingPlayer.setDexterityModifier(AbilityScoreGenerator.calculateModifier(dexterity));

        existingPlayer.setConstitution(constitution);
        int constitutionModifier = AbilityScoreGenerator.calculateModifier(constitution);
        existingPlayer.setConstitutionModifier(constitutionModifier);

        existingPlayer.setIntelligence(intelligence);
        existingPlayer.setIntelligenceModifier(AbilityScoreGenerator.calculateModifier(intelligence));

        existingPlayer.setWisdom(wisdom);
        existingPlayer.setWisdomModifier(AbilityScoreGenerator.calculateModifier(wisdom));

        existingPlayer.setCharisma(charisma);
        existingPlayer.setCharismaModifier(AbilityScoreGenerator.calculateModifier(charisma));

        // Update player's health based on the new constitution
        updatePlayerHealth(existingPlayer);

        // Save the updated player with all fields intact
        return playerRepository.save(existingPlayer);
    }

    @GetMapping("/generate")
    public int[] generateAbilityScores() {
        return AbilityScoreGenerator.generateRandomAbilityScores();
    }
}
