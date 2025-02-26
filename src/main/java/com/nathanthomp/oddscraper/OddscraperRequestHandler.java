package com.nathanthomp.oddscraper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class OddscraperRequestHandler implements RequestHandler<Request, Response> {

    @Override
    public Response handleRequest(Request request, Context context) {

        return new Response("Hello, world!");
    }

}

record Request(String content) {
}

record Response(String content) {
}
