package com.javaproject.turnbase.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Equipment {

    private String name;
    private int baseArmorClass;
    private boolean dexBonus;
    private int maxBonus;

}
