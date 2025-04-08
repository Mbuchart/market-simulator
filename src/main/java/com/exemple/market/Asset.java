package com.exemple.market;

import java.util.Random;

public class Asset {
    private String name;
    private double price;
    private static final Random random = new Random();

    public Asset(String name, double initialPrice) {
        this.name = name;
        this.price = initialPrice;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice() {
        //Simulate a random price variation (between -10% and +10%)
        double randomVariation = 1 + (random.nextDouble() * 0.1 - 0.05);
        price *= randomVariation;
    }

    @Override
    public String toString() {
        return name + " : " + String.format("%.2f", price) + "$";
    }
}