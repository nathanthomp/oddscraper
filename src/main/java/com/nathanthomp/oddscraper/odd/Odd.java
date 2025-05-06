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
        Event event = this.outcome.getEvent();
        League league = event.getLeague();
        Market market = this.outcome.getMarket();
        return "LEAGUE#" + league.getValue() + "#EVENT#" + event.hashCode() + "#OUTCOME#" + market.getValue();
    }

    @Override
    public String getSortKey() {
        Market market = this.outcome.getMarket();
        if (Market.hasPlayer(market)) {
            return "ODD#" + this.outcome.getResult() + "#" + this.outcome.getPoints() + "#" + this.outcome.getProp()
                    + "#" + this.outcome.getPlayer() + "#" + this.price + "#" + this.sportsbook.toUpperCase();
        } else if (Market.hasPoints(market)) {
            return "ODD#" + this.outcome.getResult() + "#" + this.outcome.getPoints() + "#" + this.price + "#"
                    + this.sportsbook.toUpperCase();
        } else {
            return "ODD#" + this.outcome.getResult() + "#" + this.price + "#" + this.sportsbook.toUpperCase();
        }
    }

    // @Override
    // protected String getType() {
    // return "ODD";
    // }

    @Override
    public Map<String, AttributeValue> getAttributes() {
        Event event = this.outcome.getEvent();
        Market market = this.outcome.getMarket();

        Map<String, AttributeValue> attributeMap = new HashMap<String, AttributeValue>();
        attributeMap.put("SPORTSBOOK", AttributeValue.builder().s(this.sportsbook).build());
        attributeMap.put("PRICE", AttributeValue.builder().n(this.price + "").build());
        attributeMap.put("LEAGUE", AttributeValue.builder().s("LEAGUE#" + event.getLeague().getValue()).build());
        attributeMap.put("EVENT", AttributeValue.builder().s("EVENT#" + event.hashCode()).build());
        attributeMap.put("MARKET", AttributeValue.builder().s(market.getValue() + "").build());
        attributeMap.put("RESULT", AttributeValue.builder().s(this.outcome.getResult()).build());

        if (Market.hasPlayer(market)) {
            attributeMap.put("POINTS", AttributeValue.builder().n(this.outcome.getPoints() + "").build());
            attributeMap.put("PROP", AttributeValue.builder().s(this.outcome.getProp()).build());
            attributeMap.put("PLAYER", AttributeValue.builder().s(this.outcome.getPlayer()).build());
        } else if (Market.hasPoints(market)) {
            attributeMap.put("POINTS", AttributeValue.builder().n(this.outcome.getPoints() + "").build());
        }

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
