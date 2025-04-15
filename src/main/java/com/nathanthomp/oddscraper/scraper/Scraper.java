package com.nathanthomp.oddscraper.scraper;

import java.util.Set;

import com.nathanthomp.oddscraper.odd.OddEvent;
import com.nathanthomp.oddscraper.odd.OddLeague;
import com.nathanthomp.oddscraper.odd.OddMarket;

public abstract class Scraper {
    protected OddLeague league;

    public Scraper(OddLeague league) {
        this.league = league;
    }

    abstract Set<OddEvent> scrapeOdds(OddMarket market) throws Exception;
}
