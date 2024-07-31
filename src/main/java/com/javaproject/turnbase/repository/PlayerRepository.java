package com.javaproject.turnbase.repository;

import com.javaproject.turnbase.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
