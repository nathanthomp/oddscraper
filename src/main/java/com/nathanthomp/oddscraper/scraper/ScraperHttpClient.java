package com.nathanthomp.oddscraper.scraper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

public class ScraperHttpClient {
    private static ScraperHttpClient instance = null;

    public static ScraperHttpClient getInstance() {
        if (instance == null) {
            instance = new ScraperHttpClient();
        }
        return instance;
    }

    private HttpClient httpClient;

    private ScraperHttpClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> getHttpResponse(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endpoint)).GET().build();

        BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        return httpClient.send(request, bodyHandler);
    }

    public String getHttpResonseHeader(HttpResponse<String> response, String key) {
        HttpHeaders headers = response.headers();
        return headers.map().get(key).getFirst();
    }
}
