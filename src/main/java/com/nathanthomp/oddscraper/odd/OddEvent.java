package com.nathanthomp.oddscraper.odd;

import java.util.HashSet;
import java.util.Set;

public class OddEvent {
    /*
     * Participants
     */
    private String team1;
    private String team2;
    /*
     * Time
     */
    private String startTime;

    /*
     * Outcomes
     */
    private Set<OddOutcome> outcomes;

    public OddEvent(String team1, String team2, String startTime) {
        this.team1 = team1;
        this.team2 = team2;
        this.startTime = startTime;
        this.outcomes = new HashSet<OddOutcome>();
    }

    public String getTeam1() {
        return this.team1;
    }

    public String getTeam2() {
        return this.team2;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public Set<OddOutcome> getOutcomes() {
        return this.outcomes;
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
    public int hashCode() {
        return this.team1.hashCode() * this.team2.hashCode() * this.startTime.hashCode();
    }

    @Override
    public String toString() {
        return this.team1 + " vs " + this.team2 + " @ " + this.startTime;
    }
}
