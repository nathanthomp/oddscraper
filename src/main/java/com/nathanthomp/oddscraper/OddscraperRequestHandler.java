package com.nathanthomp.oddscraper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.nathanthomp.oddscraper.exceptions.OddscraperException;
import com.nathanthomp.oddscraper.exceptions.OddscraperRequestException;
import com.nathanthomp.oddscraper.odds.Odd;
import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddList;
import com.nathanthomp.oddscraper.odds.OddMarket;
import com.nathanthomp.oddscraper.scrapers.OddsApiScraper;

public class OddscraperRequestHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final String LEAGUE_REQUEST_PARAMETER_KEY = "league";
    private final String MARKET_REQUEST_PARAMETER_KEY = "market";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        if (request.getQueryStringParameters() == null) {
            return OddscraperResponse
                    .getErrorResponse("Oddscraper Error: Missing required '" + LEAGUE_REQUEST_PARAMETER_KEY + "' and '"
                            + MARKET_REQUEST_PARAMETER_KEY + "' request parameters");
        }

        Map<String, String> requestParameters = request.getQueryStringParameters();

        OddLeague league;
        OddMarket market;
        try {
            league = handleRequestLeagueParameter(requestParameters);
            market = handleRequestMarketParameter(requestParameters);
        } catch (OddscraperException exception) {
            return OddscraperResponse.getErrorResponse(exception.getMessage());
        }

        OddList allOdds = new OddList();
        try {
            // new OddsApiScraper(league, market).scrapeOdds(allOdds);
            // String path = "data/odds-api-response-odds-cbb-moneyline.json";
            // List<Odd> odds = OddsApiScraper.scrapeOddsFromFile(path, league, market);
            // for (Odd odd : odds) {
            // allOdds.addOdd(odd);
            // }
            /*
             * Add more scrapers here
             */
        } catch (Exception e) {
            return OddscraperResponse.getErrorResponse("Could not scrape odds");
        }

        OddList bestOdds = allOdds.getBestOdds();

        OddscraperResponse response = new OddscraperResponse(league, market, bestOdds);

        /*
         * For testing
         */
        // writeResponseToFile(response);

        // try {
        // APIGatewayProxyResponseEvent test =

        // } catch (Exception e) {
        // return new APIGatewayProxyResponseEvent()
        // .withStatusCode(500)
        // .withBody("{\n \"error\": \"" + "Oddscraper Error: Could not get success
        // response: "
        // + e.getMessage() + "\"\n}")
        // .withIsBase64Encoded(false);
        // }

        return response.getSuccessResponse();
    }

    private OddLeague handleRequestLeagueParameter(Map<String, String> parameters) throws OddscraperException {
        if (!parameters.containsKey(LEAGUE_REQUEST_PARAMETER_KEY)) {
            throw new OddscraperRequestException(
                    "Missing required '" + LEAGUE_REQUEST_PARAMETER_KEY + "' request parameter");
        }

        String leagueParameterValue = parameters.get(LEAGUE_REQUEST_PARAMETER_KEY);
        switch (leagueParameterValue.toLowerCase()) {
            case "ucl":
                return OddLeague.ODDLEAGUE_UCL;
            case "nhl":
                return OddLeague.ODDLEAGUE_NHL;
            case "cbb":
                return OddLeague.ODDLEAGUE_CBB;
            default:
                throw new OddscraperRequestException(
                        "Invalid '" + LEAGUE_REQUEST_PARAMETER_KEY + "' request parameter: " + leagueParameterValue);
        }
    }

    private OddMarket handleRequestMarketParameter(Map<String, String> parameters) throws OddscraperException {
        if (!parameters.containsKey(MARKET_REQUEST_PARAMETER_KEY)) {
            throw new OddscraperRequestException(
                    "Missing required '" + MARKET_REQUEST_PARAMETER_KEY + "' request parameter");
        }

        String marketParameterValue = parameters.get(MARKET_REQUEST_PARAMETER_KEY);
        switch (marketParameterValue.toLowerCase()) {
            case "moneyline":
                return OddMarket.ODDMARKET_MONEYLINE;
            case "spread":
                return OddMarket.ODDMARKET_SPREAD;
            case "total":
                return OddMarket.ODDMARKET_TOTAL;
            default:
                throw new OddscraperRequestException(
                        "Invalid '" + MARKET_REQUEST_PARAMETER_KEY + "' request parameter: " + marketParameterValue);
        }
    }
}
