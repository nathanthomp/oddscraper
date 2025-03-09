package com.nathanthomp.oddscraper.exceptions;

// Oddscraper Error: Could not make request for odds data.

public class OddscraperHttpRequestException extends OddscraperException {
    public OddscraperHttpRequestException() {
        super("Could not make request for odds data.");
    }
}
