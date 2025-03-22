package com.nathanthomp.oddscraper;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddList;
import com.nathanthomp.oddscraper.odds.OddMarket;

public class OddscraperResponse {

    private OddLeague league;
    private OddMarket market;
    private OddList bestOdds;

    public OddscraperResponse(OddLeague league, OddMarket market, OddList bestOdds) {
        this.league = league;
        this.market = market;
        this.bestOdds = bestOdds;
    }

    public APIGatewayProxyResponseEvent getSuccessResponse() {
        String body = "{\n    \"league\": \"" + this.league + "\",\n    \"market\": \"" + this.market
                + "\",\n    \"bestodds\": []\n}";

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(body)
                .withIsBase64Encoded(false);
    }

    public static APIGatewayProxyResponseEvent getErrorResponse(String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("{\n    \"error\": \"" + message + "\"\n}")
                .withIsBase64Encoded(false);
    }

    /*
     * Eventually, this needs to be json
     */
    @Override
    public String toString() {
        return bestOdds.toString();
    }
}
