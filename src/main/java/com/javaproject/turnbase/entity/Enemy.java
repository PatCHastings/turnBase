package com.javaproject.turnbase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
public class Enemy extends GameCharacter{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String enemyName;
    private int enemyHealth;
    private String enemyType;
    private String size;
    private String type;
    private String alignment;
    private String speed;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int challengeRating;

    // dynamic fields

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Action> actions;

    @ElementCollection
    @CollectionTable(name = "enemy_armor_class", joinColumns = @JoinColumn(name = "enemy_id"))
    private List<ArmorClass> armorClass;

    @Lob
    @ElementCollection
    private List<SpecialAbility> specialAbilities;


    // sub-classes

    @Embeddable
    public static class ArmorClass {
        private String type;
        private int armorValue;

        //Getters Setters

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getArmorValue() {
            return armorValue;
        }

        public void setArmorValue(int value) {
            this.armorValue = value;
        }

    }

    @Embeddable
    public static class SpecialAbility {
        private String name;
        @Lob
        private String desc;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public void setEnemyHealth(int enemyHealth) {
        this.enemyHealth = enemyHealth;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(String enemyType) {
        this.enemyType = enemyType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getChallengeRating() {
        return challengeRating;
    }

    public void setChallengeRating(int challengeRating) {
        this.challengeRating = challengeRating;
    }

    public List<SpecialAbility> getSpecialAbilities() {
        return specialAbilities;
    }

    public void setSpecialAbilities(List<SpecialAbility> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }

    public List<ArmorClass> getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(List<ArmorClass> armorClass) {
        this.armorClass = armorClass;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public int calculateArmorClass() {
        if (armorClass != null && !armorClass.isEmpty()) {
            return armorClass.get(0).getArmorValue(); // Use getArmorValue() as intended
        }
        return 10; // Default armor class if none is available
    }

}
