package com.nathanthomp.oddscraper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class OddscraperRequestHandlerTest {

    @Test
    public void testHandleRequest_StatusCode() {
        OddscraperRequestHandler handler = new OddscraperRequestHandler();

        Context context = mock(Context.class);
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        APIGatewayProxyResponseEvent response = handler.handleRequest(request,
                context);

        assertTrue(response.getStatusCode() == 200);
    }

    // @Test
    // public void testHandleRequest_Body() {
    // OddscraperRequestHandler handler = new OddscraperRequestHandler();

    // Context context = mock(Context.class);
    // APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
    // APIGatewayProxyResponseEvent response = handler.handleRequest(request,
    // context);

    // assertTrue(response.getBody().equals("{\"msg\":\"Hello, AWS!\"}"));
    // }
}