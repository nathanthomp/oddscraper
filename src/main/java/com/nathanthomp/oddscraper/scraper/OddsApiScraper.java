package com.nathanthomp.oddscraper.scraper;

import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.nathanthomp.oddscraper.odd.Odd;
import com.nathanthomp.oddscraper.odd.OddEvent;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddMarket;
import com.nathanthomp.oddscraper.odd.OddOutcome;

public class OddsApiScraper extends Scraper {
    /*
     * Base endpoint for OddsApi
     */
    private static final String ODDS_API_HOST = "https://api.the-odds-api.com";
    /*
     * Valid OddLeagues and their matching OddsApi leagues
     */
    private static final Map<OddLeague, String> oddsApiLeagues = Map.ofEntries(
            Map.entry(OddLeague.UCL, "soccer_uefa_champs_league"),
            Map.entry(OddLeague.NHL, "icehockey_nhl"),
            Map.entry(OddLeague.CBB, "basketball_ncaab"),
            Map.entry(OddLeague.MASTERS, "golf_masters_tournament_winner"));
    /*
     * Valid OddMarkets and their matching OddsApi markets
     */
    private static final Map<OddMarket, String> oddsApiMarkets = Map.ofEntries(
            Map.entry(OddMarket.MONEYLINE, "h2h"),
            Map.entry(OddMarket.SPREAD, "spreads"),
            Map.entry(OddMarket.TOTAL, "totals"),
            Map.entry(OddMarket.OUTRIGHT, "outrights"));
    /*
     * Converted OddLeague value
     */
    private String oddsApiLeague;

    public OddsApiScraper(OddLeague league) throws Exception {
        super(league);
        if (!oddsApiLeagues.containsKey(league)) {
            throw new Exception("Cannot convert OddLeague to OddsApiLeague");
        }
        this.oddsApiLeague = oddsApiLeagues.get(league);
    }

    public Set<OddEvent> scrapeOdds(OddMarket market) throws Exception {
        /*
         * Get OddsApi data
         */
        String data = getOddsApiData(market);
        /*
         * Parse OddsApi data
         */
        Set<OddEvent> events = parseOddsApiData(data, market);
        return events;
    }

    private String getOddsApiData(OddMarket market) throws Exception {
        /*
         * Get OddsApi market
         */
        if (!oddsApiMarkets.containsKey(market)) {
            throw new Exception("Cannot convert OddMarket to OddsApiMarket");
        }
        String oddsApiMarket = oddsApiMarkets.get(market);
        /*
         * Get OddsApi endpoint
         */
        String oddsApiKey = getOddsApiKey();
        String endpoint = getOddsApiOddsEndpoint(oddsApiKey, oddsApiMarket);
        /*
         * Call OddsApi
         */
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
        return response.body();
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

    private String getOddsApiOddsEndpoint(String oddsApiKey, String oddsApiMarket) {
        return ODDS_API_HOST + "/v4/sports/" + this.oddsApiLeague + "/odds?apiKey=" + oddsApiKey
                + "&regions=us&markets=" + oddsApiMarket + "&dateFormat=iso&oddsFormat=american";
    }

    private Set<OddEvent> parseOddsApiData(String data, OddMarket market) {
        Gson gson = new Gson();
        /*
         * Convert array of OddApiEvent to Set of OddEvent
         */
        OddsApiEvent[] oddsApiEvents = gson.fromJson(data, OddsApiEvent[].class);
        return convert(oddsApiEvents, super.league, market);
    }

    private static Set<OddEvent> convert(OddsApiEvent[] oddsApiEvents, OddLeague league, OddMarket market) {
        Set<OddEvent> events = new HashSet<OddEvent>();

        for (OddsApiEvent oddsApiEvent : oddsApiEvents) {
            /*
             * Get metadata including participants and time
             */
            OddEvent event = new OddEvent(league, oddsApiEvent.homeTeam, oddsApiEvent.awayTeam,
                    oddsApiEvent.commenceTime);

            for (OddsApiEvent.Bookmaker oddsApiBookmaker : oddsApiEvent.bookmakers) {
                /*
                 * Get metadata including key
                 */
                for (OddsApiEvent.Bookmaker.Market oddsApiMarket : oddsApiBookmaker.markets) {
                    for (OddsApiEvent.Bookmaker.Market.Outcome oddsApiOutcome : oddsApiMarket.outcomes) {
                        /*
                         * Get metadata including name (and point if spread/total)
                         */
                        OddOutcome outcome;
                        if (market == OddMarket.MONEYLINE || market == OddMarket.OUTRIGHT) {
                            outcome = new OddOutcome(oddsApiOutcome.name, market);
                        } else {
                            outcome = new OddOutcome(oddsApiOutcome.name, market, oddsApiOutcome.point);
                        }
                        /*
                         * Get metadata including price
                         */
                        Odd odd = new Odd(oddsApiBookmaker.key, oddsApiOutcome.price);
                        /*
                         * If outcome exists, add the odd to the existing outcome
                         */
                        OddOutcome foundOutcome = null;
                        for (OddOutcome existingOutcome : event.getOutcomes()) {
                            if (existingOutcome.equals(outcome)) {
                                existingOutcome.getOdds().add(odd);
                                continue;
                            }
                        }
                        /*
                         * If outcome does not exist, add the odd to the new outcome and add the new
                         * outcome to the event
                         */
                        if (foundOutcome == null) {
                            outcome.getOdds().add(odd);
                            event.getOutcomes().add(outcome);
                        }
                    }
                }
            }

            events.add(event);
        }

        return events;
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
                }
            }
        }
    }

    /*
     * For testing
     */
    public static Set<OddEvent> scrapeOddsFromFile(String path, OddLeague league, OddMarket market) throws Exception {
        /*
         * Get json data from file
         */
        String data = new String(Files.readAllBytes(Paths.get(path)));
        Gson gson = new Gson();

        /*
         * Convert array of OddApiEvent to Set of OddEvent
         */
        OddsApiEvent[] oddsApiEvents = gson.fromJson(data, OddsApiEvent[].class);
        Set<OddEvent> events = convert(oddsApiEvents, league, market);

        return events;
    }
}
