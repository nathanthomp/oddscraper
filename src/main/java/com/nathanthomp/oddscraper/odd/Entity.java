package com.nathanthomp.oddscraper.odd;

public abstract class Entity {
    public abstract String getPartitionKey();

    public abstract String getSortKey();

    public abstract String getType();

    // abstract Map<String, String> getAttributes();

    // abstract void toItem();

    // public Map<String, String> getKeys() {
    // return Map.ofEntries(
    // Map.entry("PK", this.getPk()),
    // Map.entry("SK", this.getSk()));
    // }

}
