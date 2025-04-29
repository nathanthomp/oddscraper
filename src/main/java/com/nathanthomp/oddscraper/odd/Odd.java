package com.nathanthomp.oddscraper.odd;

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
    public String getPartitionKey() {
        return this.outcome.getSortKey();
    }

    @Override
    public String getSortKey() {
        return "ODD#" + this.price + "#" + this.hashCode();
    }

    @Override
    public String getType() {
        return "ODD";
    }

    @Override
    public int hashCode() {
        return this.price * this.sportsbook.hashCode();
    }
}
