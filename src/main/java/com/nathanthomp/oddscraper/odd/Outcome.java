package com.nathanthomp.oddscraper.odd;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/*
 * Examples:
 * 
 * EVENT#DD/MM/YYYY@HH:MM:SS#1, OUTCOME#SPREAD#1,      OUTCOME, SPREAD,      Under, 4.5,  NULL,          NULL
 * EVENT#DD/MM/YYYY@HH:MM:SS#1, OUTCOME#MONEYLINE#2,   OUTCOME, MONEYLINE,   CLE,   NULL, NULL,          NULL
 * EVENT#DD/MM/YYYY@HH:MM:SS#2, OUTCOME#PLAYER_PROP#3, OUTCOME, PLAYER_PROP, Over,  200,  Passing Yards, Joe Burrow
 */
public class Outcome extends Entity {
    /*
     * Required
     */
    private Event event;
    private Market market;
    private String result;
    /*
     * Not required.
     */
    private double points;
    private String prop;
    private String player;

    public Outcome(Event event, Market market, String result) {
        this.event = event;
        this.market = market;
        this.result = result;
        this.points = 0;
        this.prop = "";
        this.player = "";
    }

    public Outcome(Event event, Market market, String result, double points) {
        this(event, market, result);
        this.points = points;
    }

    public Outcome(Event event, Market market, String result, double points, String prop, String player) {
        this(event, market, result, points);
        this.prop = prop;
        this.player = player;
    }

    @Override
    public String getPartitionKey() {
        return this.event.getSortKey();
    }

    @Override
    public String getSortKey() {
        return "OUTCOME#" + this.market.getValue() + "#" + this.hashCode();
    }

    @Override
    protected String getType() {
        return "OUTCOME";
    }

    @Override
    public Map<String, AttributeValue> getAttributes() {
        Map<String, AttributeValue> attributeMap = new HashMap<String, AttributeValue>();
        attributeMap.put("market", AttributeValue.builder().n(this.market.getValue() + "").build());
        attributeMap.put("result", AttributeValue.builder().s(this.result).build());
        attributeMap.put("points", AttributeValue.builder().n(this.points + "").build());
        attributeMap.put("prop", AttributeValue.builder().s(this.prop).build());
        attributeMap.put("player", AttributeValue.builder().s(this.player).build());
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
        if (Market.hasPlayer(this.market)) {
            return this.event.hashCode() * this.market.getValue() * this.result.hashCode() * (int) this.points
                    * this.prop.hashCode() * this.player.hashCode();
        } else if (Market.hasPoints(this.market)) {
            return this.event.hashCode() * this.market.getValue() * this.result.hashCode() * (int) this.points;
        } else {
            return this.event.hashCode() * this.market.getValue() * this.result.hashCode();
        }
    }

    public class Builder {

    }
}
