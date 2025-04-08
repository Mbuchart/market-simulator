package com.exemple.market;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class MarketData {
    private final String assetName;
    private final float price;
    private final Instant timestamp;

    public MarketData(String assetName, float price, Instant timestamp) {
        this.assetName = assetName;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getAssetName() {
        return assetName;
    }

    public float getPrice() {
        return price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    // Méthode pour la conversion vers un format insérable dans BigQuery
    public Map<String, Object> toMap() {
        Map<String, Object> row = new HashMap<>();
        row.put("symbol", assetName);
        row.put("price", price);
        row.put("timestamp", timestamp.toString()); // format ISO 8601
        return row;
    }
}