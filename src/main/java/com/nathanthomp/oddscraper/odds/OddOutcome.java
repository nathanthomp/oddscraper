package com.nathanthomp.oddscraper.odds;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OddOutcome {
    /*
     * Choice of participant
     */
    private String result;
    /*
     * Type of outcome (moneyline, spread, or total)
     */
    private OddMarket market;
    /*
     * Points (only for spread and total markets)
     */
    private double points;
    /*
     * Odds
     */
    private Set<Odd> odds;

    public OddOutcome(String result, OddMarket market) {
        this.result = result;
        this.market = market;
        this.odds = new HashSet<Odd>();
    }

    public OddOutcome(String result, OddMarket market, double points) {
        this.result = result;
        this.market = market;
        this.odds = new HashSet<Odd>();
        this.points = points;
    }

    public String getResult() {
        return this.result;
    }

    public OddMarket getMarket() {
        return this.market;
    }

    public double getPoints() {
        return this.points;
    }

    public Set<Odd> getOdds() {
        return this.odds;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OddOutcome)) {
            return false;
        }

        OddOutcome that = (OddOutcome) obj;
        if (this.market == OddMarket.MONEYLINE) {
            if (!(this.result.equals(that.result))) {
                return false;
            }
        } else {
            if (!(this.result.equals(that.result) && this.points == that.points)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        if (this.market == OddMarket.MONEYLINE) {
            return this.result.hashCode() * this.market.hashCode();
        } else {
            return this.result.hashCode() * this.market.hashCode() * Objects.hash(this.points);
        }
    }

    @Override
    public String toString() {
        if (this.market == OddMarket.MONEYLINE) {
            return this.result + " " + this.market;
        } else {
            return this.result + " " + this.points + " " + this.market;
        }
    }
}
