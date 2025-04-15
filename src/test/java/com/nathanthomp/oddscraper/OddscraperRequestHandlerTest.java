package com.nathanthomp.oddscraper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddMarket;

public class OddscraperRequestHandlerTest {

        @Test
        public void testHandleRequest() {
                // OddLeague league = OddLeague.ODDLEAGUE_CBB;
                // OddMarket market = OddMarket.ODDMARKET_MONEYLINE;

                // Context context = mock(Context.class);

                // OddscraperRequest request = new OddscraperRequest("cbb", "moneyline");

                // OddscraperRequestHandler handler = new OddscraperRequestHandler();

                // OddscraperResponse response = handler.handleRequest(request, context);

                // assertTrue(response.getLeague() == league && response.getMarket() == market);
        }

        @Test
        public void testHandleRequest_MissingRequiredMarketRequestParameter() {
                // Context context = mock(Context.class);
                // APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                // Map<String, String> queryParameters = new HashMap<String, String>();
                // queryParameters.put("league", "ucl");

                // request.setQueryStringParameters(queryParameters);

                // OddscraperRequestHandler handler = new OddscraperRequestHandler();
                // APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                // context);

                // assertTrue(response.getStatusCode() == 200
                // && response.getBody().contains("Missing required 'market' request
                // parameter"));
        }

        @Test
        public void testHandleRequest_InvalidLeagueRequestParameter() {
                // Context context = mock(Context.class);
                // APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                // Map<String, String> queryParameters = new HashMap<String, String>();
                // queryParameters.put("league", "something");
                // queryParameters.put("market", "moneyline");

                // request.setQueryStringParameters(queryParameters);

                // OddscraperRequestHandler handler = new OddscraperRequestHandler();
                // APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                // context);

                // assertTrue(response.getStatusCode() == 200
                // && response.getBody().contains("Invalid 'league' request parameter:"));
        }

        @Test
        public void testHandleRequest_InvalidMarketRequestParameter() {
                // Context context = mock(Context.class);
                // APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                // Map<String, String> queryParameters = new HashMap<String, String>();
                // queryParameters.put("league", "ucl");
                // queryParameters.put("market", "something");

                // request.setQueryStringParameters(queryParameters);

                // OddscraperRequestHandler handler = new OddscraperRequestHandler();
                // APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                // context);

                // assertTrue(response.getStatusCode() == 200
                // && response.getBody().contains("Invalid 'market' request parameter:"));
        }

        @Test
        public void testHandleRequest_StatusCode200() {
                // Context context = mock(Context.class);
                // APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                // Map<String, String> queryParameters = new HashMap<String, String>();
                // queryParameters.put("league", "cbb");
                // queryParameters.put("market", "moneyline");

                // request.setQueryStringParameters(queryParameters);

                // OddscraperRequestHandler handler = new OddscraperRequestHandler();
                // APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                // context);

                // assertTrue(response.getStatusCode() == 200);
        }

}
