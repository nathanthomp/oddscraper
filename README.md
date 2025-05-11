# Oddscraper - AWS Cloud Native Application

This project was built to enable the automation and visability of sports betting data across sportsbooks.

### Table of Contents

-   [Usage](#usage)
-   [Terms and Concepts](#terms-and-concepts)
-   [Entity Chart](#entity-chart)
-   [Entity Access Patterns](#entity-access-patterns)

## Usage

Oddscraper infrastructure is managed in AWS and deployed by OpenTofu. The OpenTofu configuration are located in the `/infrastructure` directory. To deploy this project, run the following command in the `/infrastructure` directory:

```bash
tofu apply
```

The deployment operations for Oddscraper will soon be migrated to a CD pipeline using _GitHub Actions_.

## Terms and Concepts

Oddscraper is a cloud native application that scrapes real time sportsbetting odds from the internet and writes those odds to a NoSQL database. The entities that will be used in this application include sport leagues, events, outcomes, and odds. The relationship between these entities are discussed further below.

-   A League represents a sport league. These leagues can include the National Football League (NFL), National Hockey League (NHL), National Basketball Association (NBA), National Collegiate Athletic Association Football (NCAAF), etc.

-   An Event represents a specific competition within a league. These events are uniquely identified by their participants and the time/date of the competition.

-   An Outcome represents a result from the conclusion of an event. Outcomes can be drawn from different sportsbetting markets such as Moneylines, Spreads, Totals, Player Props, and Outrights.

-   An Odd represents a sportsbook's price for an outcome. Sportsbooks can include FanDuel, DraftKings, ESPN Bet, Bet365, etc. Prices that these sportsbooks set on an outcome are in american odds (ex. -110, +240)

## Entity Chart

### OddsTable Version 5

| Entity     | PK                                                       | SK                                                           |
| :--------- | :------------------------------------------------------- | :----------------------------------------------------------- |
| Odd        | LEAGUE#`<LEAGUEID>`#EVENT#`<EVENTID>`#OUTCOME#`<MARKET>` | ODD#`<RESULT>`#`<SPORTSBOOK>`                                |
| Point Odd  | LEAGUE#`<LEAGUEID>`#EVENT#`<EVENTID>`#OUTCOME#`<MARKET>` | ODD#`<RESULT>`#`<POINTS>`#`<SPORTSBOOK>`                     |
| Player Odd | LEAGUE#`<LEAGUEID>`#EVENT#`<EVENTID>`#OUTCOME#`<MARKET>` | ODD#`<RESULT>`#`<POINTS>`#`<PROP>`#`<PLAYER>`#`<SPORTSBOOK>` |

### OddsTable Version 4

| Entity     | PK                                                       | SK                                                                     |
| :--------- | :------------------------------------------------------- | :--------------------------------------------------------------------- |
| Odd        | LEAGUE#`<LEAGUEID>`#EVENT#`<EVENTID>`#OUTCOME#`<MARKET>` | ODD#`<RESULT>`#`<PRICE>`#`<SPORTSBOOK>`                                |
| Point Odd  | LEAGUE#`<LEAGUEID>`#EVENT#`<EVENTID>`#OUTCOME#`<MARKET>` | ODD#`<RESULT>`#`<POINTS>`#`<PRICE>`#`<SPORTSBOOK>`                     |
| Player Odd | LEAGUE#`<LEAGUEID>`#EVENT#`<EVENTID>`#OUTCOME#`<MARKET>` | ODD#`<RESULT>`#`<POINTS>`#`<PROP>`#`<PLAYER>`#`<PRICE>`#`<SPORTSBOOK>` |

### OddsTable Version 3

| Entity  | PK                               | SK                               | TYPE    | ATTRIBUTES           | TTL         |
| :------ | :------------------------------- | :------------------------------- | :------ | :------------------- | ----------- |
| Event   | LEAGUE#\<LeagueValue>            | EVENT#STARTDATETIME#\<EventHash> | EVENT   | `{ "key": "value" }` | ENDDATETIME |
| Outcome | EVENT#STARTDATETIME#\<EventHash> | OUTCOME#MARKET#\<OutcomeHash>    | OUTCOME | `{ "key": "value" }` | ENDDATETIME |
| Odd     | OUTCOME#MARKET#\<OutcomeHash>    | ODD#SPORTSBOOK#\<OddHash>        | ODD     | `{ "key": "value" }` | ENDDATETIME |

### OddsTable Version 2

| Entity  | PK                               | SK                               | TYPE    | ATTRIBUTES           | TTL         |
| :------ | :------------------------------- | :------------------------------- | :------ | :------------------- | ----------- |
| Event   | EVENT#STARTDATETIME#\<EventHash> | LEAGUE#\<LeagueValue>            | EVENT   | `{ "key": "value" }` | ENDDATETIME |
| Outcome | OUTCOME#MARKET#\<OutcomeHash>    | EVENT#STARTDATETIME#\<EventHash> | OUTCOME | `{ "key": "value" }` | ENDDATETIME |
| Odd     | ODD#SPORTSBOOK                   | OUTCOME#MARKET#\<OutcomeHash>    | ODD     | `{ "key": "value" }` | ENDDATETIME |

### OddsTable Version 1

| Entity  | PK                               | SK                               | TYPE    | ATTRIBUTES           | TTL         |
| :------ | :------------------------------- | :------------------------------- | :------ | :------------------- | ----------- |
| Event   | LEAGUE#\<LeagueValue>            | EVENT#STARTDATETIME#\<EventHash> | EVENT   | `{ "key": "value" }` | ENDDATETIME |
| Outcome | EVENT#STARTDATETIME#\<EventHash> | OUTCOME#MARKET#\<OutcomeHash>    | OUTCOME | `{ "key": "value" }` | ENDDATETIME |
| Odd     | OUTCOME#MARKET#\<OutcomeHash>    | ODD#SPORTSBOOK                   | ODD     | `{ "key": "value" }` | ENDDATETIME |

## Entity Access Patterns

| Access Pattern                            | Target     | Parameters | Returns              |
| :---------------------------------------- | :--------- | :--------- | :------------------- |
| Get Events by League Sorted by Start Time | Main Table | League     | SortedList\<Event>   |
| Get Outcomes by Event Sorted by Market    | Main Table | Event      | SortedList\<Outcome> |
| Get Odds by Outcome Sorted by Price       | Main Table | Outcome    | SortedList\<Odd>     |
| Get Best Price for Odd in Outcome         | Main Table | Outcome    | SortedList\<Odd>     |
