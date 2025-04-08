package com.exemple.market;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MarketSimulator {

    private List<Asset> assets;

    public MarketSimulator() {
        assets = new ArrayList<>();

        assets.add(new Asset("HSBC", 732));
        assets.add(new Asset("Bitcoin", 40000));
        assets.add(new Asset("Ethereum", 2500));
        assets.add(new Asset("Apple", 181));
    }

    public void updateMarket() {
        for (Asset asset : assets) {
            asset.updatePrice();
        }
    }

    public void printMarket() {
        System.out.println("\n---- Prix actuels ----");
        for (Asset asset : assets) {
            System.out.println(asset);
        }
    }

    public void saveMarketToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("market_prices.txt", true))) {
            writer.println("---- Enregistrement des Prix ----");
            for (Asset asset : assets) {
                writer.println(asset);
            }
            writer.println();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ecriture dans le fichier : " + e.getMessage());
        }
    }

    public void runMarketUpdater() {
        while (true) {
            updateMarket();
            printMarket();
            try {
                Thread.sleep(10_000); // pause de 10 sec
            } catch (InterruptedException e) {
                System.out.println("Thread mise a jour interrompu.");
                break;
            }
        }
    }

    public void runMarketSaver() {
        long startTime = System.currentTimeMillis();
        while (true) {
            // enregistre toutes les 5 min
            if (System.currentTimeMillis() - startTime >= 300000) {
                saveMarketToFile();
                startTime = System.currentTimeMillis();
            }
            try {
                Thread.sleep(1_000); // delai pour ne pas surcharger le processeur
            } catch (InterruptedException e) {
                System.out.println("Thread enregistrement interrompu.");
                break;
            }
        }
    }


    public void run() {
        Thread marketUpdaterThread = new Thread(() -> runMarketUpdater());
        Thread marketSaverThread = new Thread(() -> runMarketSaver());

        marketUpdaterThread.start();
        marketSaverThread.start();
    }
}