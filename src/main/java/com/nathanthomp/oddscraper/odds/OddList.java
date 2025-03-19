package com.nathanthomp.oddscraper.odds;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OddList {

    private Map<OddEvent, Map<String, Set<Odd>>> rep;

    public OddList() {
        this.rep = new HashMap<OddEvent, Map<String, Set<Odd>>>();
    }

    public void addOdd(Odd odd) {
        /*
         * 1. If new event: Add event entry
         * 2. If new result: Get event entry, add result entry
         * 3. If new odd: Get event entry, get result entry, add odd
         */

        // New event
        if (!rep.containsKey(odd.getEvent())) {
            rep.put(odd.getEvent(), new HashMap<String, Set<Odd>>());
        }

        // If new result
        if (!rep.get(odd.getEvent()).containsKey(odd.getResult())) {
            rep.get(odd.getEvent()).put(odd.getResult(), new HashSet<Odd>());
        }

        // If new odd
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

    @Override
    public String toString() {
        Gson gson = new Gson();
        Type listType = new TypeToken<Map<OddEvent, Map<String, Set<Odd>>>>() {
        }.getType();
        String json = gson.toJson(this.rep, listType);
        return json;
    }
}
