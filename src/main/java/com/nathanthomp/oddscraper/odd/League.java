package com.nathanthomp.oddscraper.odd;

/*
 * All League definitions go here.
 */
public enum League {
    UCL(1),
    NHL(2),
    NCAAB(3),
    MASTERS(4),
    NFL(5);

    private final int value;

    private League(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String convertToOddsApiLeauge(League league) {
        String oddsApiLeague;
        switch (league) {
            case UCL:
                oddsApiLeague = "soccer_uefa_champs_league";
                break;
            case NHL:
                oddsApiLeague = "icehockey_nhl";
                break;
            case NCAAB:
                oddsApiLeague = "basketball_ncaab";
                break;
            case MASTERS:
                oddsApiLeague = "golf_masters_tournament_winner";
                break;
            case NFL:
                oddsApiLeague = "americanfootball_nfl";
                break;
            default:
                oddsApiLeague = null;
                break;
        }
        return oddsApiLeague;
    }
}
