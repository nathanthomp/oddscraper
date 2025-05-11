package com.nathanthomp.oddscraper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.nathanthomp.oddscraper.handler.OddscraperRequest;
import com.nathanthomp.oddscraper.handler.OddscraperRequestHandler;
import com.nathanthomp.oddscraper.handler.OddscraperResponse;

public class OddscraperRequestHandlerTest {

        @Test
        public void testHandleRequest() {
                OddscraperRequest request = new OddscraperRequest();
                OddscraperRequestHandler handler = new OddscraperRequestHandler();
                OddscraperResponse response = handler.handleRequest(request, null);
                assertTrue(response != null);
        }
}
