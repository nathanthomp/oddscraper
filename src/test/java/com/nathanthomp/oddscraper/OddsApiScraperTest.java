package com.nathanthomp.oddscraper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.nathanthomp.oddscraper.exceptions.OddscraperException;
import com.nathanthomp.oddscraper.exceptions.OddscraperParserException;
import com.nathanthomp.oddscraper.odds.Odd;
import com.nathanthomp.oddscraper.scrapers.OddsApiScraper;

public class OddsApiScraperTest {

    @Test
    public void testGetOdds_NotEmpty() {

        List<Odd> odds = new LinkedList<Odd>();
        try {
            OddsApiScraper oddsApiScraper = new OddsApiScraper();
            List<Odd> oddsFromFile = oddsApiScraper.getOddsFromFile("soccer_uefa_champs_league");
            odds.addAll(oddsFromFile);
        } catch (OddscraperParserException e) {
            // TODO: handle exception
        } catch (OddscraperException e) {
            // TODO: handle exception
        } catch (Exception e) {
            // TODO: handle exception
        }

        assertTrue(!odds.isEmpty());
    }

    @Test
    public void testGetOdds_Odds() {

        List<Odd> odds = new LinkedList<Odd>();
        try {
            OddsApiScraper oddsApiScraper = new OddsApiScraper();
            List<Odd> oddsFromFile = oddsApiScraper.getOddsFromFile("soccer_uefa_champs_league");
            odds.addAll(oddsFromFile);
        } catch (OddscraperParserException e) {
            // TODO: handle exception
        } catch (OddscraperException e) {
            // TODO: handle exception
        } catch (Exception e) {
            // TODO: handle exception
        }

        for (Odd odd : odds) {

        }

        assertTrue(!odds.isEmpty());
    }
}
