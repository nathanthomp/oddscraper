package com.nathanthomp.oddscraper.odds;

public class OddEvent {
    private String team1;
    private String team2;
    private String startTime;

    public OddEvent(String team1, String team2, String startTime) {
        this.team1 = team1;
        this.team2 = team2;
        this.startTime = startTime;
    }

    public String getTeam1() {
        return this.team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return this.team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OddEvent)) {
            return false;
        }

        OddEvent that = (OddEvent) obj;
        if (!(this.team1.equals(that.team1) && this.team2.equals(that.team2)
                && this.startTime.equals(that.startTime))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.team1 + " vs " + this.team2;
    }
}
