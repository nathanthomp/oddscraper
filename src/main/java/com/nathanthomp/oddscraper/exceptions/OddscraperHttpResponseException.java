package com.nathanthomp.oddscraper.exceptions;

// Oddscraper Error: Could not get response for odds data.

public class OddscraperHttpResponseException extends OddscraperException {
    public OddscraperHttpResponseException() {
        super("Could not get response for odds data.");
    }
}
