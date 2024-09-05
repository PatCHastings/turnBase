package com.javaproject.turnbase.repository;

import com.javaproject.turnbase.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    Optional<Monster> findByIndex(String index);

    List<Monster> findByChallengeRating(int rating);
}
