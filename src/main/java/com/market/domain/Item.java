package com.market.domain;

public class Item {
    private final Long id;
    private final String description;
    private double cost;

    public Item(Long id, String description, double cost) {
        this.id = id;
        this.description = description;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
