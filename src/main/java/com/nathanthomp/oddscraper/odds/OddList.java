package com.nathanthomp.oddscraper.odds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class OddList {

    private Map<OddEvent, Map<String, Set<Odd>>> rep;

    public OddList() {
        this.rep = new HashMap<OddEvent, Map<String, Set<Odd>>>();
    }

    public void addOdd(Odd odd) {
        /*
         * New event
         */
        if (!rep.containsKey(odd.getEvent())) {
            rep.put(odd.getEvent(), new HashMap<String, Set<Odd>>());
        }
        /*
         * New result
         */
        if (!rep.get(odd.getEvent()).containsKey(odd.getResult())) {
            rep.get(odd.getEvent()).put(odd.getResult(), new HashSet<Odd>());
        }
        /*
         * New Odd
         */
        if (!rep.get(odd.getEvent()).get(odd.getResult()).contains(odd)) {
            rep.get(odd.getEvent()).get(odd.getResult()).add(odd);
        }
    }

    public List<Odd> getOdds() {
        List<Odd> odds = new LinkedList<Odd>();
        for (Map.Entry<OddEvent, Map<String, Set<Odd>>> eventEntry : this.rep.entrySet()) {
            for (Map.Entry<String, Set<Odd>> resultEntry : eventEntry.getValue().entrySet()) {
                odds.addAll(resultEntry.getValue());
            }
        }
        return odds;
    }

    public OddList getBestOdds() {
        OddList bestOdds = new OddList();
        for (Map.Entry<OddEvent, Map<String, Set<Odd>>> eventEntry : this.rep.entrySet()) {
            for (Map.Entry<String, Set<Odd>> resultEntry : eventEntry.getValue().entrySet()) {
                /*
                 * Get odds with best price for result in event
                 */
                Map<Integer, Set<Odd>> bestOddsForResult = new HashMap<Integer, Set<Odd>>();
                int currentResultBestPrice = Integer.MIN_VALUE;
                for (Odd odd : resultEntry.getValue()) {
                    if (odd.getPrice() >= currentResultBestPrice) {
                        currentResultBestPrice = odd.getPrice();
                        if (bestOddsForResult.containsKey(currentResultBestPrice)) {
                            bestOddsForResult.get(currentResultBestPrice).add(odd);
                        } else {
                            Set<Odd> bestOddList = new HashSet<Odd>();
                            bestOddList.add(odd);
                            bestOddsForResult.put(currentResultBestPrice, bestOddList);
                        }
                    }
                }
                /*
                 * Add all best odds for result in event to the new OddList
                 */
                for (Odd odd : bestOddsForResult.get(currentResultBestPrice)) {
                    bestOdds.addOdd(odd);
                }
            }
        }
        return bestOdds;
    }

    /*
     * {\n
     * ____\"identifier\": \"value\"\n
     * }
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // stringBuilder.append(" \"event\": \""
        // + "Texas Tech Red Raiders vs UNC Wilmington Seahawks @ 2025-03-21T02:10:00Z"
        // + "\"\n");
        // stringBuilder.append(" \"result\": \"" + "UNC Wilmington Seahawks" + "\"\n");
        // stringBuilder.append(" \"price\": \"" + 1100 + "\"\n");
        // stringBuilder.append(" \"sportsbooks\": \"" + "draftkings" + "\"\n");
        // stringBuilder.append(" \"result\": \"" + "Texas Tech Red Raiders" + "\"\n");
        // stringBuilder.append(" \"price\": \"" + -1900 + "\"\n");
        // stringBuilder.append(" \"sportsbooks\": \"" + "lowvig, betus" + "\"\n");
        // stringBuilder.append(" \"event\": \""
        // + "Marquette Golden Eagles vs New Mexico Lobos @ 2025-03-21T23:25:00Z" +
        // "\"\n");
        // stringBuilder.append(" \"result\": \"" + "New Mexico Lobos" + "\"\n");
        // stringBuilder.append(" \"price\": \"" + 155 + "\"\n");
        // stringBuilder.append(" \"sportsbooks\": \"" + "lowvig, betonlineag" +
        // "\"\n");
        // stringBuilder.append(" \"result\": \"" + "Marquette Golden Eagles" + "\"\n");
        // stringBuilder.append(" \"price\": \"" + -175 + "\"\n");
        // stringBuilder.append(
        // " \"sportsbooks\": \"" + "draftkings, lowvig, betus, betonlineag, bovada" +
        // "\"\n");

        int currentEventIndex = 1;
        for (Map.Entry<OddEvent, Map<String, Set<Odd>>> eventEntry : this.rep.entrySet()) {
            stringBuilder.append("        {\n");
            stringBuilder.append("            \"event\": \"" + eventEntry.getKey() + "\",\n");
            stringBuilder.append("            \"results\": [\n");

            int currentResultIndex = 1;
            for (Map.Entry<String, Set<Odd>> resultEntry : eventEntry.getValue().entrySet()) {
                stringBuilder.append("                {\n");
                stringBuilder.append("                    \"name\": \"" + resultEntry.getKey() + "\",\n");
                Set<Odd> resultEntryValue = resultEntry.getValue();
                stringBuilder.append(
                        "                    \"price\": \"" + resultEntryValue.iterator().next().getPrice()
                                + "\",\n");
                String sportsbooks = "";
                for (Odd odd : resultEntryValue) {
                    sportsbooks += odd.getSportsbook() + " ";
                }
                stringBuilder.append("                    \"sportsbooks\": \"" + sportsbooks + "\"\n");
                stringBuilder.append("                }");

                if (currentResultIndex < eventEntry.getValue().size()) {
                    stringBuilder.append(",\n");
                } else {
                    stringBuilder.append("\n");
                }

                currentResultIndex++;
            }
            stringBuilder.append("            ]\n");
            stringBuilder.append("        }");

            if (currentEventIndex < this.rep.entrySet().size()) {
                stringBuilder.append(",\n");
            } else {
                stringBuilder.append("\n");
            }

            currentEventIndex++;
        }

        return stringBuilder.toString();
    }
}
