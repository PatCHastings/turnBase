package com.javaproject.turnbase.controller;

import com.javaproject.turnbase.entity.Player;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerId(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            player.setUserName(playerDetails.getUserName());
            player.setCharacterClass(playerDetails.getCharacterClass());
            player.setHealth(playerDetails.getHealth());
            return playerRepository.save(player);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }

}
