package com.nathanthomp.oddscraper.scrapers;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OddsApiEvent {
    @SerializedName("id")
    private String id;

    @SerializedName("sport_key")
    private String sportKey;

    @SerializedName("sport_title")
    private String sportTitle;

    @SerializedName("commence_time")
    private String commenceTime;

    @SerializedName("home_team")
    private String homeTeam;

    @SerializedName("away_team")
    private String awayTeam;

    @SerializedName("bookmakers")
    private List<Bookmaker> bookmakers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportKey() {
        return sportKey;
    }

    public void setSportKey(String sportKey) {
        this.sportKey = sportKey;
    }

    public String getSportTitle() {
        return sportTitle;
    }

    public void setSportTitle(String sportTitle) {
        this.sportTitle = sportTitle;
    }

    public String getCommenceTime() {
        return commenceTime;
    }

    public void setCommenceTime(String commenceTime) {
        this.commenceTime = commenceTime;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public List<Bookmaker> getBookmakers() {
        return bookmakers;
    }

    public void setBookmakers(List<Bookmaker> bookmakers) {
        this.bookmakers = bookmakers;
    }

    public class Bookmaker {
        @SerializedName("key")
        private String key;

        @SerializedName("title")
        private String title;

        @SerializedName("last_update")
        private String lastUpdate;

        @SerializedName("markets")
        private List<Market> markets;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(String lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public List<Market> getMarkets() {
            return markets;
        }

        public void setMarkets(List<Market> markets) {
            this.markets = markets;
        }

        public class Market {
            @SerializedName("key")
            private String key;

            @SerializedName("last_update")
            private String lastUpdate;

            @SerializedName("outcomes")
            private List<Outcome> outcomes;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getLastUpdate() {
                return lastUpdate;
            }

            public void setLastUpdate(String lastUpdate) {
                this.lastUpdate = lastUpdate;
            }

            public List<Outcome> getOutcomes() {
                return outcomes;
            }

            public void setOutcomes(List<Outcome> outcomes) {
                this.outcomes = outcomes;
            }

            public class Outcome {
                @SerializedName("name")
                private String name;

                @SerializedName("price")
                private int price;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }
            }
        }
    }

}
