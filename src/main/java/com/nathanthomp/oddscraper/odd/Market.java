package com.nathanthomp.oddscraper.odd;

/*
 * All Market definitions go here.
 */
public enum Market {
    MONEYLINE(1),
    SPREAD(2),
    TOTAL(3),
    OUTRIGHT(4),
    PLAYER_PROP(5);

    private final int value;

    private Market(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Market[] getMarkets(League league) {
        Market[] markets;
        switch (league) {
            case UCL:
                markets = new Market[] { MONEYLINE, SPREAD, TOTAL };
                break;
            case NHL:
                markets = new Market[] { MONEYLINE, SPREAD, TOTAL };
                break;
            case NCAAB:
                markets = new Market[] { MONEYLINE, SPREAD, TOTAL };
                break;
            case MASTERS:
                markets = new Market[] { OUTRIGHT };
                break;
            case NFL:
                markets = new Market[] { MONEYLINE, SPREAD, TOTAL, PLAYER_PROP };
                break;
            default:
                // This cannot be accessed
                markets = new Market[] {};
                break;
        }
        return markets;
    }

    public static boolean hasPlayer(Market market) {
        boolean hasPlayer;
        switch (market) {
            case MONEYLINE:
                hasPlayer = false;
                break;
            case SPREAD:
                hasPlayer = true;
                break;
            case TOTAL:
                hasPlayer = true;
                break;
            case OUTRIGHT:
                hasPlayer = false;
                break;
            default:
                // This cannot be accessed
                hasPlayer = false;
                break;
        }
        return hasPlayer;
    }

    public static boolean hasPoints(Market market) {
        boolean hasPoints;
        switch (market) {
            case MONEYLINE:
                hasPoints = false;
                break;
            case SPREAD:
                hasPoints = true;
                break;
            case TOTAL:
                hasPoints = true;
                break;
            case OUTRIGHT:
                hasPoints = false;
                break;
            default:
                // This cannot be accessed
                hasPoints = false;
                break;
        }
        return hasPoints;
    }

    public static String convertToOddsApiMarket(Market market) {
        String oddsApiMarket;
        switch (market) {
            case MONEYLINE:
                oddsApiMarket = "h2h";
                break;
            case SPREAD:
                oddsApiMarket = "spreads";
                break;
            case TOTAL:
                oddsApiMarket = "totals";
                break;
            case OUTRIGHT:
                oddsApiMarket = "outrights";
                break;
            case PLAYER_PROP:
                oddsApiMarket = "TODO";
                break;
            default:
                oddsApiMarket = null;
                break;
        }
        return oddsApiMarket;
    }
}
