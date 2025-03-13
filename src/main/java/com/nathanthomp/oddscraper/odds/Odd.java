package com.nathanthomp.oddscraper.odds;

public class Odd {
    private String league;
    private OddEvent event;
    private String sportsbook;
    private String type;
    private String name;
    private int price;

    public Odd(String league, OddEvent event, String sportsbook, String type, String name, int price) {
        this.league = league;
        this.event = event;
        this.sportsbook = sportsbook;
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public String getLeague() {
        return this.league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public OddEvent getEvent() {
        return this.event;
    }

    public void setEvent(OddEvent event) {
        this.event = event;
    }

    public String getSportsbook() {
        return this.sportsbook;
    }

    public void setSportsbook(String sportsbook) {
        this.sportsbook = sportsbook;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
