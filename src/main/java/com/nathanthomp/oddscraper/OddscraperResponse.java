package com.nathanthomp.oddscraper;

import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.nathanthomp.oddscraper.odds.Odd;
import com.nathanthomp.oddscraper.odds.OddEvent;
import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddList;
import com.nathanthomp.oddscraper.odds.OddMarket;

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
    private Map<OddEvent, Map<String, Set<Odd>>> odds;

    public OddscraperResponse(OddLeague league, OddMarket market, String status, OddList odds) {
        this(league, market, status, "", odds);
    }

    public OddscraperResponse(OddLeague league, OddMarket market, String status, String message, OddList odds) {
        this.league = league;
        this.market = market;
        this.status = status;
        this.message = message;
        this.odds = odds.getRep();
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

    public Map<OddEvent, Map<String, Set<Odd>>> getOdds() {
        return this.odds;
    }
}
