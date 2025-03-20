package com.nathanthomp.oddscraper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class OddscraperRequestHandlerTest {

        @Test
        public void testHandleRequest_StatusCode500_MissingRequiredLeaugeRequestParameter() {
                Context context = mock(Context.class);
                APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                Map<String, String> queryParameters = new HashMap<String, String>();
                queryParameters.put("market", "moneyline");

                request.setQueryStringParameters(queryParameters);

                OddscraperRequestHandler handler = new OddscraperRequestHandler();
                APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                                context);

                assertTrue(response.getStatusCode() == 500
                                && response.getBody().contains("Missing required 'league' request parameter"));
        }

        @Test
        public void testHandleRequest_StatusCode500_MissingRequiredMarketRequestParameter() {
                Context context = mock(Context.class);
                APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                Map<String, String> queryParameters = new HashMap<String, String>();
                queryParameters.put("league", "ucl");

                request.setQueryStringParameters(queryParameters);

                OddscraperRequestHandler handler = new OddscraperRequestHandler();
                APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                                context);

                assertTrue(response.getStatusCode() == 500
                                && response.getBody().contains("Missing required 'market' request parameter"));
        }

        @Test
        public void testHandleRequest_StatusCode500_InvalidLeagueRequestParameter() {
                Context context = mock(Context.class);
                APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                Map<String, String> queryParameters = new HashMap<String, String>();
                queryParameters.put("league", "something");
                queryParameters.put("market", "moneyline");

                request.setQueryStringParameters(queryParameters);

                OddscraperRequestHandler handler = new OddscraperRequestHandler();
                APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                                context);

                assertTrue(response.getStatusCode() == 500
                                && response.getBody().contains("Invalid 'league' request parameter:"));
        }

        @Test
        public void testHandleRequest_StatusCode500_InvalidMarketRequestParameter() {
                Context context = mock(Context.class);
                APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

                Map<String, String> queryParameters = new HashMap<String, String>();
                queryParameters.put("league", "ucl");
                queryParameters.put("market", "something");

                request.setQueryStringParameters(queryParameters);

                OddscraperRequestHandler handler = new OddscraperRequestHandler();
                APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                                context);

                assertTrue(response.getStatusCode() == 500
                                && response.getBody().contains("Invalid 'market' request parameter:"));
        }
}
