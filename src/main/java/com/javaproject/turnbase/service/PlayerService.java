package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.Player;
import com.javaproject.turnbase.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService extends AbstractCharacterService<Player>{
    @Autowired
    private PlayerRepository playerRepository;

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player getPlayer(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    protected JpaRepository<Player, Long> getRepository() {
        return playerRepository;
    }

}
