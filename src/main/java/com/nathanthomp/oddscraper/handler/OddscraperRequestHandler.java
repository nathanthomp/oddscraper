package com.nathanthomp.oddscraper.handler;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nathanthomp.oddscraper.odd.OddEvent;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddList;
import com.nathanthomp.oddscraper.odd.OddMarket;
import com.nathanthomp.oddscraper.scraper.OddsApiScraper;

/*
 * NEW STEPS:
 * 1. Get active leagues
 * 2. for each active leauge, get valid markets
 * 3. for each market in active leauge, get odds
 * 3.1 if get odds fails, log warning and continue
 */

/**
 * Lambda handler for scraping sports betting odds from sportsbooks and writing
 * odds to a database.
 */
public class OddscraperRequestHandler implements RequestHandler<OddscraperRequest, OddscraperResponse> {

    private static OddLeague[] activeLeagues = new OddLeague[] { OddLeague.NHL };

    @Override
    public OddscraperResponse handleRequest(OddscraperRequest request, Context context) {
        /*
         * Try to scrape odds from active leagues into a list
         */
        OddList oddList = new OddList();
        try {
            for (OddLeague league : activeLeagues) {
                OddsApiScraper oddsApiScraper = new OddsApiScraper(league);

                OddMarket[] markets = OddMarket.getMarkets(league);

                for (OddMarket market : markets) {
                    try {
                        OddsApiScraper.scrapeOdds(market, oddList);
                    } catch (Exception e) {
                        // TODO: Log error Could not scrape odds for leauge and market
                    }

                }

                if (markets.length == 0) {
                    // TODO: Log warning if markets is empty
                }
            }
        } catch (Exception e) {
            // TODO: Log fatal
            return new OddscraperResponse("failure", e.getMessage());
        }

        /*
         * Try to write list of odds to database
         */
        try {
            writeToDatabase(oddList);
        } catch (Exception e) {
            // TODO: Log error
            return new OddscraperResponse("failure", e.getMessage());
        }

        return new OddscraperResponse("success", "");
    }

    private void writeToDatabase(OddList oddList) {
        /*
         * For each event in oddList, add to events table referencing league
         * For each outcome in oddList, add to outcomes table referencing event
         * For each odd in oddList, add to odds table referencing outcome
         */
    }

    /*
     * Testing
     */
    private static String path = "";

    public static void main(String[] args) {
        Set<OddEvent> events = testOutright();
        /*
         * How to find middle bets?
         * 1. Get outcomes for totals
         */
    }

    private static Set<OddEvent> testMoneyline() {
        OddscraperRequestHandler.path = "data/odds-api-response/ucl-moneyline.json";
        OddscraperRequestHandler handler = new OddscraperRequestHandler();
        OddscraperRequest request = new OddscraperRequest("ucl", "moneyline");
        OddscraperResponse response = handler.handleRequest(request, null);
        return response.getEvents();
    }

    private static Set<OddEvent> testSpread() {
        OddscraperRequestHandler.path = "data/odds-api-response/cbb-spread.json";
        OddscraperRequestHandler handler = new OddscraperRequestHandler();
        OddscraperRequest request = new OddscraperRequest("cbb", "spread");
        OddscraperResponse response = handler.handleRequest(request, null);
        return response.getEvents();
    }

    private static Set<OddEvent> testTotal() {
        OddscraperRequestHandler.path = "data/odds-api-response/nhl-total.json";
        OddscraperRequestHandler handler = new OddscraperRequestHandler();
        OddscraperRequest request = new OddscraperRequest("nhl", "total");
        OddscraperResponse response = handler.handleRequest(request, null);
        return response.getEvents();
    }

    private static Set<OddEvent> testOutright() {
        OddscraperRequestHandler.path = "data/odds-api-response/masters-outright.json";
        OddscraperRequestHandler handler = new OddscraperRequestHandler();
        OddscraperRequest request = new OddscraperRequest("masters", "outright");
        OddscraperResponse response = handler.handleRequest(request, null);
        return response.getEvents();
    }
}
