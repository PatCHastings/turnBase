package com.javaproject.turnbase.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player extends GameCharacter{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int level;
    private String characterImage;


    @ManyToOne
    @JoinColumn(name = "class_index")
    @JsonBackReference
    private Class characterClass;

    @ElementCollection
    private List<Equipment> equipment = new ArrayList<>(); // List of equipped items

    public int getArmorClass() {
        int armorClass = 10; // Base armor class if no equipment

        for (Equipment item : equipment) {
            int itemArmorClass = item.getBaseArmorClass();
            if (item.isDexBonus()) {
                int dexBonus = Math.min(this.getDexterityModifier(), item.getMaxBonus());
                itemArmorClass += dexBonus;
            }
            armorClass = Math.max(armorClass, itemArmorClass); // Choose the best armor class from the equipped items
        }

        return armorClass;
    }

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public String getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(String characterImage) {
        this.characterImage = characterImage;
    }

    public Class getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(Class characterClass) {
        this.characterClass = characterClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }



}
