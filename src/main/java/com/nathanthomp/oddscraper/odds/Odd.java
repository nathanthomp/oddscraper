package com.nathanthomp.oddscraper.odds;

public class Odd {
    private OddLeague league;
    private OddEvent event;
    private String sportsbook;
    private OddMarket market;
    private String result;
    private int price;

    public Odd(OddLeague league, OddEvent event, String sportsbook, OddMarket market, String result, int price) {
        this.league = league;
        this.event = event;
        this.sportsbook = sportsbook;
        this.market = market;
        this.result = result;
        this.price = price;
    }

    public OddLeague getLeague() {
        return this.league;
    }

    public OddEvent getEvent() {
        return this.event;
    }

    public String getSportsbook() {
        return this.sportsbook;
    }

    public OddMarket getMarket() {
        return this.market;
    }

    public String getResult() {
        return this.result;
    }

    public int getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Odd)) {
            return false;
        }

        Odd that = (Odd) obj;
        if (!(this.event.equals(that.event) && this.result.equals(that.result)
                && this.price == that.price && this.sportsbook.equals(that.sportsbook))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) this.event.hashCode() * this.result.hashCode() * this.price * this.sportsbook.hashCode();
    }

    @Override
    public String toString() {
        return this.event + ": " + this.result + " for " + this.price + " on " + this.sportsbook;
    }
}
