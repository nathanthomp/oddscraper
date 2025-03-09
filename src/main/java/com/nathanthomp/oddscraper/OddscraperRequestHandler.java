package com.nathanthomp.oddscraper;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nathanthomp.oddscraper.odds.Odd;
import com.nathanthomp.oddscraper.scrapers.OddsApiScraper;
import com.nathanthomp.oddscraper.scrapers.Scraper;

public class OddscraperRequestHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        // Read request data
        // - auth token
        // - sport

        // Supported sports:
        // nfl, nhl, ucl,

        // Scrape for odds data
        Map<String, String> queryParams = request.getQueryStringParameters();
        String league = queryParams.get("league");

        List<Odd> odds = new LinkedList<Odd>();
        try {
            Scraper oddsApiScraper = new OddsApiScraper();
            odds.addAll(oddsApiScraper.scrapeOdds(league));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody(e.getMessage())
                    .withIsBase64Encoded(false);
        }

        // Calculate odds data
        Oddscraper.findArbitrageBets(odds);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Odd>>() {
        }.getType();
        String json = gson.toJson(odds, listType);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(json)
                .withIsBase64Encoded(false);
    }
}
