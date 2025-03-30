package com.nathanthomp.oddscraper;

import java.util.Map;
import java.util.Objects;

import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddMarket;

/*
 * Incoming request to AWS lambda, requiring non null and valid values for league and market.
 */
public record OddscraperRequest(String league, String market) {
    /*
     * List of valid league and market values.
     */
    private static Map<String, OddLeague> validLeagues;
    private static Map<String, OddMarket> validMarkets;

    static {
        validLeagues = Map.ofEntries(
                Map.entry("ucl", OddLeague.ODDLEAGUE_UCL),
                Map.entry("nhl", OddLeague.ODDLEAGUE_NHL),
                Map.entry("cbb", OddLeague.ODDLEAGUE_CBB));
        validMarkets = Map.ofEntries(
                Map.entry("moneyline", OddMarket.ODDMARKET_MONEYLINE),
                Map.entry("spread", OddMarket.ODDMARKET_MONEYLINE),
                Map.entry("total", OddMarket.ODDMARKET_MONEYLINE));
    }

    public static OddLeague getOddLeague(String league) {
        return validLeagues.get(league.toLowerCase());
    }

    public static OddMarket getOddMarket(String market) {
        return validMarkets.get(market.toLowerCase());
    }

    public OddscraperRequest {
        /*
         * Require non null league and market values.
         */
        Objects.requireNonNull(league, "league must be non null");
        Objects.requireNonNull(market, "market must be non null");
        /*
         * Require valid league and market values.
         */
        Objects.requireNonNull(validLeagues.get(league), "league must be valid");
        Objects.requireNonNull(validMarkets.get(market), "market must be valid");
    }
}
