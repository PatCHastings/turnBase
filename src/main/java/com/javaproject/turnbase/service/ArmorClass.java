package com.javaproject.turnbase.service;

import jakarta.persistence.Embeddable;

@Embeddable
public class ArmorClass {
    private String type;
    private int value;

    // Constructors, getters, setters
    public ArmorClass() {}

    public ArmorClass(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
