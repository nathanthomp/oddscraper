package com.nathanthomp.oddscraper.scrapers;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.nathanthomp.oddscraper.odds.Odd;
import com.nathanthomp.oddscraper.odds.OddEvent;
import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddList;
import com.nathanthomp.oddscraper.odds.OddMarket;

public class OddsApiScraper extends Scraper {
    private final String ODDS_API_HOST = "https://api.the-odds-api.com";

    private String oddsApiLeague;
    private String oddsApiMarket;

    public OddsApiScraper(OddLeague league, OddMarket market) throws Exception {
        super(league, market);

        switch (league) {
            case ODDLEAGUE_UCL:
                this.oddsApiLeague = "soccer_uefa_champs_league";
                break;
            case ODDLEAGUE_NHL:
                this.oddsApiLeague = "icehockey_nhl";
                break;
            case ODDLEAGUE_CBB:
                this.oddsApiLeague = "basketball_ncaab";
                break;
            default:
                throw new Exception("Cannot convert OddLeague to OddsApiLeague");
        }

        switch (market) {
            case ODDMARKET_MONEYLINE:
                this.oddsApiMarket = "h2h";
                break;
            case ODDMARKET_SPREAD:
                this.oddsApiMarket = "spreads";
                break;
            case ODDMARKET_TOTAL:
                this.oddsApiMarket = "totals";
                break;
            default:
                throw new Exception("Cannot convert OddMarket to OddsApiMarket");
        }
    }

    public void scrapeOdds(OddList odds) throws Exception {
        String oddsApiKey = getOddsApiKey();
        String endpoint = getOddsApiOddsEndpoint(oddsApiKey);

        /*
         * Call odds api
         */
        ScraperHttpClient httpClient = ScraperHttpClient.getInstance();
        HttpResponse<String> response = httpClient.getHttpResponse(endpoint);

        /*
         * Potential status codes: 401, 422, 429, 500
         */
        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new Exception("Could not get OddsApi data: " + response.body());
        }

        String body = response.body();

        /*
         * TODO: Log raw odds api data to aws
         */
        // writeOddsApiResponseBodyToFile(body);

        parseOddsApiData(body, odds);
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

    private String getOddsApiOddsEndpoint(String oddsApiKey) {
        return ODDS_API_HOST + "/v4/sports/" + this.oddsApiLeague + "/odds?apiKey=" + oddsApiKey
                + "&regions=us&markets=" + this.oddsApiMarket + "&dateFormat=iso&oddsFormat=american";
    }

    private void parseOddsApiData(String data, OddList odds) throws Exception {
        Gson gson = new Gson();
        OddsApiEvent[] events = gson.fromJson(data, OddsApiEvent[].class);

        for (OddsApiEvent oddsApiEvent : events) {
            String homeTeam = oddsApiEvent.getHomeTeam();
            String awayTeam = oddsApiEvent.getAwayTeam();
            String commence_time = oddsApiEvent.getCommenceTime();

            OddEvent event = new OddEvent(homeTeam, awayTeam, commence_time);

            for (OddsApiEvent.Bookmaker bookmaker : oddsApiEvent.getBookmakers()) {
                String sportsbook = bookmaker.getKey();

                for (OddsApiEvent.Bookmaker.Market oddsApiMarket : bookmaker.getMarkets()) {

                    for (OddsApiEvent.Bookmaker.Market.Outcome outcome : oddsApiMarket.getOutcomes()) {
                        String result = outcome.getName();
                        int price = outcome.getPrice();

                        Odd odd = new Odd(super.league, event, sportsbook, super.market, result, price);
                        odds.addOdd(odd);
                    }
                }
            }
        }
    }

    public void writeOddsApiResponseBodyToFile(String odds) {
        LocalDateTime time = LocalDateTime.now();
        String path = "data/odds-api-response-" + super.league + "-" + super.market + "-" + time.getMonthValue()
                + "-" + time.getDayOfMonth() + "-" + time.getYear() + ".json";
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(odds);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * For testing
     */
    public static List<Odd> scrapeOddsFromFile(String path, OddLeague league, OddMarket market) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(path)));

        Gson gson = new Gson();
        OddsApiEvent[] events = gson.fromJson(data, OddsApiEvent[].class);

        List<Odd> odds = new LinkedList<Odd>();
        for (OddsApiEvent oddsApiEvent : events) {
            String homeTeam = oddsApiEvent.getHomeTeam();
            String awayTeam = oddsApiEvent.getAwayTeam();
            String commence_time = oddsApiEvent.getCommenceTime();

            OddEvent event = new OddEvent(homeTeam, awayTeam, commence_time);

            for (OddsApiEvent.Bookmaker bookmaker : oddsApiEvent.getBookmakers()) {
                String sportsbook = bookmaker.getKey();

                for (OddsApiEvent.Bookmaker.Market oddsApiMarket : bookmaker.getMarkets()) {

                    for (OddsApiEvent.Bookmaker.Market.Outcome outcome : oddsApiMarket.getOutcomes()) {
                        String result = outcome.getName();
                        int price = outcome.getPrice();

                        Odd odd = new Odd(league, event, sportsbook, market, result, price);
                        odds.add(odd);
                    }
                }
            }
        }

        return odds;
    }
}
