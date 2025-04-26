package com.nathanthomp.oddscraper.handler;

import java.util.Map;
import java.util.Objects;

import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddMarket;

/*
 * Incoming request to AWS lambda, requiring non null and valid values for league and market.
 */
public record OddscraperRequest(String league, String market) {
    /*
     * Valid Oddscraper leagues and their matching OddLeauge.
     */
    private final static Map<String, OddLeague> validLeagues = Map.ofEntries(
            Map.entry("ucl", OddLeague.UCL),
            Map.entry("nhl", OddLeague.NHL),
            Map.entry("cbb", OddLeague.CBB),
            Map.entry("masters", OddLeague.MASTERS));
    /*
     * Valid Oddscraper markets and their matching OddMarket.
     */
    private final static Map<String, OddMarket> validMarkets = Map.ofEntries(
            Map.entry("moneyline", OddMarket.MONEYLINE),
            Map.entry("spread", OddMarket.SPREAD),
            Map.entry("total", OddMarket.TOTAL),
            Map.entry("outright", OddMarket.OUTRIGHT));

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
