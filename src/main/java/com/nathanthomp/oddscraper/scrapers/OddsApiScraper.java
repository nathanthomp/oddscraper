package com.nathanthomp.oddscraper.scrapers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.nathanthomp.oddscraper.exceptions.OddscraperException;
import com.nathanthomp.oddscraper.exceptions.OddscraperHttpRequestException;
import com.nathanthomp.oddscraper.exceptions.OddscraperHttpResponseException;
import com.nathanthomp.oddscraper.exceptions.OddscraperParserException;
import com.nathanthomp.oddscraper.odds.Odd;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

public class OddsApiScraper implements Scraper {
    private static final String ODDS_API_HOST = "https://api.the-odds-api.com";

    private HttpClient httpClient;

    public OddsApiScraper() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Odd> getOddsFromFile(String league) throws OddscraperException {

        String body;
        try {
            body = new String(Files.readAllBytes(Paths.get("data/odds-api-response-odds-1.json")));
        } catch (Exception e) {
            return null;
        }

        List<Odd> odds = new LinkedList<Odd>();
        try {
            Gson gson = new Gson();
            OddsApiEvent[] events = gson.fromJson(body, OddsApiEvent[].class);

            for (OddsApiEvent oddsApiEvent : events) {
                String homeTeam = oddsApiEvent.getHomeTeam();
                String awayTeam = oddsApiEvent.getAwayTeam();

                for (OddsApiEvent.Bookmaker bookmaker : oddsApiEvent.getBookmakers()) {
                    String sportsbook = bookmaker.getKey();

                    for (OddsApiEvent.Bookmaker.Market market : bookmaker.getMarkets()) {
                        String type = market.getKey();

                        for (OddsApiEvent.Bookmaker.Market.Outcome outcome : market.getOutcomes()) {
                            String name = outcome.getName();
                            int price = outcome.getPrice();

                            Odd odd = new Odd(league, homeTeam, awayTeam, sportsbook, type, name, price);
                            odds.add(odd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new OddscraperParserException();
        }

        return odds;
    }

    public List<Odd> scrapeOdds(String league) throws OddscraperException {

        HttpRequest request;
        try {
            String oddsApiKey = getOddsApiKey();
            String endpoint = getOddsApiOddsEndpoint(league, oddsApiKey);
            request = createHttpRequest(endpoint);
        } catch (Exception e) {
            throw new OddscraperHttpRequestException();
        }

        HttpResponse<String> response;
        try {
            response = getHttpResponse(request);
        } catch (Exception e) {
            throw new OddscraperHttpResponseException();
        }

        // Potential status codes
        // 401: Unauthenticated or unauthorized. The API key might be missing or invalid
        // (unauthenticated), or it might at its usage limit (unauthorized). The
        // repsonse body will contain more info
        // 422: One or more of the query params are invalid. The repsonse body will
        // contain more info
        // 429: Requests are being sent too frequently - the request was throttled
        // 500: Internal error
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new OddscraperHttpResponseException();
        }

        String body = response.body();

        List<Odd> odds = new LinkedList<Odd>();
        try {
            Gson gson = new Gson();
            OddsApiEvent[] events = gson.fromJson(body, OddsApiEvent[].class);

            for (OddsApiEvent oddsApiEvent : events) {
                String homeTeam = oddsApiEvent.getHomeTeam();
                String awayTeam = oddsApiEvent.getAwayTeam();

                for (OddsApiEvent.Bookmaker bookmaker : oddsApiEvent.getBookmakers()) {
                    String sportsbook = bookmaker.getKey();

                    for (OddsApiEvent.Bookmaker.Market market : bookmaker.getMarkets()) {
                        String type = market.getKey();

                        for (OddsApiEvent.Bookmaker.Market.Outcome outcome : market.getOutcomes()) {
                            String name = outcome.getName();
                            int price = outcome.getPrice();

                            Odd odd = new Odd(league, homeTeam, awayTeam, sportsbook, type, name, price);
                            odds.add(odd);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new OddscraperParserException();
        }

        return odds;
    }

    private static String getOddsApiOddsEndpoint(String league, String oddsApiKey) {
        return ODDS_API_HOST + "/v4/sports/" + league + "/odds?apiKey=" + oddsApiKey
                + "&regions=us&markets=h2h&dateFormat=iso&oddsFormat=american";
    }

    private String getOddsApiKey() {
        String secretName = "OddscraperSecretsManager56C-45zSscfjucq3";
        Region region = Region.of("us-east-2");

        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        String secretString;

        try {
            secretString = secretsClient.getSecretValue(getSecretValueRequest).secretString();
        } catch (Exception e) {
            return "";
        }

        Gson gson = new Gson();
        Object obj = gson.fromJson(secretString, Object.class);
        if (!(obj instanceof LinkedTreeMap)) {
            return "";
        }

        LinkedTreeMap<String, String> secrets = (LinkedTreeMap) obj;

        String oddsApiKey = "";
        int maxRemainingCredits = 0;

        for (Map.Entry<String, String> secret : secrets.entrySet()) {
            if (secret.getKey().startsWith("OddsApiKey")) {
                String endpoint = ODDS_API_HOST + "/v4/sports?apiKey=" + secret.getValue();

                HttpRequest request = createHttpRequest(endpoint);
                HttpResponse<String> response;
                try {
                    response = getHttpResponse(request);
                } catch (Exception e) {
                    continue;
                }

                int remainingCredits = Integer.parseInt(getHttpResonseHeader(response, "x-requests-remaining"));

                if (remainingCredits > maxRemainingCredits) {
                    maxRemainingCredits = remainingCredits;
                    oddsApiKey = secret.getValue();
                }

            }
        }

        return oddsApiKey;
    }

    private HttpRequest createHttpRequest(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        return request;
    }

    private HttpResponse<String> getHttpResponse(HttpRequest request) throws Exception {
        BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, bodyHandler);
        return response;
    }

    private String getHttpResonseHeader(HttpResponse<String> response, String key) {
        HttpHeaders headers = response.headers();
        // TODO:
        String firstHeaderValue = headers.map().get("x-requests-remaining").getFirst();
        return firstHeaderValue;
    }

    /**
     * A successful response includes a list of available sports and tournaments
     * 
     * @param includeOutOfSeason Boolean determining whether to include out of
     *                           season sports
     */
    // public static String getSports(boolean includeOutOfSeason) {

    // // Create the endpoint
    // String endpoint = "/v4/sports/?apiKey=" + ODDS_API_KEY_1 + "&all=";
    // if (includeOutOfSeason) {
    // endpoint += "true";
    // } else {
    // endpoint += "false";
    // }

    // // Create HTTP request for endpoint
    // HttpRequest request = HttpRequest.newBuilder()
    // .uri(URI.create(ODDS_API_HOST + endpoint))
    // .GET()
    // .build();

    // // Create HTTP client for request and response
    // HttpClient client = HttpClient.newHttpClient();

    // // Get the response
    // HttpResponse<String> response;
    // try {
    // response = client.send(request, HttpResponse.BodyHandlers.ofString());
    // } catch (Exception e) {
    // return "";
    // }

    // int statusCode = response.statusCode();
    // if (statusCode != 200) {
    // return "";
    // }

    // String body = response.body();

    // return body;
    // }

    // Get odds
    // Get scores
    // Get events
    // public static String getEvents(String sport) {

    // String oddsApiKey = getOddsApiKey();

    // String endpoint = "/v4/sports/" + sport + "/events?apiKey=" + oddsApiKey;

    // // Create HTTP request for endpoint
    // HttpRequest request = HttpRequest.newBuilder()
    // .uri(URI.create(ODDS_API_HOST + endpoint))
    // .GET()
    // .build();

    // // Create HTTP client for request and response
    // HttpClient client = HttpClient.newHttpClient();

    // // Get the response
    // HttpResponse<String> response;
    // try {
    // response = client.send(request, HttpResponse.BodyHandlers.ofString());
    // } catch (Exception e) {
    // return "";
    // }

    // int statusCode = response.statusCode();
    // if (statusCode != 200) {
    // return "";
    // }

    // String body = response.body();

    // return body;
    // }
    // Get event odds
    // Get participants

    // Unsupported
    // Get historical odds
    // Get historical events
    // Get historical event odds

}
