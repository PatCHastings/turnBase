package com.javaproject.turnbase.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Enemy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String EnemyName;
    private int enemyHealth;

    public Enemy() {
    }

    public Enemy(Long id, String enemyName, int enemyHealth) {
        this.id = id;
        EnemyName = enemyName;
        this.enemyHealth = enemyHealth;
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEnemyName() {
        return EnemyName;
    }
    public void setEnemyName(String enemyName) {
        EnemyName = enemyName;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }
    public void setEnemyHealth(int enemyHealth) {
        this.enemyHealth = enemyHealth;
    }
}
