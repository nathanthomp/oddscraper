package com.nathanthomp.oddscraper;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nathanthomp.oddscraper.odd.OddEvent;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddList;
import com.nathanthomp.oddscraper.odd.OddMarket;
import com.nathanthomp.oddscraper.scraper.OddsApiScraper;

public class OddscraperRequestHandler implements RequestHandler<OddscraperRequest, OddscraperResponse> {

    @Override
    public OddscraperResponse handleRequest(OddscraperRequest request, Context context) {
        /*
         * Get league and market values from request.
         */
        OddLeague league = OddscraperRequest.getOddLeague(request.league());
        OddMarket market = OddscraperRequest.getOddMarket(request.market());
        /*
         * Get odds for league and market.
         */
        Set<OddEvent> events = new HashSet<OddEvent>();

        OddList oddList = new OddList();
        try {
            /*
             * TODO: market should be at the scrape odds level
             */
            events = new OddsApiScraper(league, market).scrapeOdds();

            /*
             * Add more scrapers here
             */

            /*
             * Testing
             */
            // events = OddsApiScraper.scrapeOddsFromFile(OddscraperRequestHandler.path,
            // league, market);

        } catch (Exception e) {
            return new OddscraperResponse(league, market, "failure", e.getMessage(), events);
        }

        return new OddscraperResponse(league, market, "success", events);
    }

    /*
     * Testing
     */
    private static String path = "";

    public static void main(String[] args) {
        Set<OddEvent> events = testTotal();
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
}
