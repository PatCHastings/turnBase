package com.javaproject.turnbase.repository;

import com.javaproject.turnbase.entity.Enemy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnemyRepository extends JpaRepository<Enemy, Long> {

    Optional<Enemy> findFirstByChallengeRating(int challengeRating);
}
