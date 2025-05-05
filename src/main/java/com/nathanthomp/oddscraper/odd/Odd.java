package com.nathanthomp.oddscraper.odd;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/*
 * Examples:
 * 
 * OUTCOME#SPREAD#1,     ODD#-120, ODD, DraftKings, -120
 * OUTCOME#SPREAD#1,     ODD#-122, ODD, FanDuel,    -122
 * OUTCOME#SPREAD#1,     ODD#-125, ODD, ESPN Bet,   -125
 * OUTCOME#SPREAD#1,     ODD#-130, ODD, Bet 365,    -130
 * OUTCOME#SPREAD#2,     ODD#250,  ODD, DraftKings, -130
 * OUTCOME#SPREAD#2,     ODD#245,  ODD, FanDuel,    -130
 */
public class Odd extends Entity {
    private Outcome outcome;
    private String sportsbook;
    private int price;

    private String link;

    public Odd(Outcome outcome, String sportsbook, int price) {
        this.outcome = outcome;
        this.sportsbook = sportsbook;
        this.price = price;
    }

    @Override
    public String getPartitionKey() {
        return this.outcome.getSortKey();
    }

    @Override
    public String getSortKey() {
        return "ODD#" + this.sportsbook.toUpperCase();
    }

    @Override
    protected String getType() {
        return "ODD";
    }

    @Override
    public Map<String, AttributeValue> getAttributes() {
        Map<String, AttributeValue> attributeMap = new HashMap<String, AttributeValue>();
        attributeMap.put("sportsbook", AttributeValue.builder().s(this.sportsbook).build());
        attributeMap.put("price", AttributeValue.builder().n(this.price + "").build());
        return attributeMap;
    }

    @Override
    protected String getTimeToLive() {
        /*
         * This needs to be when the event is over
         */
        long offset = 86400; // 24 hours
        return Long.toString(Instant.now().getEpochSecond() + offset);
    }

    @Override
    public int hashCode() {
        return this.outcome.hashCode() * this.price * this.sportsbook.hashCode();
    }
}
