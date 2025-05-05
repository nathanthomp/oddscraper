package com.nathanthomp.oddscraper.scraper;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.nathanthomp.oddscraper.odd.Odd;
import com.nathanthomp.oddscraper.odd.OddList;
import com.nathanthomp.oddscraper.odd.Outcome;
import com.nathanthomp.oddscraper.odd.Event;
import com.nathanthomp.oddscraper.odd.League;
import com.nathanthomp.oddscraper.odd.Market;

public class OddsApiScraper {
    /*
     * Base endpoint for OddsApi
     */
    private static final String ODDS_API_HOST = "https://api.the-odds-api.com";

    public OddsApiScraper() {
        /*
         * TODO:
         */
    }

    public void scrapeOdds(League league, Market market, OddList oddList) throws Exception {
        /*
         * Call OddsApi endpoint and get OddsApi data.
         */
        String oddsApiLeague = League.convertToOddsApiLeauge(league);
        String oddsApiMarket = Market.convertToOddsApiMarket(market);
        String oddsApiKey = getOddsApiKey();
        String endpoint = getOddsApiOddsEndpoint(oddsApiLeague, oddsApiMarket, oddsApiKey);
        ScraperHttpClient httpClient = ScraperHttpClient.getInstance();
        HttpResponse<String> response = httpClient.getHttpResponse(endpoint);
        /*
         * Get OddsApi response
         * Potential status codes: 401, 422, 429, 500
         */
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new Exception("Could not get OddsApi data: " + response.body());
        }
        /*
         * Parse OddsApi data to Odds.
         */
        Gson gson = new Gson();
        OddsApiEvent[] oddsApiEvents = gson.fromJson(response.body(), OddsApiEvent[].class);
        convert(oddsApiEvents, league, market, oddList);
    }

    private String getOddsApiKey() throws Exception {

        ScraperSecretsManagerClient secretsManagerClient = ScraperSecretsManagerClient.getInstance();
        ScraperHttpClient httpClient = ScraperHttpClient.getInstance();

        String oddsApiKey = "";
        int maxRemainingCredits = 0;

        for (Map.Entry<String, String> secretEntry : secretsManagerClient.getOddsApiSecrets().entrySet()) {
            String endpoint = ODDS_API_HOST + "/v4/sports?apiKey=" + secretEntry.getValue();

            HttpResponse<String> response;
            try {
                response = httpClient.getHttpResponse(endpoint);
            } catch (Exception e) {
                continue;
            }

            int remainingCredits = Integer
                    .parseInt(httpClient.getHttpResonseHeader(response, "x-requests-remaining"));

            if (remainingCredits > maxRemainingCredits) {
                maxRemainingCredits = remainingCredits;
                oddsApiKey = secretEntry.getValue();
            }
        }

        if (oddsApiKey.isEmpty()) {
            throw new Exception("Could not get OddsApi key");
        }

        return oddsApiKey;
    }

    private String getOddsApiOddsEndpoint(String oddsApiLeague, String oddsApiMarket, String oddsApiKey) {
        return ODDS_API_HOST + "/v4/sports/" + oddsApiLeague + "/odds?apiKey=" + oddsApiKey
                + "&regions=us&markets=" + oddsApiMarket + "&dateFormat=iso&oddsFormat=american";
    }

    private static void convert(OddsApiEvent[] oddsApiEvents, League league, Market market, OddList oddList) {
        for (OddsApiEvent oddsApiEvent : oddsApiEvents) {

            Event event = new Event(league, oddsApiEvent.homeTeam, oddsApiEvent.awayTeam, oddsApiEvent.commenceTime);
            oddList.addEvent(event);

            for (OddsApiEvent.Bookmaker oddsApiBookmaker : oddsApiEvent.bookmakers) {
                for (OddsApiEvent.Bookmaker.Market oddsApiMarket : oddsApiBookmaker.markets) {

                    for (OddsApiEvent.Bookmaker.Market.Outcome oddsApiOutcome : oddsApiMarket.outcomes) {

                        Outcome outcome;
                        if (Market.hasPlayer(market)) {
                            outcome = new Outcome(event, market, oddsApiOutcome.name, oddsApiOutcome.point, "TODO PROP",
                                    oddsApiOutcome.description);
                        } else if (Market.hasPoints(market)) {
                            outcome = new Outcome(event, market, oddsApiOutcome.name, oddsApiOutcome.point);
                        } else {
                            outcome = new Outcome(event, market, oddsApiOutcome.name);
                        }

                        if (!oddList.addOutcome(outcome)) {
                            outcome = oddList.getOutcome(outcome.hashCode());
                        }

                        Odd odd = new Odd(outcome, oddsApiBookmaker.key, oddsApiOutcome.price);
                        oddList.addOdd(odd);
                    }
                }
            }
        }
    }

    private class OddsApiEvent {
        @SerializedName("id")
        private String id;

        @SerializedName("sport_key")
        private String sportKey;

        @SerializedName("sport_title")
        private String sportTitle;

        @SerializedName("commence_time")
        private String commenceTime;

        @SerializedName("home_team")
        private String homeTeam;

        @SerializedName("away_team")
        private String awayTeam;

        @SerializedName("bookmakers")
        private List<Bookmaker> bookmakers;

        public class Bookmaker {
            @SerializedName("key")
            private String key;

            @SerializedName("title")
            private String title;

            @SerializedName("last_update")
            private String lastUpdate;

            @SerializedName("markets")
            private List<Market> markets;

            public class Market {
                @SerializedName("key")
                private String key;

                @SerializedName("last_update")
                private String lastUpdate;

                @SerializedName("outcomes")
                private List<Outcome> outcomes;

                public class Outcome {
                    @SerializedName("name")
                    private String name;

                    @SerializedName("price")
                    private int price;

                    @SerializedName("point")
                    private double point;

                    @SerializedName("description")
                    private String description;
                }
            }
        }
    }
}
