package com.exemple.market;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import java.io.FileInputStream;
import java.io.IOException;

public class BigQueryConfig {

    private static BigQuery instance;

    private BigQueryConfig() {}

    public static BigQuery getClient() {
        if (instance == null) {
            synchronized (BigQueryConfig.class) {
                if (instance == null) {
                    try {
                        String credentialsPath = "/mnt/c/Users/mathe/IdeaProjects/market-simulator/key/fair-abbey-456116-j2-63383d2fa0f1.json";
                        FileInputStream credentialsStream = new FileInputStream(credentialsPath);

                        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

                        instance = BigQueryOptions.newBuilder()
                                .setCredentials(credentials)
                                .setProjectId("fair-abbey-456116-j2")
                                .build()
                                .getService();
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la configuration des identifiants : " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }


}