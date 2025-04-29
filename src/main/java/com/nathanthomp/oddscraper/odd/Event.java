package com.nathanthomp.oddscraper.odd;

/*
 * Examples:
 * 
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#1, EVENT, ["CIN", "CLE"], DD/MM/YYYY@HH:MM:SS
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#2, EVENT, ["CIN", "CLE"], DD/MM/YYYY@HH:MM:SS
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#3, EVENT, ["CIN", "CLE"], DD/MM/YYYY@HH:MM:SS
 * LEAGUE#2, EVENT#DD/MM/YYYY@HH:MM:SS#4, EVENT, ["BUF", "PHI"], DD/MM/YYYY@HH:MM:SS
 */
public class Event extends Entity {
    private League league;
    private String participant1;
    private String participant2;
    private String startTime;

    public Event(League league, String participant1, String participant2, String startTime) {
        this.league = league;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.startTime = startTime;
    }

    @Override
    public String getPartitionKey() {
        return "LEAGUE#" + this.league.ordinal();
    }

    @Override
    public String getSortKey() {
        return "EVENT#" + this.startTime + "#" + this.hashCode();
    }

    @Override
    public String getType() {
        return "EVENT";
    }

    @Override
    public int hashCode() {
        /*
         * Event has to have the same league, same participants, and the same start
         * time.
         */
        return this.league.ordinal() * this.participant1.hashCode() * this.participant2.hashCode()
                * this.startTime.hashCode();
    }
}
