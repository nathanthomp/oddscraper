package com.nathanthomp.oddscraper.odd;

public class Odd {
    private String sportsbook;
    private int price;

    public Odd(String sportsbook, int price) {
        this.sportsbook = sportsbook;
        this.price = price;
    }

    public String getSportsbook() {
        return this.sportsbook;
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
        if (!(this.sportsbook.equals(that.sportsbook) && this.price == that.price)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return this.price * this.sportsbook.hashCode();
    }

    @Override
    public String toString() {
        return this.price + " on " + this.sportsbook;
    }
}
