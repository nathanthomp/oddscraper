package com.nathanthomp.oddscraper.scrapers;

import com.nathanthomp.oddscraper.odds.OddLeague;
import com.nathanthomp.oddscraper.odds.OddList;
import com.nathanthomp.oddscraper.odds.OddMarket;

public abstract class Scraper {
    protected OddLeague league;
    protected OddMarket market;

    public Scraper(OddLeague league, OddMarket market) {
        this.league = league;
        this.market = market;
    }

    abstract void scrapeOdds(OddList odds) throws Exception;
}
