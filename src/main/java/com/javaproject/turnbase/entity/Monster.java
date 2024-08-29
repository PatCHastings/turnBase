package com.javaproject.turnbase.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Monster extends Enemy {
    private String index; // Use 'index' as the primary key
    private String hitDice;
    private String url;

    // dynamic fields
    @ElementCollection
    private List<ArmorClass> armorClass;

    @Lob
    @ElementCollection
    private List<SpecialAbility> specialAbilities;

    @Override
    public List<SpecialAbility> getSpecialAbilities() {
        return specialAbilities;
    }
    @Override
    public void setSpecialAbilities(List<SpecialAbility> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }

    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }

    public String getHitDice() {
        return hitDice;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }

    public List<ArmorClass> getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(List<ArmorClass> armorClass) {
        this.armorClass = armorClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
