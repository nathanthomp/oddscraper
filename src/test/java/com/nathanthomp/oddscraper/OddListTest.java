package com.nathanthomp.oddscraper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.nathanthomp.oddscraper.odd.Odd;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddList;
import com.nathanthomp.oddscraper.odd.OddMarket;
import com.nathanthomp.oddscraper.scraper.OddsApiScraper;

public class OddListTest {

    @Test
    public void testAddOdd_NotEmpty() {

        // String path = "data/odds-api-response-odds-cbb-moneyline.json";
        // OddList oddList = new OddList();
        // List<Odd> odds;
        // try {
        // odds = OddsApiScraper.scrapeOddsFromFile(path, OddLeague.CBB,
        // OddMarket.MONEYLINE);
        // } catch (Exception e) {
        // fail("Could not get data from file: " + e.getMessage());
        // return;
        // }

        // for (Odd odd : odds) {
        // oddList.addOdd(odd);
        // }

        // assertTrue(!oddList.getOdds().isEmpty());
    }

    @Test
    public void testGetBestOdds_NotEmpty() {

        // String path = "data/odds-api-response-odds-cbb-moneyline.json";
        // OddList oddList = new OddList();
        // List<Odd> odds;
        // try {
        // odds = OddsApiScraper.scrapeOddsFromFile(path, OddLeague.CBB,
        // OddMarket.MONEYLINE);
        // } catch (Exception e) {
        // fail("Could not get data from file: " + e.getMessage());
        // return;
        // }

        // for (Odd odd : odds) {
        // oddList.addOdd(odd);
        // }

        // OddList bestOdds = oddList.getBestOdds();

        // assertTrue(!bestOdds.getOdds().isEmpty());
    }

    @Test
    public void testToString_NotEmpty() {

        // String path = "data/odds-api-response-odds-cbb-moneyline.json";
        // OddList oddList = new OddList();
        // List<Odd> odds;
        // try {
        // odds = OddsApiScraper.scrapeOddsFromFile(path, OddLeague.CBB,
        // OddMarket.MONEYLINE);
        // } catch (Exception e) {
        // fail("Could not get data from file: " + e.getMessage());
        // return;
        // }

        // for (Odd odd : odds) {
        // oddList.addOdd(odd);
        // }

        // OddList bestOdds = oddList.getBestOdds();
        // String bestOddsToString = bestOdds.toString();

        // assertTrue(!bestOddsToString.isEmpty());
    }
}
