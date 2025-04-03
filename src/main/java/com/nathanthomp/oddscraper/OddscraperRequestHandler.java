package com.nathanthomp.oddscraper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddList;
import com.nathanthomp.oddscraper.odds.OddMarket;
import com.nathanthomp.oddscraper.scrapers.OddsApiScraper;

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
        OddList odds = new OddList();
        try {
            new OddsApiScraper(league, market).scrapeOdds(odds);
            /*
             * Add more scrapers here
             */

            /*
             * Testing
             */
            // String path = "data/odds-api-response-odds-cbb-moneyline.json";
            // List<Odd> oddsFromFile = OddsApiScraper.scrapeOddsFromFile(path, league,
            // market);
            // for (Odd odd : oddsFromFile) {
            // odds.addOdd(odd);
            // }
        } catch (Exception e) {
            return new OddscraperResponse(league, market, "failure", e.getMessage(), odds);
        }

        return new OddscraperResponse(league, market, "success", odds);
    }
}
