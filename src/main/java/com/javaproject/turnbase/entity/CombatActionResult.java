package com.javaproject.turnbase.entity;

public class CombatActionResult {
    private Long attackerId;
    private Long defenderId;
    private int damage;
    private String actionDescription;

    public CombatActionResult(Long attackerId, Long defenderId, int damage, String actionDescription) {
        this.attackerId = attackerId;
        this.defenderId = defenderId;
        this.damage = damage;
        this.actionDescription = actionDescription;
    }

    // Getters and setters

    public Long getAttackerId() {
        return attackerId;
    }

    public void setAttackerId(Long attackerId) {
        this.attackerId = attackerId;
    }

    public Long getDefenderId() {
        return defenderId;
    }

    public void setDefenderId(Long defenderId) {
        this.defenderId = defenderId;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }
}
