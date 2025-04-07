package com.nathanthomp.oddscraper.scraper;

import java.util.Set;

import com.nathanthomp.oddscraper.odd.OddEvent;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddMarket;

public abstract class Scraper {
    protected OddLeague league;
    protected OddMarket market;

    public Scraper(OddLeague league, OddMarket market) {
        this.league = league;
        this.market = market;
    }

    abstract Set<OddEvent> scrapeOdds() throws Exception;
}
