package com.exemple.market;

public class Main {
    public static void main(String[] args) {
        System.out.println("Simulation started");
        MarketSimulator simulator = new MarketSimulator("fair-abbey-456116-j2", "market_data");
        simulator.run();
    }
}