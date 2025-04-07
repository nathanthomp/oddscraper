package com.nathanthomp.oddscraper;

import java.util.Set;

import com.nathanthomp.oddscraper.odd.OddEvent;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddMarket;

/*
 * Outgoing response from AWS lambda, returning a list of scraped odds.
 */
public class OddscraperResponse {
    /*
     * Computed league and market values from request.
     */
    private OddLeague league;
    private OddMarket market;
    /*
     * Status and status message.
     */
    private String status;
    private String message;
    /*
     * JSON representation of scraped odds.
     */
    private Set<OddEvent> events;

    public OddscraperResponse(OddLeague league, OddMarket market, String status, Set<OddEvent> events) {
        this(league, market, status, "", events);
    }

    public OddscraperResponse(OddLeague league, OddMarket market, String status, String message, Set<OddEvent> events) {
        this.league = league;
        this.market = market;
        this.status = status;
        this.message = message;
        this.events = events;
    }

    public OddLeague getLeague() {
        return this.league;
    }

    public OddMarket getMarket() {
        return this.market;
    }

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Set<OddEvent> getEvents() {
        return this.events;
    }
}
