package com.nathanthomp.oddscraper.exceptions;

public abstract class OddscraperException extends Exception {
    public OddscraperException(String message) {
        super("Oddscraper Error: " + message);
    }
}
