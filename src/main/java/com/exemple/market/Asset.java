package com.exemple.market;

import java.util.Random;

public class Asset {
    private String name;
    private float price;
    private static final Random random = new Random();

    public Asset(String name, float initialPrice) {
        this.name = name;
        this.price = initialPrice;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void updatePrice() {
        //Simulate a random price variation (between -5% and +5%)
        double randomVariation = 1 + (random.nextDouble() * 0.1 - 0.05);
        price *= (float) randomVariation;
    }

    @Override
    public String toString() {
        return name + " : " + String.format("%.2f", price) + "$";
    }
}