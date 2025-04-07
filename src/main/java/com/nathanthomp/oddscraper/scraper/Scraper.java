package com.nathanthomp.oddscraper.scrapers;

import java.util.Set;

import com.nathanthomp.oddscraper.odds.OddEvent;
import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddMarket;

public abstract class Scraper {
    protected OddLeague league;
    protected OddMarket market;

    public Scraper(OddLeague league, OddMarket market) {
        this.league = league;
        this.market = market;
    }

    abstract Set<OddEvent> scrapeOdds() throws Exception;
}
