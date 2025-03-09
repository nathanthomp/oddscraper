package com.nathanthomp.oddscraper.odds;

public class Odd {
    private String sport;
    private String team1;
    private String team2;
    private String sportsbook;
    private String type;
    private String name;
    private int price;

    public Odd(String sport, String team1, String team2, String sportsbook, String type, String name, int price) {
        this.sport = sport;
        this.team1 = team1;
        this.team2 = team2;
        this.sportsbook = sportsbook;
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getSportsbook() {
        return sportsbook;
    }

    public void setSportsbook(String sportsbook) {
        this.sportsbook = sportsbook;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
