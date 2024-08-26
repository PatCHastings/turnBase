package com.javaproject.turnbase.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Class {

    @Id
    @Column(unique = true, nullable = false)
    private String index;
    private int baseArmorClass;
    private String name;

    @JsonProperty("hit_die")
    private int hitDie;

    @ElementCollection
    @CollectionTable(name = "class_proficiency_choices", joinColumns = @JoinColumn(name = "class_index"))
    private List<Proficiency> proficiencyChoices;

    @ElementCollection
    @CollectionTable(name = "class_proficiencies", joinColumns = @JoinColumn(name = "class_index"))
    private List<Proficiency> proficiencies;

    @ElementCollection
    @CollectionTable(name = "class_starting_equipment", joinColumns = @JoinColumn(name = "class_index"))
    private List<StartingEquipment> startingEquipment;

    @ElementCollection
    @CollectionTable(name = "class_subclasses", joinColumns = @JoinColumn(name = "class_index"))
    private List<Reference> subclasses;

    @OneToMany(mappedBy = "characterClass")
    @JsonManagedReference
    private List<Player> players;

    // Getters and setters


    public int getBaseArmorClass() {
        return baseArmorClass;
    }

    public void setBaseArmorClass(int baseArmorClass) {
        this.baseArmorClass = baseArmorClass;
    }

    public List<Proficiency> getProficiencyChoices() {
        return proficiencyChoices;
    }

    public void setProficiencyChoices(List<Proficiency> proficiencyChoices) {
        this.proficiencyChoices = proficiencyChoices;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitDie() {
        return hitDie;
    }

    public void setHitDie(int hitDie) {
        this.hitDie = hitDie;
    }

    public List<Proficiency> getProficiencies() {
        return proficiencies;
    }

    public void setProficiencies(List<Proficiency> proficiencies) {
        this.proficiencies = proficiencies;
    }

    public List<StartingEquipment> getStartingEquipment() {
        return startingEquipment;
    }

    public void setStartingEquipment(List<StartingEquipment> startingEquipment) {
        this.startingEquipment = startingEquipment;
    }

    public List<Reference> getSubclasses() {
        return subclasses;
    }

    public void setSubclasses(List<Reference> subclasses) {
        this.subclasses = subclasses;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Embeddable
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Proficiency {
        private String index;
        private String name;

        // Getters and Setters
        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Embeddable
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StartingEquipment {
        @Embedded
        private Equipment equipment;
        private int quantity;

        // Getters and Setters
        public Equipment getEquipment() {
            return equipment;
        }
        public void setEquipment(Equipment equipment) {
            this.equipment = equipment;
        }

        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    @Embeddable // for StartingEquipment
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Equipment {
        private String index;
        private String name;

        // Getters and setters
        public String getIndex() {
            return index;
        }
        public void setIndex(String index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Embeddable
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reference {
        private String index;
        private String name;

        // Getters and Setters
        public String getIndex() {
            return index;
        }
        public void setIndex(String index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
