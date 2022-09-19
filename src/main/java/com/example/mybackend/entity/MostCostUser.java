package com.example.mybackend.entity;

public class MostCostUser {
    private String name;
    private Integer cost;
    public MostCostUser(String name, Integer cost) {
        this.cost = cost;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
