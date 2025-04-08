package com.exemple.market;


import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.TableId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MarketSimulator {

    private List<Asset> assets;
    private final BigQuery bigQuery = BigQueryConfig.getClient();
    private final TableId tableId;

    public MarketSimulator(String projectId, String datasetId) {
        assets = new ArrayList<>();
        assets.add(new Asset("HSBC", 732));
        assets.add(new Asset("BTC", 40000));
        assets.add(new Asset("ETC", 2500));
        assets.add(new Asset("APPL", 181));

        this.tableId = TableId.of(projectId, datasetId, "prices");
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

    public void saveMarketToBigQuery() {
        for (Asset asset : assets) {
            MarketData data = new MarketData(asset.getName(), asset.getPrice(), Instant.now());
            Map<String, Object> rowContent = data.toMap();
            InsertAllRequest.RowToInsert row = InsertAllRequest.RowToInsert.of(UUID.randomUUID().toString(), rowContent);

            InsertAllResponse response = bigQuery.insertAll(
                    InsertAllRequest.newBuilder(tableId).addRow(row).build()
            );

            if (response.hasErrors()) {
                System.out.println("Erreur lors de l'insertion dans BigQuery :");
                response.getInsertErrors().forEach((k, v) -> System.out.println(v));
            } else {
                System.out.printf("Donnée enregistrée pour %s : %.2f à %s%n", asset.getName(), asset.getPrice(), Instant.now());
            }
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
        saveMarketToBigQuery();
        while (true) {
            // Enregistre toutes les 5 minutes dans BigQuery
            if (System.currentTimeMillis() - startTime >= 300000) {
                saveMarketToBigQuery();
                startTime = System.currentTimeMillis();
            }
            try {
                Thread.sleep(1_000); // délai pour ne pas surcharger le processeur
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