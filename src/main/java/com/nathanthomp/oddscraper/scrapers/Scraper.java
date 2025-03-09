package com.nathanthomp.oddscraper.scrapers;

import java.util.List;

import com.nathanthomp.oddscraper.exceptions.OddscraperException;
import com.nathanthomp.oddscraper.odds.Odd;

public interface Scraper {
    List<Odd> scrapeOdds(String league) throws OddscraperException;
}
