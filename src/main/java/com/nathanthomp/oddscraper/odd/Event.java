package com.nathanthomp.oddscraper.odd;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/*
 * Examples:
 * 
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#1, EVENT, { "participant1": "CIN", "participant2": "CLE", "startTime": "DD/MM/YYYY@HH:MM:SS" }
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#1, EVENT, ["CIN", "CLE"], DD/MM/YYYY@HH:MM:SS
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#2, EVENT, ["CIN", "CLE"], DD/MM/YYYY@HH:MM:SS
 * LEAGUE#1, EVENT#DD/MM/YYYY@HH:MM:SS#3, EVENT, ["CIN", "CLE"], DD/MM/YYYY@HH:MM:SS
 * LEAGUE#2, EVENT#DD/MM/YYYY@HH:MM:SS#4, EVENT, ["BUF", "PHI"], DD/MM/YYYY@HH:MM:SS
 */
public class Event extends Entity {
    private League league;
    private String participant1;
    private String participant2;
    private String startDateTime;

    public Event(League league, String participant1, String participant2, String startDateTime) {
        this.league = league;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.startDateTime = startDateTime;
    }

    @Override
    protected String getPartitionKey() {
        return "LEAGUE#" + this.league.ordinal();
    }

    @Override
    protected String getSortKey() {
        return "EVENT#" + this.startDateTime + "#" + this.hashCode();
    }

    @Override
    protected String getType() {
        return "EVENT";
    }

    @Override
    protected Map<String, AttributeValue> getAttributes() {
        Map<String, AttributeValue> attributeMap = new HashMap<String, AttributeValue>();
        attributeMap.put("participant1", AttributeValue.builder().s(this.participant1).build());
        attributeMap.put("participant2", AttributeValue.builder().s(this.participant2).build());
        attributeMap.put("startDateTime", AttributeValue.builder().s(this.startDateTime).build());
        return attributeMap;
    }

    @Override
    public int hashCode() {
        /*
         * Event has to have the same league, same participants, and the same start
         * time.
         */
        return this.league.ordinal() * this.participant1.hashCode() * this.participant2.hashCode()
                * this.startDateTime.hashCode();
    }
}
