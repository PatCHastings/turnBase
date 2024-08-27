package com.javaproject.turnbase.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@Getter
@Setter
public abstract class GameCharacter {

    private String name;
    private int health;
    private int constitution;
    private int constitutionModifier;
    private int strength;
    private int strengthModifier;
    private int dexterity;
    private int dexterityModifier;
    private int intelligence;
    private int intelligenceModifier;
    private int wisdom;
    private int wisdomModifier;
    private int charisma;
    private int charismaModifier;

    //  shared methods
    public int rollInitiative() {
        int roll = (int) (Math.random() * 20 + 1);
        return roll + dexterityModifier;
    }

    // Getters and Setters

}
