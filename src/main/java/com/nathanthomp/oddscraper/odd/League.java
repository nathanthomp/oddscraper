package com.nathanthomp.oddscraper.odd;

/*
 * All League definitions go here.
 */
public enum League {
    UCL,
    NHL,
    NCAAB,
    MASTERS,
    NFL;

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
