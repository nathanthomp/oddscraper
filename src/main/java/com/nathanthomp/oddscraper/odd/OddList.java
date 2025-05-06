package com.nathanthomp.oddscraper.odd;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OddList {
    private Map<Integer, Event> eventsMap;
    private Map<Integer, Outcome> outcomesMap;
    private Map<Integer, Odd> oddsMap;

    public OddList() {
        this.eventsMap = new HashMap<Integer, Event>();
        this.outcomesMap = new HashMap<Integer, Outcome>();
        this.oddsMap = new HashMap<Integer, Odd>();
    }

    public boolean addEvent(Event event) {
        int newEventHashCode = event.hashCode();
        if (eventsMap.containsKey(newEventHashCode)) {
            return false;
        }

        eventsMap.put(newEventHashCode, event);
        return true;
    }

    public Collection<Event> getEvents() {
        return this.eventsMap.values();
    }

    public boolean addOutcome(Outcome outcome) {
        int newOutcomeHashCode = outcome.hashCode();
        if (eventsMap.containsKey(newOutcomeHashCode)) {
            return false;
        }

        outcomesMap.put(newOutcomeHashCode, outcome);
        return true;
    }

    public Collection<Outcome> getOutcomes() {
        return this.outcomesMap.values();
    }

    public Outcome getOutcome(int hashCode) {
        return outcomesMap.get(hashCode);
    }

    public boolean addOdd(Odd odd) {
        int newOddHashCode = odd.hashCode();
        if (eventsMap.containsKey(newOddHashCode)) {
            return false;
        }

        oddsMap.put(newOddHashCode, odd);
        return true;
    }

    public Collection<Odd> getOdds() {
        return this.oddsMap.values();
    }

    // public Collection<Entity> getEntites() {
    // Collection<Entity> entities = new LinkedList<Entity>();
    // entities.addAll(eventsMap.values());
    // entities.addAll(outcomesMap.values());
    // entities.addAll(oddsMap.values());
    // return entities;
    // }

    public Collection<Entity> getOddEntites() {
        Collection<Entity> entities = new LinkedList<Entity>();
        entities.addAll(oddsMap.values());
        return entities;
    }

    // public void addOdd(Odd odd) {
    // /*
    // * New event
    // */
    // if (!rep.containsKey(odd.getEvent())) {
    // rep.put(odd.getEvent(), new HashMap<String, Set<Odd>>());
    // }
    // /*
    // * New result
    // */
    // if (!rep.get(odd.getEvent()).containsKey(odd.getResult())) {
    // rep.get(odd.getEvent()).put(odd.getResult(), new HashSet<Odd>());
    // }
    // /*
    // * New Odd
    // */
    // if (!rep.get(odd.getEvent()).get(odd.getResult()).contains(odd)) {
    // rep.get(odd.getEvent()).get(odd.getResult()).add(odd);
    // }
    // }

    // public List<Odd> getOdds() {
    // List<Odd> odds = new LinkedList<Odd>();
    // for (Map.Entry<Event, Map<String, Set<Odd>>> eventEntry :
    // this.rep.entrySet()) {
    // for (Map.Entry<String, Set<Odd>> resultEntry :
    // eventEntry.getValue().entrySet()) {
    // odds.addAll(resultEntry.getValue());
    // }
    // }
    // return odds;
    // }

    // public OddList getBestOdds() {
    // OddList bestOdds = new OddList();
    // for (Map.Entry<Event, Map<String, Set<Odd>>> eventEntry :
    // this.rep.entrySet()) {
    // for (Map.Entry<String, Set<Odd>> resultEntry :
    // eventEntry.getValue().entrySet()) {
    // /*
    // * Get odds with best price for result in event
    // */
    // Map<Integer, Set<Odd>> bestOddsForResult = new HashMap<Integer, Set<Odd>>();
    // int currentResultBestPrice = Integer.MIN_VALUE;
    // for (Odd odd : resultEntry.getValue()) {
    // if (odd.getPrice() >= currentResultBestPrice) {
    // currentResultBestPrice = odd.getPrice();
    // if (bestOddsForResult.containsKey(currentResultBestPrice)) {
    // bestOddsForResult.get(currentResultBestPrice).add(odd);
    // } else {
    // Set<Odd> bestOddList = new HashSet<Odd>();
    // bestOddList.add(odd);
    // bestOddsForResult.put(currentResultBestPrice, bestOddList);
    // }
    // }
    // }
    // /*
    // * Add all best odds for result in event to the new OddList
    // */
    // for (Odd odd : bestOddsForResult.get(currentResultBestPrice)) {
    // bestOdds.addOdd(odd);
    // }
    // }
    // }
    // return bestOdds;
    // }
}
