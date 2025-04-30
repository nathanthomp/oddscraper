package com.nathanthomp.oddscraper.odd;

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

    public Odd(Outcome outcome, String sportsbook, int price) {
        this.outcome = outcome;
        this.sportsbook = sportsbook;
        this.price = price;
    }

    @Override
    protected String getPartitionKey() {
        return this.outcome.getSortKey();
    }

    @Override
    protected String getSortKey() {
        return "ODD#" + this.price + "#" + this.hashCode();
    }

    @Override
    protected String getType() {
        return "ODD";
    }

    @Override
    public Map<String, AttributeValue> toItem() {
        Map<String, AttributeValue> item = super.itemKeysAndType();

        Map<String, AttributeValue> attributeMap = new HashMap<String, AttributeValue>();
        attributeMap.put("sportsbook", AttributeValue.builder().s(this.sportsbook).build());
        attributeMap.put("price", AttributeValue.builder().n(this.price + "").build());

        item.put("ATTRIBUTES", AttributeValue.builder().m(attributeMap).build());

        return item;
    }

    @Override
    public int hashCode() {
        return this.outcome.hashCode() * this.price * this.sportsbook.hashCode();
    }
}
