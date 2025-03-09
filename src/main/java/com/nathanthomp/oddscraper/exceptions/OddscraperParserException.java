package com.nathanthomp.oddscraper.exceptions;

// Oddscraper Error: Could not parse odds data.

public class OddscraperParserException extends OddscraperException {
    public OddscraperParserException() {
        super("Could not parse odds data.");
    }
}
