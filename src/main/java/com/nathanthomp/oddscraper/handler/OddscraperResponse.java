package com.nathanthomp.oddscraper.handler;

/*
 * Outgoing response from AWS lambda, returning a status and message if neccesary.
 */
public record OddscraperResponse(String status, String message) {
}
