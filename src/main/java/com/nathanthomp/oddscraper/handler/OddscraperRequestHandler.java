package com.nathanthomp.oddscraper.handler;

import java.util.Collection;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nathanthomp.oddscraper.odd.OddList;
import com.nathanthomp.oddscraper.odd.Outcome;
import com.nathanthomp.oddscraper.odd.Entity;
import com.nathanthomp.oddscraper.odd.Event;
import com.nathanthomp.oddscraper.odd.League;
import com.nathanthomp.oddscraper.odd.Market;
import com.nathanthomp.oddscraper.odd.Odd;
import com.nathanthomp.oddscraper.scraper.OddsApiScraper;

/*
 * NEW STEPS:
 * 1. Get active leagues
 * 2. for each active leauge, get valid markets
 * 3. for each market in active leauge, get odds
 * 3.1 if get odds fails, log warning and continue
 */

/**
 * Lambda handler for scraping sports betting odds from sportsbooks and writing
 * odds to a database.
 */
public class OddscraperRequestHandler implements RequestHandler<OddscraperRequest, OddscraperResponse> {

    private static League[] activeLeagues = new League[] {};

    @Override
    public OddscraperResponse handleRequest(OddscraperRequest request, Context context) {
        try {
            OddList oddList = new OddList();
            // try {
            // OddsApiScraper oddsApiScraper = new OddsApiScraper();
            // for (League league : activeLeagues) {
            // Market[] markets = Market.getMarkets(league);
            // for (Market market : markets) {
            // try {
            // oddsApiScraper.scrapeOdds(league, market, oddList);
            // } catch (Exception e) {
            // // Log scrape warning
            // }
            // }
            // if (markets.length == 0) {
            // // Log market warning
            // }
            // }
            // } catch (Exception e) {
            // // Log scraper error
            // }

            Event event = new Event(League.NFL, "team1", "team2", "startDateTime");
            oddList.addEvent(event);

            Outcome outcome1 = new Outcome(event, Market.MONEYLINE, "team1");
            oddList.addOutcome(outcome1);

            Odd odd1 = new Odd(outcome1, "sportsbook1", 110);
            Odd odd2 = new Odd(outcome1, "sportsbook2", 120);
            Odd odd3 = new Odd(outcome1, "sportsbook3", 130);
            oddList.addOdd(odd1);
            oddList.addOdd(odd2);
            oddList.addOdd(odd3);

            Outcome outcome2 = new Outcome(event, Market.MONEYLINE, "team2");
            oddList.addOutcome(outcome2);

            Odd odd4 = new Odd(outcome2, "sportsbook1", -210);
            Odd odd5 = new Odd(outcome2, "sportsbook2", -220);
            Odd odd6 = new Odd(outcome2, "sportsbook3", -230);
            oddList.addOdd(odd4);
            oddList.addOdd(odd5);
            oddList.addOdd(odd6);

            try {
                writeToDatabase(oddList.getEntites());
            } catch (Exception e) {
                // Log database error
            }
        } catch (Exception e) {
            return new OddscraperResponse("failure", "could not scrape odds: " + e.getMessage());
        }

        return new OddscraperResponse("success", "odds scraped and written");
    }

    private void writeToDatabase(Collection<Entity> entities) {
        // List<WriteRequest> writeRequests = new ArrayList<>();
        /*
         * For each event in oddList, add to events table referencing league
         * For each outcome in oddList, add to outcomes table referencing event
         * For each odd in oddList, add to odds table referencing outcome
         */

        /*
         * Odds must be written after all Events and Outcomes
         */
        DynamoClient dynamoClient = DynamoClient.getInstance();
        for (Entity entity : entities) {
            dynamoClient.put(entity);
        }

        /*
         * Could be oddList.getItems()
         * or
         * Could be oddList.getItemsForEvent(Event event)
         */

        // Collection<Event> events = oddList.getEvents();

        // for (Event event : events) {
        // Collection<Entity> entities = oddList.getEntitiesForEvent(event);

        // }

        // for (Event event : events) {
        // dynamoClient.putItem(event.toItem());
        // }

        // Collection<Outcome> outcomes = oddList.getOutcomes();

        // for (Outcome outcome : outcomes) {
        // dynamoClient.putItem(outcome.toItem());
        // }

        // Collection<Odd> odds = oddList.getOdds();

        // for (Odd odd : odds) {
        // dynamoClient.putItem(odd.toItem());
        // }

    }
}
